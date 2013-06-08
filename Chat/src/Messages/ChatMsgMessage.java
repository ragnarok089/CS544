/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	this is a message that carries an actual chat message.
 */
package Messages;


public class ChatMsgMessage extends Message {
	public String messages=null;//the message
	public static final long minSize=1024;//the size of the body of this message
	
	//constructs the message from the byte[]
	public ChatMsgMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		//construct the header
		super(_op,_length,_reserved,_options);
		//add the body
		processBody(body);
		if(op!=11){
			correct=false;
		}
	}
	
	//constructs the message from data
	public ChatMsgMessage(int _op,long _length,long _reserved,String _options,String _messages){
		//construct the header
		super(_op,_length,_reserved,_options);
		//set the variables
		messages=_messages;
		if(op!=11){
			correct=false;
		}
	}
	
	//populates the body fields based on the byte array
	private void processBody(byte[] body) {

		if (body.length != 1024) {
			correct = false;
			return;
		}
		// read the first 1024 bytes as the message
		byte[] messageArray = new byte[1024];
		for (int i = 0; i < body.length && i < 1024; i++) {
			messageArray[i] = body[i];
		}
		//set the value
		messages = new String(messageArray, 0, messageArray.length);

	}
	//converts the Message into a byte array
	public byte[] convert() {
		//convert the header
		byte[] upper = super.convert();
		byte[] storage = new byte[(int) (upper.length + minSize)];
		//copy in the header
		for (int i = 0; i < upper.length; i++) {
			storage[i] = upper[i];
		}

		int total = upper.length;

		byte[] tmp = null;

		//copy in the message bytes
		tmp = messages.getBytes();
		for (int i = 0; i < tmp.length && i < 1024; i++) {
			storage[total + i] = tmp[i];
		}

		total += 1024;

		return storage;
	}
}
