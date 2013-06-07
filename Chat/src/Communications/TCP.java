package Communications;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;

import Messages.Message;
import Utilities.Parser;


public class TCP implements Runnable {
	Socket socket=null;
    TCPReceiverThread tr=null;
    Thread t=null;
    ConcurrentLinkedQueue<Byte> queue=new ConcurrentLinkedQueue<Byte>();
    protected boolean active;
    int size=132;
    int moreNeeded=Integer.MAX_VALUE;
    boolean needsMore=false;
    byte[] current=null;
    byte[] body=null;
    Parser parser=new Parser();
	public String pendingIP="";
	public ServerSocket serverSocket=null;
	public InetAddress ip=null;
    
    public TCP(){
		try {
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
				if (address != null && address.toString().contains("192.168.224")) {
					break;
				}
			}
			ip=address;
			System.out.println(queue.hashCode());
			tr = new TCPReceiverThread(queue);
			System.out.println(queue.hashCode());
		}
    	catch(Exception e){
    		System.out.println("Could not set up TCP");
    		System.exit(-1);
    	}
    }
    public boolean getActive(){
    	return active && !socket.isClosed();
    }
	public void run(){
		try{
		 serverSocket = new ServerSocket(12345,0,ip);
         Socket socket2 = serverSocket.accept();
         if(!getActive()){
        	 socket=socket2;
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
			if(getActive()){
				return -1;
			}
			active=true;
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
        	 	System.out.println(new String(message,0,message.length));
                 OutputStream socketOutputStream = socket.getOutputStream();
                 socketOutputStream.write(message, 0, message.length);
         } catch (Exception e) {
        	 return -1;
         }
         return 0;
	 }
	 public int send(Message message) {
         try {
        	 System.out.println(new String(message.convert(),0,message.convert().length));
        	 System.out.println(message.getCorrect());
                 OutputStream socketOutputStream = socket.getOutputStream();
                 byte[] buffer=message.convert();
                 socketOutputStream.write(buffer, 0, buffer.length);
         } catch (Exception e) {
        	 return -1;
         }
         return 0;
	 }
	
	public Message read(){
		if (!needsMore) {
			if(queue.size()!=0){
				System.out.println(queue.size());
			}
			if (queue.size() >= size) {
				System.out.println("Getting first");
				current = new byte[size];
				for (int i = 0; i < size; i++) {
					current[i] = queue.poll();
				}
				moreNeeded=parser.parse(current);
				if(0>moreNeeded){
					System.out.println("Parser Error");
				}
				if(queue.size()>=moreNeeded){
					System.out.println("second is already here");
					for (int i = 0; i < moreNeeded; i++) {
						body[i] = queue.poll();
					}
					needsMore=false;
					moreNeeded=Integer.MAX_VALUE;
					return parser.addBody(body);
				}
				needsMore=true;
				return null;
			} else {
				return null;
			}
		}
		else if(queue.size()>=moreNeeded){
			System.out.println("Getting second");
			for (int i = 0; i < moreNeeded; i++) {
				body[i] = queue.poll();
			}
			needsMore=false;
			moreNeeded=Integer.MAX_VALUE;
			return parser.addBody(body);
		}
		return null;
	}
	
	public String getIP(){
		return serverSocket.getInetAddress().getHostAddress();
	}
	
	public void close() throws IOException{
		active=false;
		tr.stop();
		queue.clear();
		socket.close();
	}
}
