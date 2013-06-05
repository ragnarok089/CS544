import java.io.IOException;
import java.util.Date;


public class Disconnected extends State {
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState){
	
		if(tcp.getActive()==true){
			return new Connected();
		}
		else if(input.startsWith(":ip")){
			if(0>tcp.connect(input.substring(4))){
				System.out.println("Unable to connect to IP address");
			}
			return this;
		}
		else if(input.startsWith(":local")){
			try {
				String senderUsername=User.getUserName();
				String targetUsername=input.substring(7);
				String ip=tcp.getIP();
				UDPBroadcastMessage message=new UDPBroadcastMessage(1,(long)144,(long)0,"",senderUsername,targetUsername,ip);
				us.sendMessage(message);
			} catch (IOException e) {}
			return new Waiting();
		}
		else if(udpMessage!=null){
			if(udpMessage instanceof UDPBroadcastMessage){
				UDPBroadcastMessage m=(UDPBroadcastMessage)udpMessage;
				if(m.correct && m.targetUsername.equals(User.userName)){
					System.out.println("Received a broadcast with your username and calling them back");
					tcp.connect(m.senderIP);
				}
			}
			return this;
		}
		else{
			return this;
		}
	}
	

}
