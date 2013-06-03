import java.util.Date;


public abstract class State {
	public State process(String input, TCP tcp, UDPSender us,String udpMessage,String tcpMessage,Date timeEnteredState){
		return new Disconnected();
	}
}
