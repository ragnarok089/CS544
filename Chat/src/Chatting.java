
public class Chatting extends State{
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState){
		
		if(tcp.getActive()==false){
			System.out.println("Other side disconnected");
			return new Disconnected();
		}
		else if(input.startsWith(":exit")){
			try{
				tcp.close();
			}
			catch(Exception e){}
			System.out.println("Disconnecting");
			return new Disconnected();
		}
		else if(!input.equals("")){
			System.out.println(input);
			//send chat message
			return this;
		}
		else if(tcpMessage instanceof ChatMessage){
			System.out.println(tcpMessage.text);
		}
		else{
			return this;
		}
	}
}
