/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the state for the client in which the client has requested
 * 	some data and is waiting for the reply from the server
 */
package States;

import Communications.*;
import Messages.*;


public class ServerQuery extends State{
	//takes the input and returns the next state to be in
	public State process(String input, TCP tcp, UDPSender us, Message udpMessage, Message tcpMessage, long timeEnteredState,boolean firstCall) {
		if(firstCall){
			//if its the first call print this message
			System.out.println("Waiting for the server to respond to your query\nTo cancel type :dc");
		}
		//if there is a sudden disconnection close and go to disconnected
		if (tcp.getActive() == false) {
			System.out.println("Server disconnected");
			try{
				tcp.close();
			}
			catch(Exception e){}
			return new Disconnected();
			
		}
		//if you receive a lookup failed message print the message and go to connected server
		else if (tcpMessage instanceof LookupFailedMessage && tcpMessage.getCorrect()) {
			System.out.println("There is no binding on the server for that name");
			return new ConnectedServer();
		}
		//if you receive the data, display it and go to connected server
		else if (tcpMessage instanceof ServerSendsInfoMessage && tcpMessage.getCorrect()) {
			System.out.println("The IP address of that name is: "+((ServerSendsInfoMessage)tcpMessage).targetIP);
			return new ConnectedServer();
		}
		// if you receive any other kind of message, send an error message
		else if (tcpMessage != null) {
			tcp.send(new ErrorMessage(13, Message.minSize, 0, "", new byte[0]));
			return this;
		} 
		// if you give any command not already caught
		else if (input.startsWith(":")) {
			System.out.println("Invalid command");
			return this;
		}
		// if there is a message send it over tcp
		else if (!input.equals("")) {
			Message message = new ChatMsgMessage(11, Message.minSize
					+ Message.minSize, 0, "", input);
			tcp.send(message);
			return this;
		} 
		else {
			return this;
		}
	}
}
