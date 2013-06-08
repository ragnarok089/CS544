/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the message that is sent when the client tries
 * 	to update a binding in the server but the binding already
 * 	exists
 */
package Messages;

public class NameCollisionMessage extends Message {
	String targetUsername = null;//the user name that was trying to be bound

	public static final long minSize = 128;//the size of the body

	//construct a message when the body is in byte form
	public NameCollisionMessage(int _op, long _length, long _reserved,
			String _options, byte[] body) {
		//populate the header
		super(_op, _length, _reserved, _options);
		//populate the body
		processBody(body);
		if (op != 14) {
			correct = false;
		}
	}
	//construct a message when the body is in parameter form
	public NameCollisionMessage(int _op, long _length, long _reserved,
			String _options, String _targetUsername) {
		//populate the header
		super(_op, _length, _reserved, _options);
		targetUsername = _targetUsername;
		if (op != 14) {
			correct = false;
		}
	}

	//this populates the body given a byte[]
	private void processBody(byte[] body) {
		if (body.length != 128) {
			correct = false;
			return;
		}
		//convert the byte array to a string
		byte[] targetUserArray = new byte[128];
		for (int i = 0; i < body.length; i++) {
			targetUserArray[i] = body[i];
		}
		targetUsername = new String(targetUserArray, 0, targetUserArray.length);
		
	}

	//this converts the message to a byte array
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
		//copy the body
		tmp = targetUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;

		return storage;
	}
}
