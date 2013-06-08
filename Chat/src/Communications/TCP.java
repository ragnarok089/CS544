/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is a wrapper for all of our TCP communications.
 * 	It is a thread that when run listens for connections to be made
 *  It can also send Messages or byte[]. It kicks off a child thread
 *  that listens for data on the socket and puts it into a queue.
 *  Since you have to read the header in order to determine how much
 *  to read for the body, it requires 2 passes for a read. The first
 *  time it merely checks if there are enough bits for the header
 *  and if there are reads them and processes them. It then
 *  will check if there enough bits for the body. If there are not
 *  it continues and the next time it checks, it skips right to
 *  the body.
 */
package Communications;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;

import Messages.ErrorMessage;
import Messages.Message;
import Utilities.Parser;


public class TCP implements Runnable {
	public Socket socket=null;//the socket for this connection
    public TCPReceiverThread tr=null;//the runnable that reads from the socket
    Thread t=null;//the thread version of tr
    ConcurrentLinkedQueue<Byte> queue=new ConcurrentLinkedQueue<Byte>();//The queue that passes data from the reader thread to here
    public boolean active;//indicates whether the connection is active
    int size=Message.minSize;//the size of the header
    int moreNeeded=Integer.MAX_VALUE;//the number of bits that are needed for the body
    boolean needsMore=false;//whether the header has been read or not
    byte[] current=null;//storage for reading
    byte[] body=null;
    Parser parser=new Parser();//Parser that converts byte[] to messages
	public String pendingIP="";//storage
	public ServerSocket serverSocket=null;//The server socket for the listener
	public InetAddress ip=null;//the ip
	boolean running=true;//whether the thread whould run or not
	public boolean initiator=false;//stores whether this side initiated
    
