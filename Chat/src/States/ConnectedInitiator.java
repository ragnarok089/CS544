package States;

import Communications.TCP;
import Communications.UDPSender;
import Messages.ClientHandShakeMessage;
import Messages.ErrorMessage;
import Messages.Message;
import Messages.ServerHandShakeMessage;
import Utilities.User;

public class ConnectedInitiator extends State{
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState,boolean firstCall){
		if(firstCall){
			System.out.println("A connection has been established. Type :client if you are connected to a client and :server if its a server");
		}
		if(tcp.getActive()==false){
			System.out.println("The otherside disconnected");

			try{
				tcp.close();
			}
			catch(Exception e){}
			return new Disconnected();
		}
		else if(input.startsWith(":server")){
			tcp.send(new ServerHandShakeMessage(2,ServerHandShakeMessage.minSize+Message.minSize,0,"",User.getUserName(),tcp.getIP()));
			return new MidhandshakeServer();
		}
		else if(input.startsWith(":client")){
			tcp.send(new ClientHandShakeMessage(3,ClientHandShakeMessage.minSize+Message.minSize,0,"",User.getUserName(),tcp.getIP()));
			return new MidhandshakeClient();
		}
		else if(input.startsWith(":")){
			System.out.println("Invalid command");
			return this;
		}
		else if(!input.equals("")){
			System.out.println("You cannot chat yet");
			return this;
		}
		else if (tcpMessage instanceof ClientHandShakeMessage && tcpMessage.getCorrect()) {
			ClientHandShakeMessage m=(ClientHandShakeMessage)tcpMessage;
			System.out.println("Do you wish to talk to "+m.senderUsername+" at "+m.senderUsername);
			return new UserConfirmConnection();
		} 
		else if ( (tcpMessage instanceof ClientHandShakeMessage|| tcpMessage instanceof ServerHandShakeMessage) && tcpMessage.getCorrect()) {
			System.out.println("You initiated the contact but the other person sent the handshake first.\nDisconnecting");
			try{
				tcp.close();
			}
			catch(Exception e){}
			return new Disconnected();
		}
		else if (tcpMessage != null) {
			tcp.send(new ErrorMessage(13,Message.minSize,0,"",new byte[0]));
			return this;
		}
		else{
			return this;
		}
	}
}
