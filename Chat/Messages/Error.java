
public class Error {
	
	//private elements
	
	private MessageType Op;
	
	private short MessageLength;
	
	//Error default is 0;
	
	private byte Error = 0;
	
	private String[] Option = new String[1024];
	
	public Error(MessageType op, short messagelength,
			String[] option){
		
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
	
	
	//return Error but you cannot change its value
	
	public byte getError(){
		return Error;
	}	
	
	public String[] getOption(){
		return Option;
	}
	
	public void setOption(String[] option){
		this.Option = option;
	}
		
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("MessageType is: ").append(Op).append("message length is: ").append(MessageLength).append("Error sign: ").append(Error);
		return sb.toString();
	}
	
	
}
