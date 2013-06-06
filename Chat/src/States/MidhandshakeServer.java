package States;

import java.io.IOException;

import Communications.TCP;
import Communications.UDPSender;
import Messages.ClientAcceptMessage;
import Messages.DeclineConnectMessage;
import Messages.Message;
import Messages.ServerAcceptMessage;


public class MidhandshakeServer extends State{
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState){
		
		if(tcp.getActive()==false){
			try {
				tcp.close();
			} catch (IOException e) {}
			return new Disconnected();
		}
		else if(input.startsWith(":exit")){
			try {
				tcp.close();
			} catch (IOException e) {}
			System.out.println("Disconnecting");
			return new Disconnected();
		}
		else if(tcpMessage instanceof ServerAcceptMessage && tcpMessage.getCorrect()){
			return new ConnectedServer();
		}
		else if((tcpMessage instanceof ClientAcceptMessage || tcpMessage instanceof DeclineConnectMessage) && tcpMessage.getCorrect()){
			try {
				tcp.close();
			} catch (IOException e) {}
			System.out.println("Expected a server response but got a client response");
			return new Disconnected();
		}
		else{
			return this;
		}
	}
}
