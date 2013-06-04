
public class ServerConfirmationUpdate {
	
	//Define elements in the confirmation update
	
	private MessageType Op;
	
	private short MessageLength;
	
	private byte Reserved = 1;
	
	//I have not add anything in the Option field yet.
	
	private String[] Option = new String[1024];

	private String[] confirmedUsername = new String[32];
	
	public ServerConfirmationUpdate(MessageType op, short messagelength, byte reserved,
			String[] option, String[] confirmedusername){
		
		this.Op = op;
		
		this.MessageLength = messagelength;
		
		this.Reserved = reserved;
		
		this.Option = option;
		
		this.confirmedUsername = confirmedusername;
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
	
	//return and set Options
	
	public String[] getOption(){
		return Option;
	}
	
	public void setOption(String[] option){
		this.Option = option;
	}

	//return and set confirmed Username
	
	public String[] getSenderUsername(){
		return confirmedUsername;
	}
	
	public void setSenderUsername(String[] confirmedusername){
		this.confirmedUsername = confirmedusername;
	}

	
}
