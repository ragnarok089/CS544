import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;


public class MainLoop {
	static boolean error =false;
	static boolean done = false;
	static InputReader ir = new InputReader();
	static String input;
	static TCP tcp=new TCP();
	static UDPReceiver ur;
	static UDPSender us;
	static CurrentState state=new CurrentState();
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
		long timeEnteredState=System.currentTimeMillis();
		Message udpMessage=null;
		Message tcpMessage=null;
		while(!error && !done){
			if(state.getState().getClass()!=lastState.getClass()){
				timeEnteredState=System.currentTimeMillis();
			}
			System.out.print("\r"+ir.getInput());
			input=ir.getSubmitted();
			if (input.equals("")) {
				udpMessage = ur.read();
				if(udpMessage==null){
					tcpMessage = tcp.read();
					if(tcpMessage==null){
						lastState = state.getState();
					}
				}
				
			}
			state.process(input,tcp,us,udpMessage,tcpMessage,timeEnteredState);
		}
		ir.stop();
		try {
			ur.stop();
		} catch (InterruptedException e) {}
	}

}
