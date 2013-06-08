/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the state for the client in which it has sucessfully connected
 * 	to the server and has already exchanged handshakes
 */
package States;

import Communications.*;
import Messages.*;
import Utilities.User;

public class ConnectedServer extends State {
	
	//this takes the data and returns the proper next state
	public State process(String input, TCP tcp, UDPSender us, Message udpMessage, Message tcpMessage, long timeEnteredState,boolean firstCall) {
		if(firstCall){
			//if its the first time in this state print this message
			System.out.println("You are connected to a server.\nType :update to try to bind your name to your ip.\nType :query <username> to ask the server for the ip of that username\nType :dc to disconnect");
		}
		//if the connection suddenly dies cleanup and go to disconnected
		if (tcp.getActive() == false) {
			System.out.println("Server disconnected");
			try{
				tcp.close();
			}
			catch(Exception e){}
			return new Disconnected();	
		} 
		//if the user gives the update command
		else if (input.startsWith(":update")) {
			//construct a new clientrequestupdate message using the current users name and ip
			Message message=new ClientRequestUpdateMessage(6,ClientRequestUpdateMessage.minSize+Message.minSize,0,"",User.userName,tcp.getIP());
			tcp.send(message);
			return new ServerUpdate();
		}
		//if the user gives the query command
		else if (input.startsWith(":query ")) {
			//send the request info message to the server with the argument as the username
			Message message=new ClientRequestInfoMessage(8,ClientRequestInfoMessage.minSize+Message.minSize,0,"",User.userName,input.substring(7).trim(),tcp.getIP());
			System.out.println(tcp.send(message));
			return new ServerQuery();
		}
		// if you recieve any other kind of message, send an error message
		else if (tcpMessage != null) {
			tcp.send(new ErrorMessage(13, Message.minSize, 0, "", new byte[0]));
			return this;
		} 
		else {
			return this;
		}
	}
}
