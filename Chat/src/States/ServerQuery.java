package States;

import java.io.IOException;

import Communications.*;
import Messages.*;


public class ServerQuery extends State{
	public State process(String input, TCP tcp, UDPSender us, Message udpMessage, Message tcpMessage, long timeEnteredState) {
		if (tcp.getActive() == false) {
			System.out.println("Server disconnected");
			return new Disconnected();
			
		} else if (input.startsWith(":exit")) {
			try {
				tcp.close();
			} catch (IOException e) {
			}
			System.out.println("Disconnecting");
			return new Disconnected();
		} else if (tcpMessage instanceof LookupFailedMessage && tcpMessage.getCorrect()) {
			System.out.println("There is no binding on the server for that name");
			return new ConnectedServer();
		} else if (tcpMessage instanceof ServerSendsInfoMessage && tcpMessage.getCorrect()) {
			System.out.println("The IP address of that name is: "+((ServerSendsInfoMessage)tcpMessage).targetIP);
			return new ConnectedServer();
		} else {
			return this;
		}
	}
}
