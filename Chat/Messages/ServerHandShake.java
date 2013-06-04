import java.net.*;

public class ServerHandShake {
	
	//Define elements in the ChatMessage
	
	private MessageType Op;
	
	private short MessageLength;
	
	private byte Reserved = 1;
	
	//I have not add anything in the Option field yet.
	
	private String[] Option = new String[1024];
	
	//default Port number is 9876
	
	private short SourcePortNumber = 9876;
	
	private short DestinationPortNumber = 9876;
	
	private String[] ServerUsername = new String[32];
	
	private InetAddress ServerIPaddress;
	
	public ServerHandShake(MessageType op, short messagelength, byte reserved,
			String[] option, short sourcePortNumber, short destinationPortNumber, 
			String[] serverName, InetAddress serverIPaddress){
		
		this.Op = op;
		
		this.MessageLength = messagelength;
		
		this.Reserved = reserved;
		
		this.Option = option;
		
		this.SourcePortNumber = sourcePortNumber;
		
		this.DestinationPortNumber = destinationPortNumber;
		
		this.ServerUsername = serverName;
		
		this.ServerIPaddress = serverIPaddress;
		
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
	
	//return and set ServerUsername
	
	public String[] getServerUsername(){
		return ServerUsername;
	}
	
	public void setServerUsername(String[] serverUsername){
		this.ServerUsername = serverUsername;
	}
	
	//return and set Server IP address
	
	public InetAddress getServerIPaddress(){
		return ServerIPaddress;
	}
	
	public void setServerIPaddress(InetAddress serverIPaddress){
		this.ServerIPaddress = serverIPaddress;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("MessageType is: ").append(Op).append("Message Length is: ").append(MessageLength).append("Server's username is: ").append(ServerUsername)
		.append("Server IP Address is: ").append(ServerIPaddress);
		return sb.toString();
	}
	
	
	
}
