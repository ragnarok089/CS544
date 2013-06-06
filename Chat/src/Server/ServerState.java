package Server;

import Communications.TCP;
import Messages.Message;

public abstract class ServerState {
	public abstract ServerState process(TCP tcp, Message tcpMessage,long timeEnteredState);

}
