/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the main loop for the client.
 * 	It sets up all that it needs and then loops over a state machine.
 * 	On each pass reads input from STDIN, whether this is the first call
 * 	of this state, and how long it has been in this state tcp, and udp 
 * 	and passes it into the state. The state then processes it and becomes 
 * 	a new state that is then fed into on the next pass. This continues 
 * 	until the :quit command is detected, at which point it stops.
 */

import java.net.SocketException;
import java.net.UnknownHostException;

import Communications.TCP;
import Communications.UDPReceiver;
import Communications.UDPSender;
import Messages.ErrorMessage;
import Messages.Message;
import States.Disconnected;
import States.State;
import Utilities.CurrentState;
import Utilities.InputReader;
import Utilities.User;


public class MainLoop {
	static boolean error =false; //flag to indicate there has been an error
	static boolean done = false; //flag to indicate the main loop should stop
	static InputReader ir = new InputReader(); //Reads stdin
	static String input; //stores the string read
	static TCP tcp=new TCP(); //wrapper for all our tcp capabilities
	static UDPReceiver ur; //wrapper for receiving UDP
	static UDPSender us; //wrapper for sending UDP
	static CurrentState state=new CurrentState(); //a wrapper for the current state
	static State lastState=null; //stores the last state
	static boolean firstCall=true; //indicates whether this is the first call of a state
	
	
	public static void main(String[] args) {
		state.setup(); //sets state to Disconnected
		try { //setup UDP
			ur=new UDPReceiver();
			us=new UDPSender();
		} catch (SocketException | UnknownHostException e) {
			System.out.println("Could not set up UDP socket");
			System.exit(-1);
		}
		Thread tcpListen=new Thread(tcp);//setup tcp
		tcpListen.start(); //start listening for a connection
		Thread irThread=new Thread(ir);//kick off the stdin reader
		irThread.start();
		long timeEnteredState=System.currentTimeMillis();//initialize variables
		Message udpMessage=null;
		Message tcpMessage=null;
		
		//print welcome message
		System.out.println("Welcome to our chat client");
		System.out.println("To quit, type :quit");
		System.out.println("To disconnect at any time, type :dc");
		System.out.println("To change your username, type :user <new username>");
		
		//Main loop that goes until it either errors or is done
		while(!error && !done){
			//if the current state is different from the last state
			//say its the first call and reset the timeEntered counter
			if(lastState==null || !state.state.getClass().equals(lastState.getClass())){
				firstCall=true;
				timeEnteredState=System.currentTimeMillis();
			}
			else{
				firstCall=false;
			}
			//UI
			input=ir.getSubmitted();//read from STDIN
			//disconnect if command specified
			if(input.startsWith(":dc")){
				System.out.println("Disconnecting");
				try{
					tcp.close();
				}
				catch(Exception e){}
				state.state=new Disconnected();
				continue;
			}
			//quit if command specified
			else if(input.startsWith(":quit")){
				done=true;
				System.out.println("Quitting");
				continue;
			}
			//change the username if specified (Defaults to "Unknown")
			else if(input.startsWith(":user")){
				if(input.length()<7){
					System.out.println("An argument is required");
					continue;
				}
				//User is a static class that just store the username
				User.setUserName(input.substring(6).trim());
				System.out.println("Your username has been set to \""+User.getUserName()+"\"");
				continue;
			}
			//Only one of tcp, udp, STDIN can be non-trivial to prevent conflicting and dropped
			//states. Instead only one is read at a time and the rest are queued
			
			//otherwise check tcp and udp
			else if (input.equals("")) {
				udpMessage = ur.read();//ready udp
				//if no udp check tcp
				if(udpMessage==null){
					tcpMessage = tcp.read();
					//If there is an error disconnect and go to Disconnected
					if(tcpMessage instanceof ErrorMessage && tcpMessage.getCorrect()){
						System.out.println("An error occured while communicating.\nDisconnecting");
						try {
							tcp.close();
						} catch (Exception e) {}
						state.state=new Disconnected();
						continue;
					}
				}
			}
			//set lastState
			lastState = state.getState();
			//run the input through the current state (automatically changes to new state)
			
			//STATEFUL
			state.process(input,tcp,us,udpMessage,tcpMessage,timeEnteredState,firstCall);

		}
		//cleanup
		
		//Something about the java scanner won't close without a \n
		System.out.println("\n\nHit enter to finish exiting");
		try {
			tcp.close();
		} catch (Exception e1) {}
		try{
			tcp.stop();
		}catch(Exception e){}
		try{
			ir.stop();
		}
		catch(Exception e){}
		try {
			ur.stop();
		} catch (Exception e) {}
	}

}
