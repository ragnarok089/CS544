package States;

import Communications.*;
import Messages.*;


public class Waiting extends State{
	public static final long timeout=1000;
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState){
		
		if(tcp.getActive()==true){
			System.out.println("Connection established");
			return new Connected();
		}
		else if(System.currentTimeMillis()-timeEnteredState>timeout){
			System.out.println("Got no response");
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
