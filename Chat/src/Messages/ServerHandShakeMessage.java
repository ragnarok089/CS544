package Messages;

public class ServerHandShakeMessage extends Message {
	String senderUsername = null;
	String senderIP = null;

	public static final long minSize = 143;

	public ServerHandShakeMessage(int _op, long _length, long _reserved,
			String _options, byte[] body) {
		super(_op, _length, _reserved, _options);
		processBody(body);
		if (op != 2) {
			correct = false;
		}
	}

	public ServerHandShakeMessage(int _op, long _length, long _reserved,
			String _options, String _senderUsername, String _senderIP) {
		super(_op, _length, _reserved, _options);
		senderUsername = _senderUsername;
		senderIP = _senderIP;
		if (op != 2) {
			correct = false;
		}
	}

	private void processBody(byte[] body) {
		if (body.length != 143) {
			correct = false;
			return;
		}

		byte[] senderUserArray = new byte[128];
		for (int i = 0; i < body.length && i < 128; i++) {
			senderUserArray[i] = body[i];
		}
		senderUsername = new String(senderUserArray, 0, senderUserArray.length);

		int offset = 128;
		
		byte[] senderIPArray = new byte[15];
		for (int i = 0; i < body.length && i < 15; i++) {
			senderIPArray[i] = body[i + offset];
		}

		senderIP = new String(senderIPArray, 0, senderIPArray.length);
		

	}

	public byte[] convert() {
		byte[] upper = super.convert();
		byte[] storage = new byte[(int) (upper.length + minSize)];
		for (int i = 0; i < upper.length; i++) {
			storage[i] = upper[i];
		}

		int total = upper.length;

		byte[] tmp = null;

		tmp = senderUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;

		tmp = senderIP.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 15;

		return storage;
	}
}
