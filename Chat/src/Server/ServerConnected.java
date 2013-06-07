package Server;

import java.io.IOException;

import Communications.TCP;
import Messages.*;

public class ServerConnected extends ServerState{

	public ServerState process(TCP tcp, Message tcpMessage, long timeEnteredState) {
		if(tcpMessage instanceof ServerHandShakeMessage && tcpMessage.getCorrect()){
			Message message = new ServerAcceptMessage(4,Message.minSize,0,"",new byte[0]);
			tcp.send(message);
			return new ServerReady();
		}
		else if(tcpMessage!=null){
			tcp.send(new ErrorMessage(13,Message.minSize,0,"",new byte[0]));
			try {
				tcp.close();
			} catch (IOException e) {}
			return new ServerDisconnected();
		}
		else if(System.currentTimeMillis()-timeEnteredState>30000){
			try {
				tcp.close();
			} catch (IOException e) {}
			return new ServerDisconnected();
		}
		return this;
	}

}
