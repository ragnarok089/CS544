/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the state for the client in which the client has sent
 * 	a local broadcast looking for a given user and is waiting
 * 	for a callback
 */
package States;

import Communications.*;
import Messages.*;

//STATEFUL
public class Waiting extends State{
	public static final long timeout=60000;//the length of time it waits before disconnecting
	
	//takes the input and returns the next state to be in
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState,boolean firstCall){
		if(firstCall){
			//if this is the first time in this state, print this message
			System.out.println("Waiting for a response for at most 60 seconds. To cancel, type :dc");
		}
		//if the connection occurs go to the appropriate connected state
		if(tcp.getActive()==true){
			System.out.println("Connection established");
			if(tcp.initiator){
				return new ConnectedInitiator();
			}
			else{
				return new ConnectedReceiver();
			}
		}
		//if there is a timeout then return to disconnected
		else if (System.currentTimeMillis() - timeEnteredState > timeout) {
			System.out
					.println("No response was received within the time limit");
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
		else {
			return this;
		}
	}
}
