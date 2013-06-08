/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the message sent when the server accepts the connection
 * 	with the client
 */
package Messages;


public class ServerAcceptMessage extends Message {

	//since there is no body to this message all the calls are just from the header
	public ServerAcceptMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		super(_op,_length,_reserved,_options);

		if(op!=4){
			correct=false;
		}
	}

}
