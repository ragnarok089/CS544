package Messages;

public class ClientRequestInfoMessage extends Message {
	String senderUsername = null;
	public String targetUsername = null;
	String senderIP = null;

	public static final long minSize = 271;

	public ClientRequestInfoMessage(int _op, long _length, long _reserved,
			String _options, int[] body) {
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

	private void processBody(int[] body) {
		if (body.length != 271) {
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
	
		senderIP = new String(senderIPArray, 0, senderIPArray.length);
		
		offset += 15;

		int[] targetUserArray = new int[128];
		for (int i = 0; i < body.length && i < 128; i++) {
			targetUserArray[i] = body[offset + i];
		}
		targetUsername = new String(targetUserArray, 0, targetUserArray.length);

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

		tmp = targetUsername.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			storage[total + i] = tmp[i];
		}

		total += 128;

		return storage;
	}
}
