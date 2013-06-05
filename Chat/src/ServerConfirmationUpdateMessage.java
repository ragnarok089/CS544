package chat;

public class ServerConfirmationUpdateMessage extends Message {
	String confirmedUsername=null;
       
	public static final long minSize=128;
        
	public ServerConfirmationUpdateMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		super(_op,_length,_reserved,_options);
		processBody(body);
		if(op!=7){
			correct=false;
		}
	}
	public ServerConfirmationUpdateMessage(int _op,long _length,long _reserved,String _options,String _confirmedUsername){
		super(_op,_length,_reserved,_options);
		confirmedUsername=_confirmedUsername;
		if(op!=7){
			correct=false;
		}
	}
	
        private void processBody(byte[] body){
		if(body.length!=128){
			correct=false;
			return;
		}
		byte [] confirmedUserArray = new byte[128];
                for (byte i = 0; i < body.length; i++){
                    confirmedUserArray[i] = body[i];
                }
		confirmedUsername=new String(confirmedUserArray,0,confirmedUserArray.length);
                
	}
	public byte[] convert(){
		byte[] upper=super.convert();
		byte[] storage=new byte[(int) (upper.length+minSize)];
		for(int i=0;i<upper.length;i++){
			storage[i]=upper[i];
		}
		
                int total=upper.length-1;

		byte[] tmp=null;
		
                tmp=confirmedUsername.getBytes();
		for(int i=0;i<tmp.length;i++){
			storage[total+i]=tmp[i];
		}
                
		total+=tmp.length;
                
		return storage;
	}
}
