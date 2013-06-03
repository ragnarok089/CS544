import java.net.*;

public class ClientHandShake {
	
	//Define elements in the ChatMessage
	
	private MessageType Op;
	
	private short MessageLength;
	
	private byte Reserved = 1;
	
	//I have not add anything in the Option field yet.
	
	private String[] Option = new String[1024];
	
	//default Port number is 9876
	
	private short SourcePortNumber = 9876;
	
	private short DestinationPortNumber = 9876;
	
	private InetAddress ClientIPaddress;
	
	private InetAddress TargetIPaddress;
	
	public ClientHandShake(MessageType op, short messagelength, byte reserved,
			String[] option, short sourcePortNumber, short destinationPortNumber, 
			InetAddress clientIPaddress, InetAddress targetIPaddress){
		
		this.Op = op;
		
		this.MessageLength = messagelength;
		
		this.Reserved = reserved;
		
		this.Option = option;
		
		this.SourcePortNumber = sourcePortNumber;
		
		this.DestinationPortNumber = destinationPortNumber;
		
		this.ClientIPaddress = clientIPaddress;
		
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
	
	//return and set source port number
	
	public short getSoucePortNumber(){
		return SourcePortNumber;
	}
	
	public void setSoucePortNumber(short sourcePortNumber){
		this.SourcePortNumber = sourcePortNumber;
	}
	
	//return and set Destination port number
	
	public short getDestinationPortNumber(){
		return DestinationPortNumber;
	}
	
	public void setDestinationPortNumber(short destinationPortNumber){
		this.DestinationPortNumber = destinationPortNumber;
	}
	
	//return and set Client's IP address
	
	public InetAddress getClientIPaddress(){
		return ClientIPaddress;
	}
	
	public void setClientIPaddress(InetAddress clientIPaddress){
		this.ClientIPaddress = clientIPaddress;
	}
	
	//return and set Target's IP address
	
	public InetAddress getTargetIPaddress(){
		return TargetIPaddress;
	}
	
	public void setTargetIPaddress(InetAddress targetIPaddress){
		this.TargetIPaddress = targetIPaddress;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("MessageType is: ").append(Op).append("Message Length is: ").append(MessageLength).append("Client's IP Address is: ").append(ClientIPaddress)
		.append("Target IP Address is: ").append(TargetIPaddress);
		return sb.toString();
	}
}
