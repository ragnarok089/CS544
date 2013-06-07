package States;

import Communications.*;
import Messages.*;


public class ServerUpdate extends State {
	public State process(String input, TCP tcp, UDPSender us, Message udpMessage, Message tcpMessage, long timeEnteredState,boolean firstCall) {
		if(firstCall){
			System.out.println("Waiting for the server to respond to your update request\nTo cancel type :dc");
		}
		if (tcp.getActive() == false) {
			System.out.println("Server disconnected");
			return new Disconnected();
			
		} else if (tcpMessage instanceof NameCollisionMessage && tcpMessage.getCorrect()) {
			System.out.println("There is already a binding on the server for your name");
			return new ConnectedServer();
		} else if (tcpMessage instanceof ServerConfirmationUpdateMessage && tcpMessage.getCorrect()) {
			System.out.println("Your name has been sucessfully bound");
			return new ConnectedServer();
		} else {
			return this;
		}
	}
}
