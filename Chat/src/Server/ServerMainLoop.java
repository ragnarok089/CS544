package Server;

import java.io.IOException;

import Communications.*;
import Messages.Message;
import Utilities.InputReader;

public class ServerMainLoop {
	static TCP tcp=null;
	static boolean error=false;
	static boolean done=false;
	static ServerCurrentState state=new ServerCurrentState();
	static ServerState lastState=null;
	static long timeEnteredState=0;
	static InputReader ir = new InputReader();
	static Message tcpMessage=null;
	
	public static void main(String[] args){
		tcp=new TCP();
		Thread irThread=new Thread(ir);
		irThread.start();
		while(!done && !error){
			if(state.getState().getClass()!=lastState.getClass()){
				timeEnteredState=System.currentTimeMillis();
			}
			String input=ir.getSubmitted();
			if(input.startsWith(":quit")){
				done=true;
			}
			else if(input.startsWith(":dc")){
				try {
					tcp.close();
				} catch (IOException e) {}
				state.state=new ServerDisconnected();
			}
			else if(input.equals("")){
				tcpMessage=tcp.read();
			}
			state.process(tcp, tcpMessage, timeEnteredState);
			lastState = state.getState();
		}
	}
}
