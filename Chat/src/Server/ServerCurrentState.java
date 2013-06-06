package Server;

import Communications.TCP;
import Messages.Message;

public class ServerCurrentState {
	public ServerState state;
	public void setup(){
		state= new ServerDisconnected();
	}
	public void process(TCP tcp,Message tcpMessage,long timeEnteredState){
		state=state.process(tcp, tcpMessage, timeEnteredState);
	}
	
	public ServerState getState(){
		return state;
	}
}
