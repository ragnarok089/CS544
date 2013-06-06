package States;
import Communications.*;
import Messages.*;




public class Connected extends State {
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState){
		
		if(tcp.getActive()==false){
			System.out.println("Otherside disconnceted");
			return new Disconnected();
		}
		else if(input.startsWith(":server")){
			//send server handshake
			return new MidhandshakeServer();
		}
		else if(input.startsWith(":client")){
			//send client handshake
			return new MidhandshakeClient();
		}
		else if(input.startsWith(":exit")){
			try{
				tcp.close();
			}
			catch(Exception e){}
			System.out.println("Disconnecting");
			return new Disconnected();
		}
		else{
			return this;
		}
	}
	
}
