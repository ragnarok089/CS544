package Server;

import java.io.IOException;

import Communications.TCP;
import Messages.*;

public class ServerConnected extends ServerState{

	public ServerState process(TCP tcp, Message tcpMessage, long timeEnteredState) {
		if(tcpMessage instanceof ClientRequestInfoMessage && tcpMessage.getCorrect()){
			Message message = null;
			String user=((ClientRequestInfoMessage)tcpMessage).targetUsername;
			String ip=LookupTable.lookup(user);
			if(ip==null){
				message=new LookupFailedMessage(12,LookupFailedMessage.minSize+Message.minSize,0,"",user);
			}
			else{
				message=new ServerSendsInfoMessage(9,ServerSendsInfoMessage.minSize+Message.minSize,0,"",user,ip);
			}
			tcp.send(message);
			return this;
		}
		else if(tcpMessage instanceof ClientRequestUpdateMessage && tcpMessage.getCorrect()){
			Message message = null;
			String user=((ClientRequestUpdateMessage)tcpMessage).senderUsername;
			String ip=((ClientRequestUpdateMessage)tcpMessage).ip;
			if(LookupTable.lookup(user)!=null){
				message=new NameCollisionMessage(14,NameCollisionMessage.minSize+Message.minSize,0,"",user);
			}
			else{
				LookupTable.bind(user, ip);
				message=new ServerConfirmationUpdateMessage(7,ServerConfirmationUpdateMessage.minSize+Message.minSize,0,"",user,ip);
			}
			tcp.send(message);
			return this;
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
