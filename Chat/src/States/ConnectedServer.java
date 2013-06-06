package States;

import java.io.IOException;

import Communications.*;
import Messages.*;

public class ConnectedServer extends State {
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
		} else if (input.startsWith(":update")) {
			if(input.length()-8>128 || input.substring(7).trim().equals("")){
				System.out.println("\rInvalid username");
			}
			Message message=new ClientRequestUpdateMessage(6,input.substring(7).length(),0,"",input.substring(7).getBytes());
			tcp.send(message);
			return new ServerUpdate();
		} else if (input.startsWith(":query")) {
			Message message=new ClientRequestInfoMessage(6,input.substring(7).length(),0,"",input.substring(7).getBytes());
			tcp.send(message);
			return new ServerQuery();
		} else {
			return this;
		}
	}
}
