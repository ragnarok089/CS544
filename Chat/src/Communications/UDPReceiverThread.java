/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This class asynchronously reads from the UDP socket and sticks 
 * 	the data on a queue to be read by the UDP receiver.
 */
package Communications;
import java.io.*;
import java.net.*;
//import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;


public class UDPReceiverThread implements Runnable {
		protected DatagramSocket socket=null;//the socket to read from
		protected ConcurrentLinkedQueue<Byte> out =null;//the queue to add the data to
		protected volatile boolean running;//a boolean to indicate whether the loop should continue
		
		public UDPReceiverThread(ConcurrentLinkedQueue<Byte> queue) throws SocketException{
			try {
				//SERVICE
				socket = new DatagramSocket(12346,InetAddress.getByName("0.0.0.0"));//setup a socket listening for any UDP packets on that socket
			} catch (UnknownHostException e) {
				System.out.println("Failed to create UDP receiver");
			}
			//set timeout to .5 sec
			socket.setSoTimeout(500);
			//copy the queue over
			out=queue;
		}
		
		//sets the isRunning bit
		public void setRunning(boolean isRunning){
			running=isRunning;
		}
		
		//This constantly reads from the udp packet and sticks the resulting bits onto the queue
		public void run(){
			running=true;
			byte[] buffer=new byte[5000];//the buffer that stores the read bits
			DatagramPacket packet= new DatagramPacket(buffer,buffer.length);//setup a packet
			//keep running until stopped
			while(running){
				try{
					//read in a packet
					socket.receive(packet);
					//copy the packet to the queue
					byte[] data=packet.getData();
					for(int i=0;i<packet.getLength();i++){
						out.add(data[i]);
					}
				}
				//do nothing on timeout
				catch(SocketTimeoutException e){}
				//if there was a strange error stop
				catch (IOException e) {
					System.out.println("socket receive error");
					running=false;
				}
			}
			//cleanup
			socket.close();
		}
		

}
