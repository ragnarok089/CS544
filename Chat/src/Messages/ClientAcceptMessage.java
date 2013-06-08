/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the message that a client sends to accept a connection
 */
package Messages;


public class ClientAcceptMessage extends Message {
	//since there is no body to the message it just constructs the header
	public ClientAcceptMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		super(_op,_length,_reserved,_options);
		if(op!=5){
			correct=false;
		}
	}

}
