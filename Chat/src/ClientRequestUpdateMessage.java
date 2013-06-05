package chat;

public class ClientRequestUpdateMessage extends Message {
	String senderUsername=null;

	public static final long minSize=128;
       
	public ClientRequestUpdateMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		super(_op,_length,_reserved,_options);
		processBody(body);
		if(op!=6){
			correct=false;
		}
	}
	public ClientRequestUpdateMessage(int _op,long _length,long _reserved,String _options,String _senderUsername){
		super(_op,_length,_reserved,_options);
		senderUsername=_senderUsername;
		if(op!=6){
			correct=false;
		}
	}
	private void processBody(byte[] body){
		if(body.length!=128){
			correct=false;
			return;
		}
		byte [] senderUserArray = new byte[128];
                for (byte i = 0; i < body.length; i++){
                    senderUserArray[i] = body[i];
                }
		senderUsername=new String(senderUserArray,0,senderUserArray.length);
                
	}
	public byte[] convert(){
		byte[] upper=super.convert();
		byte[] storage=new byte[(int) (upper.length+minSize)];
		for(int i=0;i<upper.length;i++){
			storage[i]=upper[i];
		}
		
                int total=upper.length-1;

		byte[] tmp=null;
		
                tmp=senderUsername.getBytes();
		for(int i=0;i<tmp.length;i++){
			storage[total+i]=tmp[i];
		}
                
		total+=tmp.length;
                
		return storage;
	}
}
