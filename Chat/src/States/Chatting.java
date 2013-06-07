package States;
import Communications.TCP;
import Communications.UDPSender;
import Messages.ChatMsgMessage;
import Messages.Message;


public class Chatting extends State{
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState,boolean firstCall){
		if(firstCall){
			System.out.println("You are can now chat with the other person");
		}
		if(tcp.getActive()==false){
			System.out.println("The other side disconnected");
			return new Disconnected();
		}
		else if(!input.equals("")){
			//System.out.println(input);
			Message message=new ChatMsgMessage(11,input.length()+Message.minSize,0,"",input);
			tcp.send(message);
			return this;
		}
		else if(tcpMessage instanceof ChatMsgMessage && tcpMessage.getCorrect()){
			System.out.println(((ChatMsgMessage)tcpMessage).messages);
			return this;
		}
		else{
			return this;
		}
	}
}
