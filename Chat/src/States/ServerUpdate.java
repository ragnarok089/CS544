/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the state for the client in which the client has requested
 * 	the lookup table be updated and is waiting for the reply from the server
 */
package States;

import Communications.*;
import Messages.*;

//STATEFUL
public class ServerUpdate extends State {
	
	//takes the input and returns the next state to be placed into
	public State process(String input, TCP tcp, UDPSender us, Message udpMessage, Message tcpMessage, long timeEnteredState,boolean firstCall) {
		if(firstCall){
			//if this is the first call print this message
			System.out.println("Waiting for the server to respond to your update request\nTo cancel type :dc");
		}
		//if the connection suddenly dies disconnect and go to disconnected
		if (tcp.getActive() == false) {
			System.out.println("Server disconnected");
			try{
				tcp.close();
			}
			catch(Exception e){}
			return new Disconnected();
			
		}
		//if you get a name collision print the message and go to connected server
		else if (tcpMessage instanceof NameCollisionMessage && tcpMessage.getCorrect()) {
			System.out.println("There is already a binding on the server for your name");
			return new ConnectedServer();
		}
		//if you get a confirmation print the message and go to connected server
		else if (tcpMessage instanceof ServerConfirmationUpdateMessage && tcpMessage.getCorrect()) {
			System.out.println("Your name has been sucessfully bound");
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
