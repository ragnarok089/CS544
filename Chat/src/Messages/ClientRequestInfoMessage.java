package Messages;

public class ClientRequestInfoMessage extends Message {
	String senderUsername = null;
	public String targetUsername = null;
	String senderIP = null;

	public static final long minSize = 260;

	public ClientRequestInfoMessage(int _op, long _length, long _reserved,
			String _options, byte[] body) {
		super(_op, _length, _reserved, _options);
		processBody(body);
		if (op != 8) {
			correct = false;
		}
	}

	public ClientRequestInfoMessage(int _op, long _length, long _reserved,
			String _options, String _senderUsername, String _targetUsername,
			String _senderIP) {
		super(_op, _length, _reserved, _options);
		senderUsername = _senderUsername;
		targetUsername = _targetUsername;
		senderIP = _senderIP;
		if (op != 8) {
			correct = false;
		}
	}

	private void processBody(byte[] body) {
		if (body.length != 260) {
			correct = false;
			return;
		}
		byte[] senderUserArray = new byte[128];
		for (byte i = 0; i < body.length && i < 128; i++) {
			senderUserArray[i] = body[i];
		}
		senderUsername = new String(senderUserArray, 0, senderUserArray.length);

		byte[] senderIPArray = new byte[] { body[128], body[129], body[130],
				body[131] };
		senderIP = new String(senderIPArray, 0, senderIPArray.length);

		byte[] targetUserArray = new byte[128];
		for (byte i = 0; i < body.length && i > 132; i++) {
			targetUserArray[i] = body[i];
		}
		targetUsername = new String(targetUserArray, 0, targetUserArray.length);

	}

	public byte[] convert() {
		byte[] upper = super.convert();
		byte[] storage = new byte[(int) (upper.length + minSize)];
		for (int i = 0; i < upper.length; i++) {
			storage[i] = upper[i];
		}

		int total = upper.length - 1;

		byte[] tmp = null;

		tmp = senderUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += tmp.length;

		tmp = senderIP.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += tmp.length;

		tmp = targetUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += tmp.length;

		return storage;
	}
}
