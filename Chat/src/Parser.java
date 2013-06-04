
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
			case 1: //udp broadcast
				return 12;
			case 241:// server handshake start
				return 12;
			case 2:// client handshake start
				return 12;
			case 242://server accept
				return 0;
			case 3://client accept
				return 4;
			case 4://client request update
				return 4;
			case 243://server confirmation of update
				return 4;
			case 5://client request info from server
				return 12;
			case 244://server sends requested info
				return 8;
			case 6://Decline to connect Message
				return 0;
			case 4: //chat message
				if(length-132<0){
					return -1;
				}
				else{
					return (int) (length-132);
				}
			case 161: //lookup failed
				return 4;
			case 162://Error
				return 0;
			case 163://name collision
				return 4;
			default:
				return -1;
		}
	}
	public Message addBody(byte[] body){
		switch(op){
		case 1: //udp broadcast
			return new UDPBroadcastMessage(op,length,reserved,options,body);
		case 241:// server handshake start
			return new ServerHandshakeStartMessage(op,length,reserved,options,body);
		case 2:// client handshake start
			return new ClientHandshakeStartMessage(op,length,reserved,options,body);
		case 242://server accept
			return new ServerAcceptMessage(op,length,reserved,options,body);
		case 3://client accept
			return new ClientAcceptMessage(op,length,reserved,options,body);
		case 4://client request update
			return new ClientRequestUpdateMessage(op,length,reserved,options,body);
		case 243://server confirmation of update
			return new ServerConfirmUpdateMessage(op,length,reserved,options,body);
		case 5://client request info from server
			return new ClientRequestInfoMessage(op,length,reserved,options,body);
		case 244://server sends requested info
			return new ServerSendInfoMessage(op,length,reserved,options,body);
		case 6://Decline to connect Message
			return new DeclineConnectionMessage(op,length,reserved,options,body);
		case 4: //chat message
			return new ChatMessage(op,length,reserved,options,body);
		case 161: //lookup failed
			return new LookupFailedMessage(op,length,reserved,options,body);
		case 162://Error
			return new ErrorMessage(op,length,reserved,options,body);
		case 163://name collision
			return new NameCollisonMessage(op,length,reserved,options,body);
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
