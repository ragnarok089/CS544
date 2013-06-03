
public class ServerAccept {
	
	//private elements
	
	private MessageType Op;
	
	private short MessageLength;
	
	//ServerACK default is 1;
	
	private byte ServerACK = 1;
	
	private String[] Option = new String[1024];
	
	public ServerAccept(MessageType op, short messagelength, byte reserved,
			String[] option){
		
		this.Op = op;
		
		this.MessageLength = messagelength;
		
		this.ServerACK = reserved;
		
		this.Option = option;
		
	}
	
	//return and set Op sign
	
	public MessageType getOp(){
		return Op;
	}
		
	public void setOp(MessageType op){
		this.Op = op;
	}
		
	//return and set MessageLength
		
	public short getMessageLength(){
		return MessageLength;
	}
	
	public void setMessageLength(short messagelength){
		this.MessageLength = messagelength;
	}
	
	//return ServerACK and you cannot change the value of it.
	
	public byte getServerACK(){
		return ServerACK;
	}
	
	//return and set Options
	
	public String[] getOption(){
		return Option;
	}
	
	public void setOption(String[] option){
		this.Option = option;
	}
		
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("MessageType is: ").append(Op).append("Message length is: ").append(MessageLength).append("ACK vaule: ").append(ServerACK);
		return sb.toString();
	}
	
}
