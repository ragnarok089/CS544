/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the message the client sends to the server to open
 * 	communications between them
 */
package Messages;

public class ServerHandShakeMessage extends Message {
	String senderUsername = null;
	String senderIP = null;

	public static final long minSize = 143;//size of the body

	//constructs a message when the body is a byte[]
	public ServerHandShakeMessage(int _op, long _length, long _reserved,
			String _options, byte[] body) {
		//populate the header
		super(_op, _length, _reserved, _options);
		//populate the body
		processBody(body);
		if (op != 2) {
			correct = false;
		}
	}

	//constructs a message when the body is parameterized
	public ServerHandShakeMessage(int _op, long _length, long _reserved,
			String _options, String _senderUsername, String _senderIP) {
		//populate the header
		super(_op, _length, _reserved, _options);
		senderUsername = _senderUsername;
		senderIP = _senderIP;
		if (op != 2) {
			correct = false;
		}
	}

	//populates the body based on the byte[]
	private void processBody(byte[] body) {
		if (body.length != 143) {
			correct = false;
			return;
		}

		//converts the senderUserArray to the first 128 bytes
		byte[] senderUserArray = new byte[128];
		for (int i = 0; i < body.length && i < 128; i++) {
			senderUserArray[i] = body[i];
		}
		senderUsername = new String(senderUserArray, 0, senderUserArray.length);

		int offset = 128;
		//converts the ip to the next 15 bytes
		byte[] senderIPArray = new byte[15];
		for (int i = 0; i < body.length && i < 15; i++) {
			senderIPArray[i] = body[i + offset];
		}

		senderIP = new String(senderIPArray, 0, senderIPArray.length);
		

	}

	//converts the messages into a byte[]
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
		//copy in the username
		tmp = senderUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;
		//copy in the ip
		tmp = senderIP.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 15;

		return storage;
	}
}
