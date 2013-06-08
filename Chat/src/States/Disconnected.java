/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the state for the client in which the user is disconnected by everything
 * 	from here they can initiate a connection by using the :ip or :host commands
 * 	or request a callback using :local. They also must be in this state to accept connections
 * 	form other clients
 */
package States;

import java.io.IOException;

import Communications.*;
import Messages.*;
import Utilities.*;


public class Disconnected extends State {
	
	//this function takes the current input and returns the next state the client should be in
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState,boolean firstCall){
		if(firstCall){
			//if its the first time in this state print messages
			System.out.println("You are disconnected. To attempt to automatically find a chatter locally, type :local <username>");
			System.out.println("To talk to someone in particular use the :ip <ip address> or the :host <host name> commands");
		}
		//if there is a connection go to the appropriate connected state
		if(tcp.getActive()==true){
			if(tcp.initiator){
				return new ConnectedInitiator();
			}
			else{
				return new ConnectedReceiver();
			}
		}
		//if you give an ip command
		else if(input.startsWith(":ip ")){
			//if there was no argument
			if(input.length()<5){
				System.out.println("An argument is required");
				return this;
			}
			//if the connection failed
			else if(0>tcp.connect(input.substring(4))){
				System.out.println("Unable to connect to IP address");
			}
			return this;
		}
		//if the host command was given do the same as ip but with a longer offset
		else if(input.startsWith(":host ")){
			if(input.length()<7){
				System.out.println("An argument is required");
				return this;
			}
			if(0>tcp.connect(input.substring(6))){
				System.out.println("Unable to connect to IP address");
			}
			return this;
		}
		//if the local command is given
		else if(input.startsWith(":local ")){
			//check to make sure it has the proper arugments
			if(input.length()<8){
				System.out.println("An argument is required");
				return this;
			}
			try {
				//gather the data
				String senderUsername=User.getUserName();
				String targetUsername=input.substring(7);
				String ip=tcp.getIP();
				//send a message requesting a callback from the username given as an argument
				UDPBroadcastMessage message=new UDPBroadcastMessage(1,(long)144,(long)0,"",senderUsername,targetUsername,ip);
				us.sendMessage(message);
			} catch (IOException e) {}
			return new Waiting();
		}
		//if its any other command 
		else if(input.startsWith(":")){
			System.out.println("Invalid command");
			return this;
		}
		//if they typing is not a command
		else if(!input.equals("")){
			System.out.println("You cannot chat in this state");
			return this;
		}
		//if you recieve a UDP broadcast
		else if(udpMessage instanceof UDPBroadcastMessage && udpMessage.getCorrect()){
				UDPBroadcastMessage m=(UDPBroadcastMessage)udpMessage;
				//if the requested user is you
				if(m.targetUsername.trim().equals(User.userName.trim())){
					//print a message and go to user confirm callback
					System.out.println("Received a broadcast with to your username from "+m.senderUsername+" at "+m.senderIP);
					//store the senders ip for use later
					tcp.pendingIP=m.senderIP;
					return new UserConfirmCallback();
				}
			return this;
		}
		else{
			return this;
		}
	}
	

}
