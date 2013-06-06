package Server;

import Communications.*;
import Utilities.InputReader;

public class ServerMainLoop {
	static TCP tcp=null;
	static boolean error=false;
	static boolean done=false;
	static InputReader ir = new InputReader();
	
	public static void main(String[] args){
		tcp=new TCP();
		Thread irThread=new Thread(ir);
		irThread.start();
		while(!done && !error){
			if(tcp.getActive()){
				ServerThread st=new ServerThread();
				st.setTCP(tcp);
				(new Thread(st)).start();
				tcp=new TCP();
			}
		}
	}
}
