package States;

import java.io.IOException;

import Communications.*;
import Messages.*;

public class ConnectedServer extends State {
	public State process(String input, TCP tcp, UDPSender us, Message udpMessage, Message tcpMessage, long timeEnteredState) {
		if (tcp.getActive() == false) {
			System.out.println("Server disconnected");
			return new Disconnected();
			
		} 
		else if (input.startsWith(":update")) {
			if(input.length()-8>128 || input.substring(7).trim().equals("")){
				System.out.println("Invalid username");
			}
			Message message=new ClientRequestUpdateMessage(6,input.substring(7).length(),0,"",input.substring(7)); error;
			tcp.send(message);
			return new ServerUpdate();
		} 
		else if (input.startsWith(":query")) {
			Message message=new ClientRequestInfoMessage(6,input.substring(7).length(),0,"",input.substring(7));
			tcp.send(message);
			return new ServerQuery();
		} 
		else if(input.startsWith(":")){
			System.out.println("Invalid command");
			return this;
		}
		else if(!input.equals("")){
			System.out.println("You cannot chat with a server");
			return this;
		}
		else if(tcpMessage!=null){
			System.out.println("The server sent an unsolicited message.\nDisconnecting.");
			try{
				tcp.close();
			}
			catch(Exception e){}
			return new Disconnected();
		}
		else {
			return this;
		}
	}
}
