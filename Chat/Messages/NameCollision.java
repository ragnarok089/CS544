
public class NameCollision {
	
	//private elements
	
	private MessageType Op;
	
	private short MessageLength;
	
	//Name collision default is 0;
	
	private byte NameCollision = 0;
	
	private String[] Option = new String[1024];
	
	private String[] TargetUsername = new String[32];
	
	public NameCollision(MessageType op, short messagelength,
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
		
		
		//return NameCollision sign and you cannot change its value
		
		public byte getNameCollision(){
			return NameCollision;
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
			.append("Name collision sign: ").append(NameCollision).append("The Target's name").append(TargetUsername);
			return sb.toString();
		}
		
	
}
