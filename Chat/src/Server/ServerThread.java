/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is one connection between the server and a client
 * 	it continually acts as a state machine, polling the tcp connection
 * 	and passing the state the state.
 * 	It also automatically shuts down if the connection ever closes
 */
package Server;

import java.io.IOException;

import Messages.ErrorMessage;
import Messages.Message;
import Communications.TCP;

public class ServerThread implements Runnable{
	TCP tcp=null;//the connection
	
	//sets the connection for this thread
	public void setTCP(TCP in){
		tcp=in;
	}
	
	//the main loop
	public void run() {
		boolean done=false;//if the loop should stop
		boolean error=false;
		ServerCurrentState state=new ServerCurrentState();//the state of this connection
		state.setup();//initialize the state
		long timeEnteredState=0;//the time the state was entered
		ServerState lastState=null;//the last state
		Message tcpMessage=null;//the tcp message
		
		while(!done && !error){
			//update the time entered if the state has changed
			if(lastState==null||state.getState().getClass()!=lastState.getClass()){
				timeEnteredState=System.currentTimeMillis();
			}
			//if there was a disconnection
			//clean up and exit the thread
			if(!tcp.getActive()){
				try {
					tcp.close();
				} catch (Exception e) {}
				done=true;
				continue;
			}
			//read from tcp
			tcpMessage = tcp.read();
			//if its an error message disconnect and quit the thread
			if (tcpMessage instanceof ErrorMessage && tcpMessage.getCorrect()) {
				System.out.println("An error occured while communicating.\nDisconnecting");
				try {
					tcp.close();
				} catch (IOException e) {
				}
				state.state = new ServerDisconnected();
				done = true;
				continue;
			}
			//update the last state
			lastState = state.getState();
			//otherwise, give the data to the current state to get the next state
			state.process(tcp, tcpMessage, timeEnteredState);
		}
	}
}
