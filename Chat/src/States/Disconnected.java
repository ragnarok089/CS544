package States;

import java.io.IOException;

import Communications.*;
import Messages.*;
import Utilities.*;


public class Disconnected extends State {
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState,boolean firstCall){
		if(firstCall){
			System.out.println("You are disconnected. To attempt to automatically find a chatter locally, type :local <username>");
			System.out.println("To talk to someone in particular use the :ip <ip address> or the :host <host name> commands");
		}
		if(tcp.getActive()==true){
			if(tcp.initiator){
				return new ConnectedInitiator();
			}
			else{
				return new ConnectedReceiver();
			}
		}
		else if(input.startsWith(":ip ")){
			if(input.length()<5){
				System.out.println("An argument is required");
				return this;
			}
			if(0>tcp.connect(input.substring(4))){
				System.out.println("Unable to connect to IP address");
			}
			return this;
		}
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
		else if(input.startsWith(":local ")){
			if(input.length()<8){
				System.out.println("An argument is required");
				return this;
			}
			try {
				String senderUsername=User.getUserName();
				String targetUsername=input.substring(7);
				String ip=tcp.getIP();
				UDPBroadcastMessage message=new UDPBroadcastMessage(1,(long)144,(long)0,"",senderUsername,targetUsername,ip);
				us.sendMessage(message);
			} catch (IOException e) {}
			return new Waiting();
		}
		else if(input.startsWith(":")){
			System.out.println("Invalid command");
			return this;
		}
		else if(!input.equals("")){
			System.out.println("You cannot chat in this state");
			return this;
		}
		else if(udpMessage instanceof UDPBroadcastMessage && udpMessage.getCorrect()){
				UDPBroadcastMessage m=(UDPBroadcastMessage)udpMessage;
				if(m.targetUsername.trim().equals(User.userName.trim())){
					System.out.println("Received a broadcast with to your username from "+m.senderUsername+" at "+m.senderIP);
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
