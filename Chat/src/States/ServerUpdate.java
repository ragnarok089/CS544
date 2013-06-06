package States;

import java.io.IOException;

import Communications.*;
import Messages.*;


public class ServerUpdate extends State {
	public State process(String input, TCP tcp, UDPSender us, Message udpMessage, Message tcpMessage, long timeEnteredState) {
		if (tcp.getActive() == false) {
			System.out.println("\rServer disconnected");
			return new Disconnected();
			
		} else if (input.startsWith(":exit")) {
			try {
				tcp.close();
			} catch (IOException e) {
			}
			System.out.println("\rDisconnecting");
			return new Disconnected();
		} else if (tcpMessage instanceof NameCollisionMessage && tcpMessage.getCorrect()) {
			System.out.println("\rThere is already a binding on the server for your name");
			return new ConnectedServer();
		} else if (tcpMessage instanceof ServerConfirmationUpdateMessage && tcpMessage.getCorrect()) {
			System.out.println("\rYour name has been sucessfully bound");
			return new ConnectedServer();
		} else {
			return this;
		}
	}
}
