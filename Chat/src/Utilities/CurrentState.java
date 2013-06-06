package Utilities;
import Communications.*;
import Messages.*;
import States.*;




public class CurrentState {
	public State state;
	public void setup(){
		state= new Disconnected();
	}
	public void process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState){
		state=state.process(input, tcp, us, udpMessage, tcpMessage, timeEnteredState);
	}
	
	public State getState(){
		return state;
	}
}
