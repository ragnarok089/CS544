/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the message a client sends to another client to start
 * 	communications
 */
package Messages;

public class ClientHandShakeMessage extends Message {
	public String senderUsername = null; //the username of the sender
	public String senderIP = null;//the ip of the sender

	public static final long minSize = 143;//the size of the body of the message

	//populates message from a byte array of the body
	public ClientHandShakeMessage(int _op, long _length, long _reserved,
			String _options, byte[] body) {
		//construct the header
		super(_op, _length, _reserved, _options);
		//add the body
		processBody(body);
		if (op != 3) {
			correct = false;
		}
	}

	//populates the message from fields
	public ClientHandShakeMessage(int _op, long _length, long _reserved,
			String _options, String _senderUsername, String _senderIP) {
		//populate the header
		super(_op, _length, _reserved, _options);
		senderUsername = _senderUsername;
		senderIP = _senderIP;
		if (op != 3) {
			correct = false;
		}
	}

	//this populates the message given the body in bytes
	private void processBody(byte[] body) {
		
		if (body.length != 143) {
			correct = false;
			return;
		}

		byte[] senderUserArray = new byte[128];
		//copy the senders username and convert to string
		for (int i = 0; i < body.length && i < 128; i++) {
			senderUserArray[i] = body[i];
		}
		senderUsername = new String(senderUserArray, 0, senderUserArray.length);
		
		int offset = 128;
		//copy the senders IP and convert to string
		byte[] senderIPArray = new byte[15];
		for (int i = 0; i < body.length && i < 15; i++) {
			senderIPArray[i] = body[i + offset];
		}
		
		senderIP = new String(senderIPArray, 0, senderIPArray.length);

	}

	//this converts the message into a byte array
	public byte[] convert() {
		//convert the header
		byte[] upper = super.convert();
		byte[] storage = new byte[(int) (upper.length + minSize)];
		//copy the header over
		for (int i = 0; i < upper.length; i++) {
			storage[i] = upper[i];
		}

		int total = upper.length;

		byte[] tmp = null;
		//copy the username over
		tmp = senderUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;
		//copy the ip over
		tmp = senderIP.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 15;

		return storage;
	}
}
