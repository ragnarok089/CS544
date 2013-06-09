/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the server state when the server if first connected
 * 	if it receives a handshake it returns an acceptance
 * 	if given anything else, itdisconnects.
 * 	Eventually it will disconnect on its own if no progress is made
 */
package Server;

import java.io.IOException;

import Communications.TCP;
import Messages.*;
//STATEFUL
public class ServerConnected extends ServerState{

	//takes in a tcp, tcpMessage, and how long its been in this state and returns the next
	//state the server should be in
	public ServerState process(TCP tcp, Message tcpMessage, long timeEnteredState) {
		//if the message is a valid serverhandshake
		if(tcpMessage instanceof ServerHandShakeMessage && tcpMessage.getCorrect()){
			//send a server accept and return the state serverReady
			Message message = new ServerAcceptMessage(4,Message.minSize,0,"",new byte[0]);
			tcp.send(message);
			return new ServerReady();
		}
		//if there was a different message
		else if(tcpMessage!=null){
			//return an error message
			tcp.send(new ErrorMessage(13,Message.minSize,0,"",new byte[0]));
			try {
				//close the port
				tcp.close();
			} catch (IOException e) {}
			//go to disconnected
			return new ServerDisconnected();
		}
		//if it has been more than 30 seconds 
		else if(System.currentTimeMillis()-timeEnteredState>30000){
			try {
				//close the connection
				tcp.close();
			} catch (IOException e) {}
			//go to disconnected
			return new ServerDisconnected();
		}
		return this;
	}

}
