import java.io.IOException;


public class MidhandshakeServer extends State{
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
		else if(tcpMessage instanceof ServerAcceptMessage){
			return new ConnectedServer();
		}
		else if(tcpMessage instanceof ClientAcceptMessage || tcpMessage instanceof DeclineMessage){
			try {
				tcp.close();
			} catch (IOException e) {}
			System.out.println("Expected a server response but got a client response");
			return new Disconnected();
		}
		else{
			return this;
		}
	}
}
