import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class TCP implements Runnable {
	Socket socket=null;
    PushbackInputStream clientInputStream;
    TCPReceiverThread tr=null;
    Thread t=null;
    ConcurrentLinkedQueue<Byte> queue=null;
    boolean active;
        
    public TCP(){
    	queue=new ConcurrentLinkedQueue<Byte>();
    	tr=new TCPReceiverThread(queue);
    }
	public void run(){
		try{
		 ServerSocket serverSocket = new ServerSocket(12345);
         Socket socket2 = serverSocket.accept();
         if(!active){
        	 socket=socket2;
             clientInputStream = new PushbackInputStream(socket.getInputStream());
             active=true;
             tr.setSocket(socket);
             t=new Thread(tr);
             t.start();
         }
         else{
        	 socket2.close();
         }
		}
		catch(Exception e){
			active=false;
		}
	}
	
	public int connect(String target){
		try {
			if(active){
				return -1;
			}
			socket = new Socket(target, 12345);
			tr.setSocket(socket);
            t=new Thread(tr);
            t.start();
		} catch (UnknownHostException e) {
			return -1;
		} catch (IOException e) {
			return -2;
		}
		return 0;
	}
	 public int send(byte[] message) {
         try {
                 OutputStream socketOutputStream = socket.getOutputStream();
                 socketOutputStream.write(message, 0, message.length);
         } catch (Exception e) {
        	 return -1;
         }
         return 0;
	 }
	
	public byte[] read(){
		//TODO implement reading the header figuring out packet size then reading the rest
		if(queue.size()>size){
			byte[] packet=new byte[size];
			for(int i=0;i<size;i++){
				packet[i]=queue.poll();
			}
			return packet;
		}
		else{
			return new byte[0];
		}
	}
	
	public void close() throws IOException{
		active=false;
		queue.clear();
		tr.stop();
		clientInputStream.close();
		socket.close();
	}
}
