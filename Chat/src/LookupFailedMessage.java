package chat;

public class LookupFailedMessage extends Message {
	String targetUsername=null;

	public static final long minSize=128;
        
	public LookupFailedMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		super(_op,_length,_reserved,_options);
		processBody(body);
		if(op!=12){
			correct=false;
		}
	}
	public LookupFailedMessage(int _op,long _length,long _reserved,String _options,String _targetUsername){
		super(_op,_length,_reserved,_options);
		targetUsername= _targetUsername;
		if(op!=12){
			correct=false;
		}
	}
        
	private void processBody(byte[] body){
		if(body.length!=128){
			correct=false;
			return;
		}

                byte [] targetUserArray = new byte[128];
                for (byte i = 0; i < body.length; i++){
                    targetUserArray[i] = body[i];
                }
		targetUsername=new String(targetUserArray,0,targetUserArray.length);       
		
	}
        
	public byte[] convert(){
		byte[] upper=super.convert();
		byte[] storage=new byte[(int) (upper.length+minSize)];
		for(int i=0;i<upper.length;i++){
			storage[i]=upper[i];
		}
		
                int total=upper.length-1;

		byte[] tmp=null;
		
                tmp=targetUsername.getBytes();
		for(int i=0;i<tmp.length;i++){
			storage[total+i]=tmp[i];
		}
                
		total+=tmp.length;
                
		return storage;
	}
}
