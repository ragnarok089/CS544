import java.io.*;
import java.net.SocketException;
import java.util.concurrent.*;


public class UDPReceiver  {
	protected PipedInputStream data=null;
	protected UDPReceiverThread thread=null;
	protected Thread t;
	protected ConcurrentLinkedQueue<Byte> queue=new ConcurrentLinkedQueue<Byte>();
	public UDPReceiver() throws SocketException{
		data=new PipedInputStream();
		thread=new UDPReceiverThread(queue);
		t=(new Thread(thread));
		t.start();
	}
	
	public byte[] getPacket(){
		if(queue.size()<size){
			return new byte[0];
		}
		else{
			byte[] packet = new byte[size];
			for(int i=0;i<size;i++){
				packet[i]=queue.poll();
			}
		}
	}
	
	public void stop() throws InterruptedException{
		thread.setRunning(false);
		t.join();
	}
}