    public TCP(){
    	//This block attempts to find a connection with an ip starting with 192.168.224.
    	//We were getting strange errors because tux was trying to connect on invalid
    	//connects so we added this in. Unfortunately, it stops it from working on
    	//anything but tux
		try {
			//gather up all the interfaces
			InetAddress address = null;
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			//look through the interfaces until you find the one you want
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback())
					continue; // Don't want to broadcast to the loopback
								// interface
				Enumeration<InetAddress> addresses = networkInterface
						.getInetAddresses();
				//Look through all the addresses on this interface
				while (addresses.hasMoreElements()) {
					address = addresses.nextElement();
					//if it contains 192.168.224 stop looking
					if (!address.getHostAddress().contains("192.168.224"))
						continue;
					break;
				}
				//if it contains 192.168.224 stop looking
				if (address != null && address.toString().contains("192.168.224")) {
					break;
				}
			}
			ip=address;//store the ip
			//initialize a new server thread connected to your queue
			tr = new TCPReceiverThread(queue);
		}
    	catch(Exception e){
    		System.out.println("Could not set up TCP");
    		System.exit(-1);
    	}
    }
    
    //returns true if both the socket is active and the boject thinks that it should be active
    public boolean getActive(){
    	return active && (socket!=null && !socket.isClosed());
    }
    
    //this is the method that get run if this is converted to a thread
    //It constantly listens on the serversocket and establishes a connection
    //if there isn't one already. If there is one, the new one is immediately closed
	public void run(){
		//socket 2 is the local socket that is made
		Socket socket2 = null;
		boolean error = false;
		try {
			//tries to make a new server socket and if it can't it stops running
			serverSocket = new ServerSocket(12345, 0, ip);
			serverSocket.setSoTimeout(500);
		} catch (Exception e) {
			System.out.println("Could not open TCP port");
			running=false;
		}
		//keeps running until running is set to false
		while (running) {
			if (!getActive()) {
				try {
					//listen for a new connection
					socket2 = serverSocket.accept();
					error = false;
				} catch (Exception e) {
					//timeout
					error = true;
				}
				//if there still isn't an active one and there wasn't a timeout
				if (!getActive() && !error) {
					//initialize variables
					initiator = false;
					socket = socket2;
					socket2=null;
					active = true;
					tr.setSocket(socket);
					//start listener on new socket
					t = new Thread(tr);
					t.start();
				} else {
					//if there is already an active socket close the new one
					try {
						socket2.close();
					} catch (Exception e) {}
				}
			}
			try {
				//if there is already one up just wait for .5 seconds
				Thread.sleep(500);
			} catch (Exception e) {}
		}
		//cleanup
		try {
			serverSocket.close();
		} catch (IOException e) {}
	}
	
	//stops the run method from continuing to loop
	public void stop(){
		running=false;
	}

	//Attempts to establish a conection with the target
	//can either be an IP string or a hostname
	public int connect(String target){
		try {
			//if there's already an active connection do nothing
			if(getActive()){
				return -1;
			}
			//make the connection
			socket = new Socket(target, 12345);
			//set up variables
			initiator=true;
			active=true;
			//set up the new reader
			tr.setSocket(socket);
            t=new Thread(tr);
            //kick off the new reader
            t.start();
		} catch (UnknownHostException e) {
			return -1;
		} catch (IOException e) {
			return -2;
		}
		return 0;
	}
	
	//sends a stream of bytes over the tcp connection
	public int send(byte[] message) {
		try {
			//get the network stream
			OutputStream socketOutputStream = socket.getOutputStream();
			//write the array to the network stream
			socketOutputStream.write(message, 0, message.length);
		} catch (Exception e) {
			//if there was a problem with the connection
			return -1;
		}
		return 0;
	}
	//converts the message to a stream of bytes and then sends it over the tcp socket
	public int send(Message message) {
		try {
			//get the network stream
			OutputStream socketOutputStream = socket.getOutputStream();
			//convert the message to bytes
			byte[] buffer = message.convert();
			//write the bytes to the buffer
			socketOutputStream.write(buffer, 0, buffer.length);
		} catch (Exception e) {
			//if there is a problem with the stream
			return -1;
		}
		return 0;
	 }
	
	//This reads data from the reader and passes it out as a message
	//Since you can only tell how many bytes are needed after reading some of them
	//it has to take 2 passes. On the first pass it reads the header and reads the
	//body if there are enough bytes. If there are not enough bytes it preserves its state
	//and the next time it is called tries to read the body of the message
	public Message read(){
		//if there isn't a message half constructed
		if (!needsMore) {
			//check if there is enough for a header
			if (queue.size() >= size) {
				//grab the header bytes from the queue
				current = new byte[size];
				for (int i = 0; i < size; i++) {
					current[i] = queue.poll();
				}
				//place them in the parser which stores the message
				//and returns the number of bytes in the body
				moreNeeded=parser.parse(current);
				//if it returns -1 there was an error
				//so stop the reader clear the queue and return an error message
				if(0>moreNeeded){
					System.out.println("Parser Error");
					tr.stop();
					queue.clear();
					return new ErrorMessage();
				}
				//if there are enough bytes for the body
				if(queue.size()>=moreNeeded){
					//grab the bytes from the queue
					body=new byte[moreNeeded];
					for (int i = 0; i < moreNeeded; i++) {
						body[i] = queue.poll();
					}
					//reset the state of this method
					needsMore=false;
					moreNeeded=Integer.MAX_VALUE;
					//construct and return the new message
					return parser.addBody(body);
				}
				//if there weren't enough bytes return null and store the state
				needsMore=true;
				return null;
			} else {
				//if there weren't enough bits for a header just return null
				return null;
			}
		}
		//if the header has already been processed and there are enough bytes
		else if(queue.size()>=moreNeeded){
			//grab bytes from the queue
			body=new byte[moreNeeded];
			for (int i = 0; i < moreNeeded; i++) {
				body[i] = queue.poll();
			}
			//reset the state of this method
			needsMore=false;
			moreNeeded=Integer.MAX_VALUE;
			//construct and return the new Message
			return parser.addBody(body);
		}
		//if not enough bits just return null
		return null;
	}
	
	//returns the ip address that the serversocket is listening on
	public String getIP(){
		return serverSocket.getInetAddress().getHostAddress();
	}
	
	//attempts to close the tcp connection
	public void close() throws IOException{
		active=false;
		tr.stop();//stop the reader
		try {
			t.join();
		} catch (Exception e) {}
		queue.clear();//clear the queue
		socket.close();//close the socket
	}
}
