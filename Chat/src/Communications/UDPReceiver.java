/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is a class that sets up an asynchronous listener on the udp socket
 * 	that takes the data and adds it to a queue. When read is called it is removed
 * 	from the queue and returned
 */
package Communications;

import java.net.SocketException;
import java.util.concurrent.*;

import Messages.ErrorMessage;
import Messages.Message;
import Utilities.Parser;


public class UDPReceiver  {
	protected UDPReceiverThread thread=null;//the asynchronous listener
	protected Thread t;//the thread version of the listener
	protected ConcurrentLinkedQueue<Byte> queue=new ConcurrentLinkedQueue<Byte>();//the queue to read from the listener
	protected boolean needsMore=false;//whether the header has already been read
	int size=Message.minSize;//the size of the header
    int moreNeeded=Integer.MAX_VALUE;//the number of bytes needed for the body
    byte[] current=null;//storage
    byte[] body=null;
    Parser parser=new Parser();//the parser that converts byte[] into messages
    
    //creates the new listener and kicks it off
	public UDPReceiver() throws SocketException{
		thread=new UDPReceiverThread(queue);
		t=(new Thread(thread));
		t.start();
	}
	
	//takes data from the queue and attempts to parse it
	//it returns null if no new messages (not enough data)
	//and it returns the message if there was one
	public Message read(){
		//if the header has not been already in read
		if (!needsMore) {
			//if there are enough bytes for the header
			if (queue.size() >= size) {
				//move the bytes from the queue
				current = new byte[size];
				for (int i = 0; i < size; i++) {
					current[i] = queue.poll();
				}
				//create the header from the bytes and returns the number of bytes needed for the body
				moreNeeded=parser.parse(current);
				if(moreNeeded<0){
					//if the bytes formed an invalid header stop reading purge the queue and return an error message
					System.out.println("Parser Error");
					thread.setRunning(false);
					queue.clear();
					return new ErrorMessage();
				}
				//if there are enough bytes already for the body
				if(queue.size()>=moreNeeded){
					//get the bytes from the queue
					body=new byte[moreNeeded];
					for (int i = 0; i < moreNeeded; i++) {
						body[i] = queue.poll();
					}
					//reset the state of this method
					needsMore=false;
					moreNeeded=Integer.MAX_VALUE;
					//parse the body and return the resulting message
					return parser.addBody(body);
				}
				//if there weren't enough for the body
				//indicate that and return null
				needsMore=true;
				return null;
			} else {
				//if there weren't enough for the body
				//indicate that and return null
				return null;
			}
		}
		//if the header has already been read and there are enough bytes for the body
		else if(queue.size()>=moreNeeded){
			//grab the bytes off the queue
			body=new byte[moreNeeded];
			for (int i = 0; i < moreNeeded; i++) {
				body[i] = queue.poll();
			}
			//reset the state of this method
			needsMore=false;
			moreNeeded=Integer.MAX_VALUE;
			//parse body and return the resulting message
			return parser.addBody(body);
		}
		return null;
	}
	
	//try to stop the reader thread and then join to it
	public void stop() throws InterruptedException{
		thread.setRunning(false);
		t.join();
	}
}
