package Messages;

public class ClientHandShakeMessage extends Message {
	public String senderUsername = null;
	String senderIP = null;

	public static final long minSize = 143;

	public ClientHandShakeMessage(int _op, long _length, long _reserved,
			String _options, int[] body) {
		super(_op, _length, _reserved, _options);
		processBody(body);
		if (op != 3) {
			correct = false;
		}
	}

	public ClientHandShakeMessage(int _op, long _length, long _reserved,
			String _options, String _senderUsername, String _senderIP) {
		super(_op, _length, _reserved, _options);
		senderUsername = _senderUsername;
		senderIP = _senderIP;
		if (op != 3) {
			correct = false;
		}
	}

	private void processBody(int[] body) {
		
		if (body.length != 143) {
			correct = false;
			return;
		}

		int[] senderUserArray = new int[128];
		for (int i = 0; i < body.length && i < 128; i++) {
			senderUserArray[i] = body[i];
		}
		senderUsername = new String(senderUserArray, 0, senderUserArray.length);
		
		int offset = 128;

		int[] senderIPArray = new int[15];
		for (int i = 0; i < body.length && i < 15; i++) {
			senderIPArray[i] = body[i + offset];
		}
		
		senderIP = new String(senderUserArray, 0, senderUserArray.length);

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
