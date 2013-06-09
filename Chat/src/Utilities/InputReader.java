/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This thread reads from STDIN, and makes the results
 * 	available for the client to access.
 */
package Utilities;
import java.util.*;

//UI
public class InputReader implements Runnable {
	Scanner sc;//the scanner 
	String ready; //the last string collected
	boolean done;
	public InputReader(){
		sc=new Scanner(System.in);//setup the scanner
		done=false;
		ready="";
	}
	
	//returns the ready string in a thread safe way
	public String getSubmitted(){
		String store=getSetReady("");
		return store;
	}
	
	//tells the thread to stop
	public void stop(){
		done=true;
		sc.close();
	}
	
	//this allows the thread and any outside threads to access
	//the ready variable in a threadsafe way
	//it returns the current string and makes the new value the passed string
	private synchronized String getSetReady(String newReady){
		String store=ready;
		ready=newReady;
		return store;
		
	}
	
	//the main loop of the thread
	public void run() {
		try{
			while (!done) {
				//if there is another line ready
				if (sc.hasNextLine()) {
					//read it
					getSetReady(sc.nextLine());
				} else {
					try {
						//sleep for .01 of a second
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
				}
			}
		} catch (IllegalStateException e) {}
	}
}
