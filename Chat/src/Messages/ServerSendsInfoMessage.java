/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the message the server sends in response to a client
 * 	info request if the server has a binding for the given username
 */
package Messages;

public class ServerSendsInfoMessage extends Message {
	String targetUsername = null; //username asked for
	public String targetIP = null; //ip bound to that username

	public static final long minSize = 143;//size of the body of the message

	//this constructs a message when the body is a byte[]
	public ServerSendsInfoMessage(int _op, long _length, long _reserved,
			String _options, byte[] body) {
		//populate the header
		super(_op, _length, _reserved, _options);
		//populate the body
		processBody(body);
		if (op != 9) {
			correct = false;
		}
	}

	//this constructs a message when the body is parameterized
	public ServerSendsInfoMessage(int _op, long _length, long _reserved,
			String _options, String _targetUsername, String _targetIP) {
		//populate the header
		super(_op, _length, _reserved, _options);
		targetUsername = _targetUsername;
		targetIP = _targetIP;
		if (op != 9) {
			correct = false;
		}
	}

	//populate the body when given a byte[]
	private void processBody(byte[] body) {
		if (body.length != 143) {
			correct = false;
			return;
		}

		//copy over the target username
		byte[] targetUserArray = new byte[128];
		for (int i = 0; i < body.length && i < 128; i++) {
			targetUserArray[i] = body[i];
		}
		targetUsername = new String(targetUserArray, 0, targetUserArray.length);
		
		int offset = 128;
		//copy over the ip
		byte[] targetIPArray = new byte[15];
		for (int i = 0; i < body.length && i < 15; i++) {
			targetIPArray[i] = body[i + offset];
		}

		targetIP = new String(targetIPArray, 0, targetIPArray.length);
		
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
		//copy over the target username
		tmp = targetUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;

		//copy over the target ip
		tmp = targetIP.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 15;

		return storage;
	}
}
