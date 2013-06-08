package Communications;
import java.io.*;
import java.net.*;
//import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;


public class UDPReceiverThread implements Runnable {
		protected DatagramSocket socket=null;
		protected ConcurrentLinkedQueue<Byte> out =null;
		protected volatile boolean running;
		
		public UDPReceiverThread(ConcurrentLinkedQueue<Byte> queue) throws SocketException{
			try {
				socket = new DatagramSocket(12346,InetAddress.getByName("0.0.0.0"));
			} catch (UnknownHostException e) {
				System.out.println("Failed to create UDP receiver");
			}
			socket.setSoTimeout(500);
			out=queue;
		}
		
		
		public void setRunning(boolean isRunning){
			running=isRunning;
		}
		
		public void run(){
			running=true;
			byte[] buffer=new byte[5000];
			DatagramPacket packet= new DatagramPacket(buffer,buffer.length);
			while(running){
				try{
					socket.receive(packet);
					byte[] data=packet.getData();
					for(int i=0;i<packet.getLength();i++){
						out.add(data[i]);
					}
				}
				catch(SocketTimeoutException e){
				} catch (IOException e) {
					System.out.println("socket receive error");
					running=false;
				}
			}
			socket.close();
		}
		

}
