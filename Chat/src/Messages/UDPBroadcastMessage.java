/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the message sent over UDP when the program
 * 	searches locally for someone by the given username to
 * 	connect to
 */
package Messages;

public class UDPBroadcastMessage extends Message {
	public String senderUsername = null;//username of the looker
	public String targetUsername = null;//username of the person being looked for
	public String senderIP = null;//callback address

	public static final long minSize = 271;// size of the body

	//constructs a message given the body in byte[] form
	public UDPBroadcastMessage(int _op, long _length, long _reserved,
			String _options, byte[] body) {
		//populate the header
		super(_op, _length, _reserved, _options);
		//populate the body
		processBody(body);
		if (op != 1) {
			correct = false;
		}
	}

	//constructs a message given the body in parameter form
	public UDPBroadcastMessage(int _op, long _length, long _reserved,
			String _options, String _senderUsername, String _targetUsername,
			String _senderIP) {
		//convert the header
		super(_op, _length, _reserved, _options);
		senderUsername = _senderUsername;
		targetUsername = _targetUsername;
		senderIP = _senderIP;
		if (op != 1) {
			correct = false;
		}
	}

	//populate the body given a byte[]
	private void processBody(byte[] body) {
		if (body.length != minSize) {
			correct = false;
			return;
		}
		//copy the username from the first 128 bytes
		byte[] senderUserArray = new byte[128];
		for (int i = 0; i < body.length && i < 128; i++) {
			senderUserArray[i] = body[i];
		}
		senderUsername = new String(senderUserArray, 0, senderUserArray.length);

		int offset = 128;
		byte[] ipArray = new byte[15];
		//copy the ip from the next 15 bytes
		for (int i = 0; i < body.length && i < 15; i++) {
			ipArray[i] = body[i + offset];
		}
		senderIP = new String(ipArray, 0, ipArray.length);

		offset += 15;
		//copy the target user from the last 128 bytes
		byte[] targetUserArray = new byte[128];
		for (int i = 0; i < body.length && i < 128; i++) {
			targetUserArray[i] = body[offset + i];
		}
		targetUsername = new String(targetUserArray, 0, targetUserArray.length);

	}

	//converts the message to a byte array
	public byte[] convert() {
		//convert the header
		byte[] upper = super.convert();
		byte[] storage = new byte[(int) (upper.length + minSize)];
		//copy the header
		for (int i = 0; i < upper.length; i++) {
			storage[i] = upper[i];
		}

		int total = upper.length;

		byte[] tmp = null;
		//copy the sender username
		tmp = senderUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;
		//copy the ip
		tmp = senderIP.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 15;
		//copy the target username
		tmp = targetUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;

		return storage;
	}
}
