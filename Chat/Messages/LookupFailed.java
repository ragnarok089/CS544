
public class LookupFailed {
	//private elements
	
	private MessageType Op;
	
	private short MessageLength;
	
	//LookupFialed sign default is 0;
	
	private byte LookupFailed = 0;
	
	private String[] Option = new String[1024];
	
	private String[] TargetUsername = new String[32];
	
	public LookupFailed(MessageType op, short messagelength,
			String[] option, String[] targetUsername){
		
		this.Op = op;
		
		this.MessageLength = messagelength;
		
		this.Option = option;
		
		this.TargetUsername = targetUsername;
		
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
	
	
	//return Lookup Failed sign but you cannot change its value
	
	public byte getLookupFailedSign(){
		return LookupFailed;
	}	
	
	//return and set Option field
	
	public String[] getOption(){
		return Option;
	}
	
	public void setOption(String[] option){
		this.Option = option;
	}
	
	//return and set TargetUsername
	
	public String[] getTargetUsername(){
		return TargetUsername;
	}
	
	public void setTargetUsername(String[] targetUsername){
		this.TargetUsername = targetUsername;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("MessageType is: ").append(Op).append("Message length is: ").append(MessageLength)
		.append("Lookup Failed sign is: ").append(LookupFailed).append("The target name is: ").append(TargetUsername);
		return sb.toString();
	}
	
}
