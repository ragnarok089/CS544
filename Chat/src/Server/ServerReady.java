/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is ther server state that occurs once a connection
 * 	has been established and the handshake has been completed
 * 	from here the client can either update their entry
 * 	or query the server
 */
package Server;

import java.io.IOException;

import Communications.TCP;
import Messages.ClientRequestInfoMessage;
import Messages.ClientRequestUpdateMessage;
import Messages.ErrorMessage;
import Messages.LookupFailedMessage;
import Messages.Message;
import Messages.NameCollisionMessage;
import Messages.ServerConfirmationUpdateMessage;
import Messages.ServerSendsInfoMessage;
//STATEFUL
public class ServerReady extends ServerState{
	//returns the next state the thread should be in
	public ServerState process(TCP tcp, Message tcpMessage, long timeEnteredState) {
		//if it is a correct request for information
		if(tcpMessage instanceof ClientRequestInfoMessage && tcpMessage.getCorrect()){
			Message message = null;
			//get the username
			String user=((ClientRequestInfoMessage)tcpMessage).targetUsername;
			//lookup the user
			String ip=LookupTable.lookup(user);
			//if there is no binding
			if(ip==null){
				//send a lookupFailedMessage
				message=new LookupFailedMessage(12,LookupFailedMessage.minSize+Message.minSize,0,"",user);
			}
			else{
				//send the information back
				message=new ServerSendsInfoMessage(9,ServerSendsInfoMessage.minSize+Message.minSize,0,"",user,ip);
			}
			tcp.send(message);
			return this;
		}
		//otherwise if its a correct request for an update
		else if(tcpMessage instanceof ClientRequestUpdateMessage && tcpMessage.getCorrect()){
			Message message = null;
			//get the user and ip
			String user=((ClientRequestUpdateMessage)tcpMessage).senderUsername;
			String ip=((ClientRequestUpdateMessage)tcpMessage).senderIP;
			//lookup the upser.
			//if there is a binding
			if(LookupTable.lookup(user)!=null){
				//send a name collision message
				message=new NameCollisionMessage(14,NameCollisionMessage.minSize+Message.minSize,0,"",user);
			}
			else{
				//bind the username ip pair
				LookupTable.bind(user, ip);
				//send a new server confirmation update message
				message=new ServerConfirmationUpdateMessage(7,ServerConfirmationUpdateMessage.minSize+Message.minSize,0,"",user,ip);
			}
			tcp.send(message);
			return this;
		}
		//if its any other type of message
		else if(tcpMessage!=null){
			//respond with an error message
			tcp.send(new ErrorMessage(13,Message.minSize,0,"",new byte[0]));
			try {
				//close the connection
				tcp.close();
			} catch (IOException e) {}
			//go to disconnected
			return new ServerDisconnected();
		}
		//after 300 seconds disconnect
		else if(System.currentTimeMillis()-timeEnteredState>300000){
			try {
				tcp.close();
			} catch (IOException e) {}
			return new ServerDisconnected();
		}
		return this;
	}
}
