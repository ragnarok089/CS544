import java.io.IOException;

public class ConnectedServer extends State {
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
		} else if (input.startsWith(":update")) {
			// send update Message
			return new ServerUpdate();
			;
		} else if (input.startsWith(":query")) {
			// send query Message
			return new ServerQuery();
		} else {
			return this;
		}
	}
}
