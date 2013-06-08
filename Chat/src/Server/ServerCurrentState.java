/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is a wrapper that stores the current state one thread of the server
 * 	is in
 */
package Server;

import Communications.TCP;
import Messages.Message;

public class ServerCurrentState {
	public ServerState state;//the state the server thread is in
	
	//initializes the state to disconnected
	public void setup(){
		state= new ServerDisconnected();
	}
	//on process calls process on the state and sets the state to the result
	public void process(TCP tcp,Message tcpMessage,long timeEnteredState){
		state=state.process(tcp, tcpMessage, timeEnteredState);
	}
	
	public ServerState getState(){
		return state;
	}
}
