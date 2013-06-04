
public class UDPBroadcastMessage extends Message {
	String senderUsername=null;
	String targetUsername=null;
	String senderIP=null;
	long sourcePort=-1;
	long destPort=-1;
	public static final long minSize=12;
	public UDPBroadcastMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		super(_op,_length,_reserved,_options);
		processBody(body);
		if(op!=1){
			correct=false;
		}
	}
	public UDPBroadcastMessage(int _op,long _length,long _reserved,String _options,String _senderUsername,String _targetUsername,String _senderIP){
		super(_op,_length,_reserved,_options);
		senderUsername=_senderUsername;
		targetUsername= _targetUsername;
		senderIP=_senderIP;
		if(op!=1){
			correct=false;
		}
	}
	private void processBody(byte[] body){
		if(body.length!=12){
			correct=false;
			return;
		}
		byte [] sourcePortArray=new byte[]{body[0],body[1]};
		sourcePort=fromByteArray(sourcePortArray);
		byte [] destPortArray=new byte[]{body[2],body[3]};
		destPort=fromByteArray(destPortArray);
		byte [] senderUserArray=new byte[]{body[4],body[5],body[6],body[7]};
		senderUsername=new String(senderUserArray,0,senderUserArray.length);
		byte [] senderIPArray=new byte[]{body[7],body[8],body[9],body[10]};
		senderIP=new String(senderIPArray,0,senderIPArray.length);
		//need to add one for target username
		//and to make the usernames longer
	}
	public byte[] convert(){
		byte[] upper=super.convert();
		byte[] storage=new byte[(int) (upper.length+minSize)];
		for(int i=0;i<upper.length;i++){
			storage[i]=upper[i];
		}
		int total=upper.length-1;
		byte[] tmp=numToByte((int) sourcePort,2);
		for(int i=0;i<tmp.length;i++){
			storage[total+i]=tmp[i];
		}
		total+=tmp.length;
		tmp=numToByte((int) destPort,2);
		for(int i=0;i<tmp.length;i++){
			storage[total+i]=tmp[i];
		}
		total+=tmp.length;
		tmp=senderUsername.getBytes();
		for(int i=0;i<tmp.length;i++){
			storage[total+i]=tmp[i];
		}
		total+=tmp.length;
		tmp=senderIP.getBytes();
		for(int i=0;i<tmp.length;i++){
			storage[total+i]=tmp[i];
		}
		total+=tmp.length;
		return storage;
	}
}
