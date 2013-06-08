/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This message is sent by the server when it successfully binds
 * 	a name to an ip address at the request of a client
 */
package Messages;

public class ServerConfirmationUpdateMessage extends Message {
	String confirmedUsername = null;
	String senderIP = null;

	public static final long minSize = 143;//size of the body

	//constructs the message given the body in byte[]
	public ServerConfirmationUpdateMessage(int _op, long _length,
			long _reserved, String _options, byte[] body) {
		//populate the header
		super(_op, _length, _reserved, _options);
		//populate the body
		processBody(body);
		if (op != 7) {
			correct = false;
		}
	}

	//constructs the message given the body in parameter form
	public ServerConfirmationUpdateMessage(int _op, long _length,
			long _reserved, String _options, String _confirmedUsername,
			String _senderIP) {
		//populate the header
		super(_op, _length, _reserved, _options);
		confirmedUsername = _confirmedUsername;
		senderIP = _senderIP;
		if (op != 7) {
			correct = false;
		}
	}

	//populates the body from a byte[]
	private void processBody(byte[] body) {
		if (body.length != 143) {
			correct = false;
			return;
		}
		//taket he first 128 bytes and make it the username
		byte[] confirmedUserArray = new byte[128];
		for (int i = 0; i < confirmedUserArray.length; i++) {
			confirmedUserArray[i] = body[i];
		}
		confirmedUsername = new String(confirmedUserArray, 0,
				confirmedUserArray.length);
		
		int offset = 128;
		//take the next 15 and make ith the ip
		byte[] senderIPArray = new byte[15];
		for (int i = 0; i < senderIPArray.length && i < 15; i++) {
			senderIPArray[i] = body[i + offset];
		}

		senderIP = new String(senderIPArray, 0, senderIPArray.length);
		
	}

	//converts the message into a bytes[]
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
		//copy the username in
		tmp = confirmedUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;
		//copy the ip in
		tmp = senderIP.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}
		
		total += 15;
		
		return storage;
	}
}
