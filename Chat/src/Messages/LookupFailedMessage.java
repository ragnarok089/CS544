/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the message the server returns is the client
 * 	tried to do a lookup on a username that was not bound
 */
package Messages;

public class LookupFailedMessage extends Message {
	String targetUsername = null;

	public static final long minSize = 128;//size of the body

	//constructs the message when the body is a byte[]
	public LookupFailedMessage(int _op, long _length, long _reserved,
			String _options, byte[] body) {
		//populate the header
		super(_op, _length, _reserved, _options);
		//populate the body
		processBody(body);
		if (op != 12) {
			correct = false;
		}
	}

	//constructs the message when the body is in argument form
	public LookupFailedMessage(int _op, long _length, long _reserved,
			String _options, String _targetUsername) {
		//populate the header
		super(_op, _length, _reserved, _options);
		targetUsername = _targetUsername;
		if (op != 12) {
			correct = false;
		}
	}
	
	//this method populates the body given a byte []
	private void processBody(byte[] body) {
		if (body.length != 128) {
			correct = false;
			return;
		}
		
		//make the first 128 bytes the targetUsername when converted to string
		byte[] targetUserArray = new byte[128];
		for (int i = 0; i < body.length; i++) {
			targetUserArray[i] = body[i];
		}
		targetUsername = new String(targetUserArray, 0, targetUserArray.length);
		

	}

	//this converts the message into byte array
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
		//copy the targetUsername
		tmp = targetUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;

		return storage;
	}
}
