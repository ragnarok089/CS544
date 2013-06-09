/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the state in which you have requested to chat with another
 * 	client and sent them the handshake but they have not yet accepted
 */
package States;

import java.io.IOException;

import Communications.TCP;
import Communications.UDPSender;
import Messages.ChatMsgMessage;
import Messages.ClientAcceptMessage;
import Messages.DeclineConnectMessage;
import Messages.ErrorMessage;
import Messages.Message;
import Messages.ServerAcceptMessage;

//STATEFUL
public class MidhandshakeClient extends State{
	
	//this takes the data and returns the next state the client should be in
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState,boolean firstCall){
		//if its the first time in the state print the message
		if(firstCall){
			System.out.println("Waiting for the client to accept your invitation to chat.\nTo cancel type :dc");
		}
		//if there is a sudden disconnection goto disconnect
		if(tcp.getActive()==false){
			try {
				tcp.close();
			} catch (Exception e) {}
			return new Disconnected();
		}
		// if you give any command not already cuaght
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
		// if you receive a correct client accept message goto chatting
		else if (tcpMessage instanceof ClientAcceptMessage
				&& tcpMessage.getCorrect()) {
			return new Chatting();
		}
		//if you receive a server accept message close and go to disconnected
		else if((tcpMessage instanceof ServerAcceptMessage) && tcpMessage.getCorrect()){
			try {
				tcp.close();
			} catch (IOException e) {}
			System.out.println("Got a server accept Message from the client.\nDisconnecting");
			return new Disconnected();
		}
		//if you get a decline message disconnect and go to disconnected
		else if((tcpMessage instanceof DeclineConnectMessage) && tcpMessage.getCorrect()){
			try {
				tcp.close();
			} catch (IOException e) {}
			System.out.println("Other client declined to chat.\nDisconnecting");
			return new Disconnected();
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
