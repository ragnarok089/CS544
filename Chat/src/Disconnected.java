import java.io.IOException;
import java.util.Date;


public class Disconnected extends State {
	public State process(String input, TCP tcp, UDPSender us,String udpMessage,String tcpMessage,Date timeEnteredState){
	
		if(tcp.active==true){
			return new Connected();
		}
		else if(input.startsWith(":ip")){
			tcp.connect(input.substring(4));
			return this;
		}
		else if(input.startsWith(":local")){
			try {
				us.sendMessage("");
			} catch (IOException e) {}
			//TODO insert UDP broadcast message
			return new Waiting();
		}
		else if(!udpMessage.equals("")){
			tcp.connect(target);
			//TODO extract the target from the message
			return this;
		}
		else if(!tcpMessage.equals("")){
			return new Error("In disconnected Received a tcpMessage");
		}
		else{
			return this;
		}
	}
}
