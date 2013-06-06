package Utilities;
import Messages.*;


public class Parser {
	int op;
	long length;
	long reserved;
	String options;
	boolean needsMore=false;
	public int parse(byte[] message){//returns the number of body bytes it needs
		if(needsMore){
			return -2;
		}
		needsMore=true;
		op=(int)fromByteArray(new byte[]{message[0]});
		length=fromByteArray(new byte[]{message[1],message[2]});
		reserved=fromByteArray(new byte[]{message[3]});
		byte[] optArray=new byte[message.length-4];
		options=new String(optArray,0,optArray.length);
		switch(op){
		redo
			case 1: //udp broadcast
				return 12;
			case 2:// server handshake start
				return 12;
			case 3:// client handshake start
				return 12;
			case 4://server accept
				return 0;
			case 5://client accept
				return 4;
			case 6://client request update
				return 4;
			case 7://server confirmation of update
				return 4;
			case 8://client request info from server
				return 12;
			case 9://server sends requested info
				return 8;
			case 10://Decline to connect Message
				return 0;
			case 11: //chat message
				if(length-132<0){
					return -1;
				}
				else{
					return (int) (length-132);
				}
			case 12: //lookup failed
				return 4;
			case 13://Error
				return 0;
			case 14://name collision
				return 4;
			default:
				return -1;
		}
	}
	public Message addBody(byte[] body){
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
	protected long fromByteArray(byte[] bytes) {
		long total=0;
	    for(int i=0;i<bytes.length;i++){
	    	total+=(long)i*Math.pow(2, 8*i);
	    }
	    return total;
	}
}
