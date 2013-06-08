/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the message that is sent when the person connected to
 * 	does not with to maintain the connection
 */
package Messages;


public class DeclineConnectMessage extends Message {
	//since there is no body to the message it just uses the header methods
	public DeclineConnectMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		super(_op,_length,_reserved,_options);
		if(op!=10){
			correct=false;
		}
	}
}
