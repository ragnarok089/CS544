/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the main loop for the server
 * 	It continues until the quit command is given or there is an error
 * 	It constant tries to accept on a server socket and when there is
 *  a connection, it spawns a new thread and kicks it off to handle that
 *  connection. It then goes back to listening on the server socket
 */

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

import Communications.*;
import Server.ServerThread;
import Utilities.InputReader;

public class ServerMainLoop {
	static TCP tcp=null; //storage for the spawned connections
	static boolean error=false;//indicates if the loop should stop due to error
	static boolean skip=false; //indicates if there was a timeout
	static boolean done=false; //indicates if the loop was ended
	static InputReader ir = new InputReader(); //the reader of STDIN
	public static InetAddress ip=null; //stores the ip of the serversocket
	static ServerSocket serverSocket=null; //initialize variables
	static Socket socket=null;
	static Thread t=null;
	
	
	public static void main(String[] args){
		//This block attempts to find a connection with an ip starting with 192.168.224.
		//We were getting strange errors because tux was trying to connect on invalid
		//connects so we added this in. Unfortunately, it stops it from working on
		//anything but tux
		try {
			//gather up all the interfaces
			InetAddress address = null;
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			//look through the interfaces until you find the one you want
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback())
					continue; // Don't want to broadcast to the loopback
								// interface
				Enumeration<InetAddress> addresses = networkInterface
						.getInetAddresses();
				//Look through all the addresses on this interface
				while (addresses.hasMoreElements()) {
					address = addresses.nextElement();
					//if it contains 192.168.224 stop looking
					if (!address.getHostAddress().contains("192.168.224"))
						continue;
					break;
				}
				//if its of the proper form stop looking
				if (address != null && address.toString().contains("192.168.224")) {
					break;
				}
			}
			ip=address;
		}catch(Exception e){
			//an error occured
			System.out.println("Unable to launch server");
			error=true;
		}
		if(!error){
			try{
				//if the ip was gotten correctly setup the serversocket
				//SERVICE
				serverSocket=new ServerSocket(12345, 0, ip);
				//set the timeout to .5 sec
				serverSocket.setSoTimeout(500);
			}
			catch(Exception e){}
		}
		
		Thread irThread=new Thread(ir);//kick off the STDIN reader
		irThread.start();
		
		//display weclcome message
		System.out.println("Welcome to the chat server");
		System.out.println("To quit, type :quit");
		
		//loop continues until its done or there's an error
		while(!done && !error){
			//read from stdin
			//UI
			String input=ir.getSubmitted();
			//if the command is quit, quit
			if(input.startsWith(":quit")){
				done=true;
				System.out.println("Quitting");
				continue;
			}
			//try to accept the new connection
			try {
				socket = serverSocket.accept();
				skip = false;
			} catch (Exception e) {
				//this means it timed out
				skip = true;
			}
			//if it didnt' timeout
			if (!skip) {
				//CONCURRENT
				//setup a new thread
				ServerThread s=new ServerThread();
				//setup the new TCP for the thread
				tcp=new TCP();
				tcp.initiator = false;
				tcp.socket = socket;
				socket=null;
				tcp.active = true;
				tcp.tr.setSocket(tcp.socket);
				//kickoff the tcp listener thread
				t = new Thread(tcp.tr);
				t.start();
				//give the new serverthread the tcp
				s.setTCP(tcp);
				//kickoff the server thread
				new Thread(s).start();
			}
			try {
				//sleep for .01 sec
				Thread.sleep(10);
			} catch (Exception e) {}
		}
		//cleanup
		
		//There is a strange error where java scanner won't quit without a \n
		System.out.println("Hit enter to finish quitting");
		try{
			tcp.close();
		}catch(Exception e){}
		try{
			tcp.stop();
		}catch(Exception e){}
		ir.stop();
		//In throry the threads could still be running
		//in an effort to not cause problems we made it wait for them to finish
		System.out.println("If quitting does not occur please make sure there are no connections to the server");
	}
}
