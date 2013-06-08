/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This class takes in bytes and returns messages
 * 	since you cannot know how many bytes are required until
 * 	you process the header it is done in two steps and the parser
 * 	stores the state
 */
package Utilities;
import Messages.*;


public class Parser {
	int op;//the op code
	long length;//the length
	long reserved;//te reserved field
	String options;//the options field
	boolean needsMore=false;//whether the header has already benn read
	
	//returns the number of body bytes it needs and populates the header internally
	public int parse(byte[] message){
		if(needsMore){
			return -2;
		}
		//signal that the header has been processed
		needsMore=true;
		//pull out the header bytes
		op=(int)fromByteArray(new byte[]{message[0]});
		length=fromByteArray(new byte[]{message[1],message[2]});
		reserved=fromByteArray(new byte[]{message[3]});
		byte[] optArray=new byte[message.length-4];
		options=new String(optArray,0,optArray.length);
		//return the number of bytes needed based on op code
		switch(op){
			case 1: //udp broadcast
				return (int) UDPBroadcastMessage.minSize;
			case 2:// server handshake start
				return (int) ServerHandShakeMessage.minSize;
			case 3:// client handshake start
				return (int) ClientHandShakeMessage.minSize;
			case 4://server accept
				return 0;
			case 5://client accept
				return 0;
			case 6://client request update
				return (int) ClientRequestUpdateMessage.minSize;
			case 7://server confirmation of update
				return (int) ServerConfirmationUpdateMessage.minSize;
			case 8://client request info from server
				return (int) ClientRequestInfoMessage.minSize;
			case 9://server sends requested info
				return (int) ServerSendsInfoMessage.minSize;
			case 10://Decline to connect Message
				return 0;
			case 11: //chat message
				return (int) ChatMsgMessage.minSize;
			case 12: //lookup failed
				return (int) LookupFailedMessage.minSize;
			case 13://Error
				return 0;
			case 14://name collision
				return (int) NameCollisionMessage.minSize;
			default:
				System.out.println("Error op code was "+Integer.toString(op));
				return -1;
		}
	}
	
	//this method adds the body to the message and returns the result
	public Message addBody(byte[] body){
		//reset the state
		needsMore=false;
		//returns the new message based on op code
		switch(op){
		case 1: //udp broadcast
			return new UDPBroadcastMessage(op,length,reserved,options,body);
		case 2:// server handshake start
			return new ServerHandShakeMessage(op,length,reserved,options,body);
		case 3:// client handshake start
			return new ClientHandShakeMessage(op,length,reserved,options,body);
		case 4://server accept
			return new ServerAcceptMessage(op,length,reserved,options,body);
		case 5://client accept
			return new ClientAcceptMessage(op,length,reserved,options,body);
		case 6://client request update
			return new ClientRequestUpdateMessage(op,length,reserved,options,body);
		case 7://server confirmation of update
			return new ServerConfirmationUpdateMessage(op,length,reserved,options,body);
		case 8://client request info from server
			return new ClientRequestInfoMessage(op,length,reserved,options,body);
		case 9://server sends requested info
			return new ServerSendsInfoMessage(op,length,reserved,options,body);
		case 10://Decline to connect Message
			return new DeclineConnectMessage(op,length,reserved,options,body);
		case 11: //chat message
			return new ChatMsgMessage(op,length,reserved,options,body);
		case 12: //lookup failed
			return new LookupFailedMessage(op,length,reserved,options,body);
		case 13://Error
			return new ErrorMessage(op,length,reserved,options,body);
		case 14://name collision
			return new NameCollisionMessage(op,length,reserved,options,body);
		default:
			return null;
		}
	}
	//this converts from a byte array to a long
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
}
