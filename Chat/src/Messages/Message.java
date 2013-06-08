/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the super class for all messages. It also provides the methods
 * 	used to deal with the header information, which is stored here as opposed to
 * 	in the unique parts of each message
 */
package Messages;

public class Message {
	int op = -1;//indicates what type of message this is
	long length = -1;//indicates the length of the message
	long reserved = 0;
	String options = "";
	boolean correct = true;//indicates whether the message is valid
	public final static int minSize = 132;//the length of the header

	public Message(int _op, long _length, long _reserved, String _options) {
		op = _op;
		length = _length;
		reserved = _reserved;
		options = _options;
	}

	public boolean getCorrect() {
		return correct;
	}

	//converts from a byte array to a number
	protected long fromByteArray(byte[] bytes) {
		long total = 0;
		//for each byte
		for (int i = 0; i < bytes.length; i++) {
			//take the byte and calculate its value by multiplying by the appropriate power of 2
			int store = (int) (Byte.valueOf(bytes[i]).intValue() * Math.pow(2,
					8 * i));
			//since byte wrap at 128, if it is negative subtract it from 127 to get the true value
			if (store < 0) {
				store = 127 - store;
			}
			total += store;
		}
		return total;
	}

	//converts the header to a byte array
	public byte[] convert() {
		byte[] storage = new byte[minSize];
		int total = 0;
		//add the op code
		byte[] temp = numToByte(op, 1);
		for (int i = 0; i < temp.length; i++) {
			storage[total + i] = temp[i];
		}
		//add the length
		total += temp.length;
		temp = numToByte((int) length, 2);
		for (int i = 0; i < temp.length; i++) {
			storage[total + i] = temp[i];
		}
		//add the reserved field
		total += temp.length;
		temp = numToByte((int) reserved, 1);
		for (int i = 0; i < temp.length; i++) {
			storage[total + i] = temp[i];
		}
		//add the options
		total += temp.length;
		temp = options.getBytes();
		for (int i = 0; i < temp.length; i++) {
			storage[total + i] = temp[i];
		}
		total += temp.length;
		return storage;
	}

	//returns a byte[] given a number and the number of bytes to make
	public byte[] numToByte(int num, int numBytes) {
		String numstr = Integer.toBinaryString(num);
		//add zeroes until there are a multiple of 8 digits
		while (numstr.length() % 8 != 0) {
			numstr = "0".concat(numstr);
		}
		byte[] storage = new byte[numBytes];
		//take the string in chunks of 8 and process them
		for (int i = 0; i < numBytes && i < numstr.length() / 8.0; i++) {
			//grab the starting from the rightmost
			String currentByte=numstr.substring(numstr.length() - 8 * (i + 1),numstr.length() - 8 * i);
			//since bytes cap at 127, if it starts with a 1 just replace it with a -, since it is
			//the same value in byte form
			if (currentByte.startsWith("1")) {
				currentByte = "-" + currentByte.substring(1);
			}
			//convert string to byte
			storage[i] = Byte.parseByte(currentByte, 2);
		}
		return storage;
	}
}
