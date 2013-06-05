package chat;

public class ServerAcceptMessage extends Message {

	public ServerAcceptMessage(int _op,long _length,long _reserved,String _options,byte[] body){
		super(_op,_length,_reserved,_options);

		if(op!=4){
			correct=false;
		}
	}

}
