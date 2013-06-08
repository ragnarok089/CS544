/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the message the client sends to to server to
 * 	request that their name gets bound to their ip
 */
package Messages;

public class ClientRequestUpdateMessage extends Message {
	public String senderUsername = null;
	public String senderIP = null;

	public static final long minSize = 143;//size of the body of the message
	
	//creates a Message when the body is a byte[]
	public ClientRequestUpdateMessage(int _op, long _length, long _reserved,
			String _options, byte[] body) {
		//populate the header
		super(_op, _length, _reserved, _options);
		//populate the body
		processBody(body);
		if (op != 6) {
			correct = false;
		}
	}

	//create a message when the body attributes are given
	public ClientRequestUpdateMessage(int _op, long _length, long _reserved,
			String _options, String _senderUsername, String _senderIP) {
		//populate the header
		super(_op, _length, _reserved, _options);
		senderUsername = _senderUsername;
		senderIP = _senderIP;
		if (op != 6) {
			correct = false;
		}
	}

	//populates the body given a byte[]
	private void processBody(byte[] body) {
		if (body.length != 143) {
			correct = false;
			return;
		}
		//grab the first 128 bytes and make it a username by converting to string
		byte[] senderUserArray = new byte[128];
		for (int i = 0; i < senderUserArray.length; i++) {
			senderUserArray[i] = body[i];
		}
		senderUsername = new String(senderUserArray, 0, senderUserArray.length);
		
		int offset = 128;
		//grab the last 15 and make it an ip by converting to a string
		int[] senderIPArray = new int[15];
		for (int i = 0; i < senderIPArray.length && i < 128; i++) {
			senderIPArray[i] = body[i + offset];
		}
		
		senderIP = new String(senderIPArray, 0, senderIPArray.length);
		

	}

	//converts the message into a byte[]
	public byte[] convert() {
		//convert the header
		byte[] upper = super.convert();
		byte[] storage = new byte[(int) (upper.length + minSize)];
		//copy over the header
		for (int i = 0; i < upper.length; i++) {
			storage[i] = upper[i];
		}

		int total = upper.length;

		byte[] tmp = null;
		//copy the sender Username
		tmp = senderUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;
		//copy the sender ip
		tmp = senderIP.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}
		
		total += 15;

		return storage;
	}
}
