import java.net.*;

public class UDPbroadcast {
	
	//Define elements in the ChatMessage
	
	private MessageType Op;
	
	private short MessageLength;
	
	private byte Reserved = 1;
	
	//I have not add anything in the Option field yet.
	
	private String[] Option = new String[1024];
	
	private String[] SenderUsername = new String[32];
	
	private InetAddress SenderIPaddress;
	
	public UDPbroadcast(MessageType op, short messagelength, byte reserved,
			String[] option, String[] senderUsername, InetAddress senderIPaddress){
		
		this.Op = op;
		
		this.MessageLength = messagelength;
		
		this.Reserved = reserved;
		
		this.Option = option;
		
		this.SenderUsername = senderUsername;
		
		this.SenderIPaddress = senderIPaddress;
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
	
	//return and set Sender's Username
	
	public String[] getSenderUsername(){
		return SenderUsername;
	}
	
	public void setSenderUsername(String[] senderUsername){
		this.SenderUsername = senderUsername;
	}
	
	//return and set Sender's IP address
	
	public InetAddress getSenderIPaddress(){
		return SenderIPaddress;
	}
	
	public void setSenderIPaddress(InetAddress senderIPaddress){
		this.SenderIPaddress = senderIPaddress;
	}
	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("MessageType is: ").append(Op).append("Message Length is: ").append(MessageLength)
		.append("Sender's username is: ").append(SenderUsername).append("Sender's IP Address is: ")
		.append(SenderIPaddress);
		return sb.toString();
	}
	
	
}
