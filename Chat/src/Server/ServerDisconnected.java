/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the disconnected state for the server. Nothing happens
 * 	unless a conection is made, in which case it goes to server connected
 * 	In reality only 1 iteration in this state should occur and otherwise
 * 	it will automatically error out from elsewhere 
 */
package Server;

import Communications.TCP;
import Messages.Message;

public class ServerDisconnected extends ServerState{

	//on any input return the next state of this thread
	public ServerState process(TCP tcp, Message tcpMessage, long timeEnteredState) {
		//if a connection has been made return server connected
		if(tcp.getActive()){
			return new ServerConnected();
		}
		else{
			return this;
		}
	}

}
