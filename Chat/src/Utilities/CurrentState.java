/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	this stores the current state of the client.
 */
package Utilities;
import Communications.*;
import Messages.*;
import States.*;



//STATEFUL
public class CurrentState {
	public State state;//the state of the client
	//initializes the state to disconnected
	public void setup(){
		state= new Disconnected();
	}
	//on process take the data and call process with it on the current state to get the new state
	public void process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState,boolean firstCall){
		state=state.process(input, tcp, us, udpMessage, tcpMessage, timeEnteredState,firstCall);
	}
	
	public State getState(){
		return state;
	}
}
