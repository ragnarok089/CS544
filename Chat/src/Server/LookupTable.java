package Server;

import java.util.HashMap;

public class LookupTable {
	static private HashMap<String,String> map=new HashMap<String,String>();
	
	static String lookup(String name){
		return map.get(name);
	}
	static void bind(String name,String ip){
		map.put(name, ip);
	}
}
