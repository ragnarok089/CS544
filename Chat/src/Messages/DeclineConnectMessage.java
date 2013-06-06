package Messages;


public class DeclineConnectMessage extends Message {

	public DeclineConnectMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		super(_op,_length,_reserved,_options);
		if(op!=10){
			correct=false;
		}
	}
}
