/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the super class for all states for the client. The
 * 	method that they all must implement, takes a certain list of inputs
 * 	and returns the next state the client should be placed into
 */
package States;
import Communications.*;
import Messages.*;

public abstract class State {
	public abstract State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState,boolean firstCall);

}
