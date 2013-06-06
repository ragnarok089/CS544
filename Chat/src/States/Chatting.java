package States;
import Communications.TCP;
import Communications.UDPSender;
import Messages.ChatMsgMessage;
import Messages.ErrorMessage;
import Messages.Message;


public class Chatting extends State{
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState){
		
		if(tcp.getActive()==false){
			System.out.println("Other side disconnected");
			return new Disconnected();
		}
		else if(input.startsWith(":")){
			System.out.println("Invalid command");
			return this;
		}
		else if(!input.equals("")){
			System.out.println(input);
			Message message=new ChatMsgMessage(11,input.length()+Message.minSize,0,"",input);
			tcp.send(message);
			return this;
		}
		else if(tcpMessage instanceof ChatMsgMessage && tcpMessage.getCorrect()){
			System.out.println(((ChatMsgMessage)tcpMessage).messages);
			return this;
		}
		else if(tcpMessage != null){
			tcp.send(new ErrorMessage(13,Message.minSize,0,"",new byte[0]));
			return this;
		}
		else{
			return this;
		}
	}
}
