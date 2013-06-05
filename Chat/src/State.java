import java.util.Date;


public abstract class State {
	public abstract State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState);

}
