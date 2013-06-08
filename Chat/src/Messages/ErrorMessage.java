/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is a message sent to indicate that a generic error has occured
 */
package Messages;


public class ErrorMessage extends Message {
	//since there is not body all of the methods just come from the header
	public ErrorMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		super(_op,_length,_reserved,_options);
		if(op!=13){
			correct=false;
		}
	}
	public ErrorMessage(){
		super(13,Message.minSize,0,"");

	}

}
