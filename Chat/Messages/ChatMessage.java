
public class ChatMessage {
	
	//Define elements in the ChatMessage
	
	private MessageType Op;
	
	private short MessageLength;
	
	private byte Reserved = 1;
	
	//I have not add anything in the Option field yet.
	
	private String[] Option = new String[1024];
	
	private String[] Messages;
	
	//
	
	public ChatMessage(MessageType op, short messagelength, byte reserved,
			String[] option, String[] messages){
		
		this.Op = op;
		
		this.MessageLength = messagelength;
		
		this.Reserved = reserved;
		
		this.Option = option;
		
		this.Messages = messages;
		
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
	
	//return and set Reserved
	
	public byte getReserved(){
		return Reserved;
	}
	
	public void setReserved(byte reserved){
		this.Reserved = reserved;
	}
	
	//return and set Options
	
	public String[] getOption(){
		return Option;
	}
	
	public void setOption(String[] option){
		this.Option = option;
	}
	
	//return and set Messages
	
	public String[] getMessage(){
		return Messages;
	}
	
	public void setMessage(String[] messages){
		this.Messages = messages;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("MessageType is: ").append(Op).append("Message Length is: ").append(MessageLength).append("Messages is").append(Messages);
		return sb.toString();
	}
	
}
