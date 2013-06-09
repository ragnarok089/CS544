/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is a wrapper that sends a UDP message
 * 	over the specified port
 */
package Communications;
import java.io.*;
import java.net.*;
import java.util.Enumeration;
import Messages.Message;

public class UDPSender {
	protected int port;//the port to send on
	protected DatagramSocket socket;//the socket to send on
	InetAddress ip;//the ip address you should send over (should be a broadcast address)
	
	public UDPSender() throws SocketException, UnknownHostException{
		//setup the socket
		//SERVICE
		port =12345;
		socket=new DatagramSocket(port);
		socket.setBroadcast(true);
		InetAddress broadcast = null;
		//This block attempts to find a connection with an ip starting with 192.168.224.
    	//We were getting strange errors because tux was trying to connect on invalid
    	//connects so we added this in. Unfortunately, it stops it from working on
    	//anything but tux
		//gather up all the interfaces
		Enumeration<NetworkInterface> interfaces =NetworkInterface.getNetworkInterfaces();
		//look through the interfaces until you find the one you want
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();
			
			if (networkInterface.isLoopback())
				continue;    // Don't want to broadcast to the loopback interface
			//Look through all the addresses on this interface
			for (InterfaceAddress interfaceAddress :
				networkInterface.getInterfaceAddresses()) {
				broadcast = interfaceAddress.getBroadcast();
				//if it contains 192.168.224 stop looking
				if (broadcast == null)
					continue;
				break;
			}
			//if it contains 192.168.224 stop looking
			if(broadcast!=null && broadcast.toString().contains("192.168.224")){
				break;
			}
		}
		//If there wasn't one of that kind error out
		if(broadcast==null){
			throw new UnknownHostException("No broadcast address");
		}
		ip = broadcast;
	}
	
	//converts the message to byte[] and then sends it as a UDP packet over the port
	public void sendMessage(Message message) throws IOException{
		//convert to byte[]
		byte[] buffer=message.convert();
		//construct the packet
		//SERVICE
		DatagramPacket packet = new DatagramPacket(buffer,buffer.length,ip,12346);
		//send the packet
		socket.send(packet);
	}
	
	//closes the socket
	public void close(){
		socket.close();
	}
}
