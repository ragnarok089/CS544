package Messages;


public class ClientAcceptMessage extends Message {

	public ClientAcceptMessage(int _op,long _length,long _reserved,String _options,int[] body){
		super(_op,_length,_reserved,_options);
		if(op!=5){
			correct=false;
		}
	}

}
