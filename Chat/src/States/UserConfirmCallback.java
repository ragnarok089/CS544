/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the state for the client in which the client has received
 * 	a udp request for a callback. It has asked the user if they wish
 * 	to connect and either does or doesnt as the user wants
 */
package States;

import Communications.TCP;
import Communications.UDPSender;
import Messages.Message;

public class UserConfirmCallback extends State{

	//this takes some input and returns the next state the client should be in
	public State process(String input, TCP tcp, UDPSender us, Message udpMessage, Message tcpMessage, long timeEnteredState,boolean firstCall) {
		if(firstCall){
			//if this is the first call print this message
			System.out.println("To connect to the caller, type :y.\nTo reject, type :n or :dc");
		}
		//if there is a sudden connection go to the appropriate connected state
		if(tcp.getActive()){
			if(tcp.initiator){
				return new ConnectedInitiator();
			}
			else{
				return new ConnectedReceiver();
			}
		}
		//if the user gives the yes command
		else if(input.startsWith(":y")){
			//if the connection cannot be established
			if(0>tcp.connect(tcp.pendingIP)){
				//print close and goto disconnected
				System.out.println("Could not connect to target");
				try{
					tcp.close();
				}
				catch(Exception e){}
				return new Disconnected();
			}
			return this;
		}
		//if the user gives the no command go to disconnected
		else if(input.startsWith(":n")){
			return new Disconnected();
		}
		//if any other command is given print the message
		else if(input.startsWith(":")){
			System.out.println("Invalid command");
			return this;
		}
		//if anything was typed print the message
		else if(!input.equals("")){
			System.out.println("You cannot chat in this state");
			return this;
		}
		return this;
	}

}
