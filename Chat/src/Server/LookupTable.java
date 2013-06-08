/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the lookup table for the server that stores the bindings of the usernames
 * 	and ip addresses
 */
package Server;

import java.util.HashMap;

public class LookupTable {
	//the table that stores the bindings
	static private HashMap<String,String> map=new HashMap<String,String>();
	
	//a method that given a name returns the ip address (or null if unbound)
	static String lookup(String name){
		return map.get(name);
	}
	
	//a method that binds the name to the given ip
	static void bind(String name,String ip){
		map.put(name, ip);
	}
}
