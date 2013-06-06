package States;

import Communications.*;
import Messages.*;


public class ServerUpdate extends State {
	public State process(String input, TCP tcp, UDPSender us, Message udpMessage, Message tcpMessage, long timeEnteredState) {
		if (tcp.getActive() == false) {
			System.out.println("Server disconnected");
			return new Disconnected();
		} 
		else if (tcpMessage instanceof NameCollisionMessage && tcpMessage.getCorrect()) {
			System.out.println("There is already a binding on the server for your name");
			return new ConnectedServer();
		} 
		else if (tcpMessage instanceof ServerConfirmationUpdateMessage && tcpMessage.getCorrect()) {
			System.out.println("Your name has been sucessfully bound");
			return new ConnectedServer();
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
		else {
			return this;
		}
	}
}
