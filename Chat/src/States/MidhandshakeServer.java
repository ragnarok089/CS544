/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the state for the client in which the user has connected to a server
 * 	and sent the handshake but the server has not yet responded
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
public class MidhandshakeServer extends State{
	//take in the input and return the next state to be in
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState,boolean firstCall){
		if(firstCall){
			//if its the first call print this message
			System.out.println("Waiting for the server to accept your invitation to chat.\nTo cancel type :dc");
		}
		//if the connection suddenly dies disconnect and go to disconnected
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
		//if you receive a correct server accept message go to connected server
		else if(tcpMessage instanceof ServerAcceptMessage && tcpMessage.getCorrect()){
			return new ConnectedServer();
		}
		// if you receive a client accept message close and go to disconnected
		else if ((tcpMessage instanceof ClientAcceptMessage)
				&& tcpMessage.getCorrect()) {
			try {
				tcp.close();
			} catch (IOException e) {
			}
			System.out
					.println("Got a client accept Message from the server.\nDisconnecting");
			return new Disconnected();
		}
		// if you get a decline message disconnect and go to disconnected
		else if ((tcpMessage instanceof DeclineConnectMessage)
				&& tcpMessage.getCorrect()) {
			try {
				tcp.close();
			} catch (IOException e) {
			}
			System.out.println("The server declined to chat.\nDisconnecting");
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
