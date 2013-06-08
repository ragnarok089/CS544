/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This stores the username
 */
package Utilities;

public class User {
	public static String userName="Unknown";//username defaulting to "Unknown"
	//getter for username
	public static String getUserName(){
		return userName;
	}
	//setter for username
	public static void setUserName(String _userName){
		userName=_userName;
	}
}
