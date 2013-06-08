/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the thread that sits and lisens for traffic
 * 	on the given tcp socket and then puts it onto a queue, so
 * 	that TCP can make non-blocking read calls and know how
 * 	much data is available.
 */
package Communications;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentLinkedQueue;


public class TCPReceiverThread implements Runnable {
	ConcurrentLinkedQueue<Byte> queue=null;//this is the queue that tcp reads from
	Socket socket=null;//the socket that this is listening on
	InputStream is=null;//the input stream of the socket
	boolean running=true;//indicates whether the loop should still be running or not
	
	//constructor just copies in the queue locally
	public TCPReceiverThread(ConcurrentLinkedQueue<Byte> _queue){
		queue=_queue;
	}
	
	//this sets the socket to the passed value
	public int setSocket(Socket _socket){
		socket=_socket;
		try {
			//set the timout to 500 and pull out the input stream
			socket.setSoTimeout(500);
			is=socket.getInputStream();
		} catch (IOException e) {
			return -1;
		}
		return 0;
	}
	
	//returns the ip of the socket
	public String getIP(){
		try{
		return socket.getInetAddress().getHostAddress();
		}catch(Exception e){
			return "";
		}
	}
	
	//The main thread of the receiver
	//it constantly makes blocking calls the read from the socekt
	//and then pushes the data it gets onto the queue
	public void run() {
		running=true;//initialize variables
		byte[] packet=null;
		//keep going until running gets set to false
		while(running){
			packet=new byte[5000];//where the data gets copied into
			try{
				//blocking call that returns the number of bytes copied into packet
				int size=is.read(packet);
				if(size<0){
					//if the stream ended
					running=false;
				}
				//copy the data from the packet to the queue
				for(int i=0;i<size;i++){
					queue.add(packet[i]);
				}
			}
			//if timout do nothing
			catch(SocketTimeoutException e){} 
			catch (IOException e) {
				//if the socket had an error print and end
				//unless it was shutting down anyway
				if(running!=false){
					System.out.println("TCP socket receive error");
					running=false;
				}
			}
		}
		//cleanup
		try {
			is.close();
		} catch (IOException e) {
		}
		try {
			socket.close();
		} catch (IOException e) {}
	}

	//stops the thread from running
	public void stop(){
		running=false;
	}

}
