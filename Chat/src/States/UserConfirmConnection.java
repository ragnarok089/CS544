/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the state for the client in which the user has been
 * 	connected to and was sent the handshake. the computer is waiting
 * 	for the user to confirm or reject the connection
 */
package States;

import Communications.TCP;
import Communications.UDPSender;
import Messages.ClientAcceptMessage;
import Messages.DeclineConnectMessage;
import Messages.ErrorMessage;
import Messages.Message;

public class UserConfirmConnection extends State{

	//takes the input and returns the next state the user should be in
	public State process(String input, TCP tcp, UDPSender us, Message udpMessage, Message tcpMessage, long timeEnteredState,boolean firstCall) {
		if(firstCall){
			//if this is the first time in this state print the message
			System.out.println("To connect to the caller type :y\nTo reject, type :n or :dc");
		}
		//if the connection suddenly dies disconnect and go to disconnected
		if(!tcp.getActive()){
			try{
				tcp.close();
			}catch(Exception e){}
			System.out.println("The other side disconnected");
			return new Disconnected();
		}
		//if the user gives the yes command send a client accept message and go to chatting
		else if(input.startsWith(":y")){
			tcp.send(new ClientAcceptMessage(5,Message.minSize,0,"",new byte[0]));
			return new Chatting();
		}
		//if the user gives teh no command send a decline message and disconnect and go to disconnected
		else if(input.startsWith(":n")){
			tcp.send(new DeclineConnectMessage(10,Message.minSize,0,"",new byte[0]));
			try{
				tcp.close();
			}
			catch(Exception e){}
			return new Disconnected();
		}
		//if any other command is given print the message
		else if(input.startsWith(":")){
			System.out.println("Invalid command");
			return this;
		}
		// if anything was typed
		else if (!input.equals("")) {
			System.out.println("You cannot chat in this state");
			return this;
		}
		// if you receive any other kind of message, send an error message
		else if (tcpMessage != null) {
			tcp.send(new ErrorMessage(13, Message.minSize, 0, "", new byte[0]));
			return this;
		}
		return this;
	}

}