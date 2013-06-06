/*import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;*/


public class driver {
	//unused
	
	/*static UDPReceiver in=null;
	static UDPSender out =null;
	static Scanner sc=new Scanner(System.in);
	public static void main(String[] args) throws SocketException{
		if(args.length==0){
			try {
				out = new UDPSender();
				try {
					out.sendMessage(sc.nextLine());
				} catch (IOException e) {
					System.out.println("sending error");
				}
			} catch (SocketException | UnknownHostException e) {
				System.out.println("Error");
			}
		}
		else{
			in = new UDPReceiver();
			sc.next();
			byte[] store=in.getPacket();
			System.out.println(new String(store,0,store.length));
		}
	}*/
}
