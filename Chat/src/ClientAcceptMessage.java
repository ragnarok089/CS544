package chat;

public class ClientAcceptMessage extends Message {

	public ClientAcceptMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		super(_op,_length,_reserved,_options);
		if(op!=1){
			correct=false;
		}
	}

}
