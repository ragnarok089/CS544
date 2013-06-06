package Server;

import Communications.TCP;
import Messages.Message;

public class ServerDisconnected extends ServerState{

	public ServerState process(TCP tcp, Message tcpMessage, long timeEnteredState) {
		if(tcp.getActive()){
			return new ServerConnected();
		}
		else{
			return this;
		}
	}

}
