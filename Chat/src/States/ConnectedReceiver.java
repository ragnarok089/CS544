/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the state for the client in which another client started
 * 	the connection and so this side is waiting for the other to send the 
 * 	proper handshake
 */
package States;

import Communications.TCP;
import Communications.UDPSender;
import Messages.ClientHandShakeMessage;
import Messages.ErrorMessage;
import Messages.Message;
import Messages.ServerHandShakeMessage;

public class ConnectedReceiver extends State{
	//this takes the input and returns the next state of the client
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState,boolean firstCall){
		if(firstCall){
			//if it is the first time in the state print his
			System.out.println("A connection has been established. Waiting for the handshake.\nTo cancel type :dc");
		}
		//if the connection suddenly dies
		if(tcp.getActive()==false){
			//disconnect and go to disconnected 
			System.out.println("The otherside disconnected");
			try{
				tcp.close();
			}
			catch(Exception e){}
			return new Disconnected();
		}
		//if you enter any command not already caught
		else if(input.startsWith(":")){
			System.out.println("Invalid command");
			return this;
		}
		//If the user enters any non command
		else if(!input.equals("")){
			System.out.println("You cannot chat yet");
			return this;
		}
		//if a client hand shake message is received
		else if (tcpMessage instanceof ClientHandShakeMessage && tcpMessage.getCorrect()) {
			//print a message and go to UserConfirm connection state
			ClientHandShakeMessage m=(ClientHandShakeMessage)tcpMessage;
			System.out.println("Do you wish to talk to "+m.senderUsername+" at "+m.senderIP);
			return new UserConfirmConnection();
		} 
		//if a server hand shake message is received
		else if (tcpMessage instanceof ServerHandShakeMessage && tcpMessage.getCorrect()) {
			//disconnect and go to disconnected
			System.out.println("The person who connected to you thinks you're a server.\nDisconnecting");
			try{
				tcp.close();
			}
			catch(Exception e){}
			return new Disconnected();
		}
		//if any other message is received
		else if (tcpMessage != null) {
			//send an error message
			tcp.send(new ErrorMessage(13,Message.minSize,0,"",new byte[0]));
			return this;
		}
		else{
			return this;
		}
	}
}
