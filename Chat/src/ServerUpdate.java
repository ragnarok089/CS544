import java.io.IOException;


public class ServerUpdate extends State {
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
		} else if (tcpMessage is instanceof NameCollision) {
			System.out.println("There is already a binding on the server for your name");
			return new ConnectedServer();
		} else if (tcpMessage is instanceof ConfirmationMessage) {
			System.out.println("Your name has been sucessfully bound");
			return new ConnectedServer();
		} else {
			return this;
		}
	}
}
