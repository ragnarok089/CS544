package States;

import java.io.IOException;

import Communications.TCP;
import Communications.UDPSender;
import Messages.ClientAcceptMessage;
import Messages.DeclineConnectMessage;
import Messages.ErrorMessage;
import Messages.Message;
import Messages.ServerAcceptMessage;


public class MidhandshakeClient extends State{
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState){
		
		if(tcp.getActive()==false){
			try {
				tcp.close();
			} catch (IOException e) {}
			System.out.println("The other side disconnected");
			return new Disconnected();
		}
		else if(tcpMessage instanceof ClientAcceptMessage && tcpMessage.getCorrect()){
			return new Chatting();
		}
		else if((tcpMessage instanceof ServerAcceptMessage || tcpMessage instanceof DeclineConnectMessage) && tcpMessage.getCorrect()){
			try {
				tcp.close();
			} catch (IOException e) {}
			System.out.println("Expected a client response but got a server response");
			return new Disconnected();
		}
		else if(tcpMessage!=null){
			tcp.send(new ErrorMessage(13,Message.minSize,0,"", new byte[0]));
			return this;
		}
		else if(input.startsWith(":")){
			System.out.println("Inavlid command");
			return this;
		}
		else if(!input.equals("")){
			System.out.println("You cannot chat in this state");
			return this;
		}
		else{
			return this;
		}
	}
}
