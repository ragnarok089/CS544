package States;

import Communications.TCP;
import Communications.UDPSender;
import Messages.Message;

public class UserConfirmCallback extends State{

	public State process(String input, TCP tcp, UDPSender us, Message udpMessage, Message tcpMessage, long timeEnteredState) {
		if(tcp.getActive()){
			return new Connected();
		}
		else if(input.startsWith(":y")){
			tcp.connect(tcp.pendingIP);
			return this;
		}
		else if(input.startsWith(":n")){
			return new Disconnected();
		}
		else if(input.startsWith(":")){
			System.out.println("\rInvalid command");
			return this;
		}
		else if(!input.equals("")){
			System.out.println("\rYou cannot chat in this state");
			return this;
		}
		return this;
	}

}
