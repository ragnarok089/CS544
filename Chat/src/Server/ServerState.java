/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the super class for all server states. All must implement
 * 	the process thread that take in the tcp object, a tcp message,
 * 	and the time this state was entered. It should return the new state
 * 	of the server thread
 */
package Server;

import Communications.TCP;
import Messages.Message;
//STATEFUL
public abstract class ServerState {
	public abstract ServerState process(TCP tcp, Message tcpMessage,long timeEnteredState);

}
