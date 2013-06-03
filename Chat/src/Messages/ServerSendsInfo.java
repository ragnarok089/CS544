import java.net.InetAddress;

public class ServerSendsInfo {
	
	//Define elements in the ChatMessage
	
	private MessageType Op;
	
	private short MessageLength;
	
	private byte Reserved = 1;
	
	//I have not add anything in the Option field yet.
	
	private String[] Option = new String[1024];
	
	private String[] TargetUsername = new String[32];
	
	private InetAddress TargetIPaddress;
	
	public ServerSendsInfo(MessageType op, short messagelength, byte reserved,
			String[] option, String[] targetUsername, InetAddress targetIPaddress){
		
		this.Op = op;
		
		this.MessageLength = messagelength;
		
		this.Reserved = reserved;
		
		this.Option = option;
		
		this.TargetUsername = targetUsername;
		
		this.TargetIPaddress = targetIPaddress;

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
	
	//return and set Target's username
	
	public String[] getTargetUsername(){
		return TargetUsername;
	}
	
	public void setTargetUsername(String[] targetUsername){
		this.TargetUsername = targetUsername;
	}
	
	//return and set Sender's IP address
	
	public InetAddress getTargetIPaddress(){
		return TargetIPaddress;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("MessageType is: ").append(Op).append("Message Length is: ").append(MessageLength)
		.append("Target's username is: ").append(TargetUsername).append("Target's IP Address is: ")
		.append(TargetIPaddress);
		return sb.toString();
	}
	
}
