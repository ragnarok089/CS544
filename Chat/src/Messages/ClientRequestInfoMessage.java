/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the message the client sends to a server to
 * 	request the ip address of a certain user
 */
package Messages;

public class ClientRequestInfoMessage extends Message {
	String senderUsername = null;
	public String targetUsername = null;//the username of the person to lookup
	String senderIP = null;

	public static final long minSize = 271;//the size of the body

	//constructs the message from a byte[] body
	public ClientRequestInfoMessage(int _op, long _length, long _reserved,
			String _options, byte[] body) {
		//populate the header
		super(_op, _length, _reserved, _options);
		//populate the body
		processBody(body);
		if (op != 8) {
			correct = false;
		}
	}

	//construct a message from the atributes
	public ClientRequestInfoMessage(int _op, long _length, long _reserved,
			String _options, String _senderUsername, String _targetUsername,
			String _senderIP) {
		//construct the header
		super(_op, _length, _reserved, _options);
		senderUsername = _senderUsername;
		targetUsername = _targetUsername;
		senderIP = _senderIP;
		if (op != 8) {
			correct = false;
		}
	}

	//populates the body when given a byte[]
	private void processBody(byte[] body) {
		if (body.length != 271) {
			correct = false;
			return;
		}
		//copy over the senderuser name and convert to string
		byte[] senderUserArray = new byte[128];
		for (int i = 0; i < body.length && i < 128; i++) {
			senderUserArray[i] = body[i];
		}
		senderUsername = new String(senderUserArray, 0, senderUserArray.length);

		int offset = 128;
		
		//copy over the senderip and convert to string
		byte[] senderIPArray = new byte[15];
		for (int i = 0; i < body.length && i < 15; i++) {
			senderIPArray[i] = body[i + offset];
		}
	
		senderIP = new String(senderIPArray, 0, senderIPArray.length);
		
		offset += 15;
		//copy over the target username and convert to string
		byte[] targetUserArray = new byte[128];
		for (int i = 0; i < body.length && i < 128; i++) {
			targetUserArray[i] = body[offset + i];
		}
		targetUsername = new String(targetUserArray, 0, targetUserArray.length);

	}

	//populates the body from a byte[]
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
		//copy the sender's username
		tmp = senderUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;
		//copy the sender's ip
		tmp = senderIP.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 15;
		//copy the target's username
		tmp = targetUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;

		return storage;
	}
}
