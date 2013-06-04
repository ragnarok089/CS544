
public class DeclineConnectionMessage {

	//Define elements in the ChatMessage
	
	private MessageType Op;
	
	private short MessageLength;
	
	private byte Decline = 0;
	
	//I have not add anything in the Option field yet.
	
	private String[] Option = new String[1024];

	public DeclineConnectionMessage(MessageType op, short messagelength
			, String[] option){
		
		this.Op = op;
		
		this.MessageLength = messagelength;
		
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
	
	//return Decline and you cannot change the default value of Decline
	
	public byte getDecline(){
		return Decline;
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
		sb.append("MessageType is: ").append(Op).append("Message Length is: ").append(MessageLength).append("Sign of Decline")
		.append(Decline);
		return sb.toString();
	}

}
