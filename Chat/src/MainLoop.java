import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;


public class MainLoop {
	static boolean error =false;
	static boolean done = false;
	static InputReader ir = new InputReader();
	static String input;
	static TCP tcp=new TCP();
	static UDPReceiver ur;
	static UDPSender us;
	static State state=new Disconnected();
	static State lastState=new Disconnected();
	public static void main(String[] args) {
		try {
			ur=new UDPReceiver();
			us=new UDPSender();
		} catch (SocketException | UnknownHostException e) {
			System.out.println("Could not set up UDP socket");
			System.exit(-1);
		}
		Thread tcpListen=new Thread(tcp);
		tcpListen.start();
		Thread irThread=new Thread(ir);
		irThread.start();
		Date timeEnteredState=new Date();
		String udpMessage="";
		String tcpMessage="";
		while(!error && !done){
			if(state.getClass()!=lastState.getClass()){
				timeEnteredState=new Date();
			}
			System.out.print("\r"+ir.getInput());
			input=ir.getSubmitted();
			byte[] packet=ur.getPacket();
			udpMessage=new String(packet,0,packet.length);
			packet=tcp.read();
			tcpMessage=new String(packet,0,packet.length);
			state=state.process(tcpMessage, tcp, us, udpMessage, tcpMessage, timeEnteredState);
		}
		ir.stop();
		try {
			ur.stop();
		} catch (InterruptedException e) {}
	}

}
