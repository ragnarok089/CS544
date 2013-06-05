import java.io.IOException;


public class MidhandshakeClient extends State{
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState){
		
		if(tcp.getActive()==false){
			try {
				tcp.close();
			} catch (IOException e) {}
			return new Disconnected();
		}
		else if(input.startsWith(":exit")){
			try {
				tcp.close();
			} catch (IOException e) {}
			System.out.println("Disconnecting");
			return new Disconnected();
		}
		else if(tcpMessage instanceof ClientAcceptMessage){
			return new Chatting();
		}
		else if(tcpMessage instanceof ServerAcceptMessage || tcpMessage instnace of DeclineMessage){
			try {
				tcp.close();
			} catch (IOException e) {}
			System.out.println("Expected a client response but got a server response");
			return new Disconnected();
		}
		else{
			return this;
		}
	}
}
