package Communications;
import java.io.*;
import java.net.*;
//import java.util.Enumeration;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;


public class UDPReceiverThread implements Runnable {
		protected DatagramSocket socket=null;
		protected ConcurrentLinkedQueue<Byte> out =null;
		protected volatile boolean running;
		
		public UDPReceiverThread(ConcurrentLinkedQueue<Byte> queue) throws SocketException{
			InetAddress address = null;
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback())
					continue; // Don't want to broadcast to the loopback
								// interface
				Enumeration<InetAddress> addresses = networkInterface
						.getInetAddresses();
				while (addresses.hasMoreElements()) {
					address = addresses.nextElement();
					if (!address.getHostAddress().contains("192.168.224"))
						continue;
					break;
				}
				if (address != null || address.toString().contains("192.168.224")) {
					break;
				}
			}
			socket = new DatagramSocket(12346,address);
			socket.setSoTimeout(500);
			out=queue;
		}
		
		
		public void setRunning(boolean isRunning){
			running=isRunning;
		}
		
		public void run(){
			running=true;
			byte[] buffer=new byte[1024];
			DatagramPacket packet= new DatagramPacket(buffer,buffer.length);
			while(running){
				try{
					socket.receive(packet);
					byte[] data=packet.getData();
					for(int i=0;i<data.length;i++){
						out.add(data[i]);
					}
					System.out.println("Got it");
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
