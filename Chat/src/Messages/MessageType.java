	import java.util.Arrays;
	import java.util.Collections;
	import java.util.List;

	public final class MessageType implements Comparable{
		
		//Define 14 different messages.
		
		public static final byte CODE_DCPBROADCAST = 1;
		
		public static final byte CODE_DCPSERVERHANDSHAKE = 2;
		
		public static final byte CODE_DCPCLIENTHANDSHAKE = 3;
		
		public static final byte CODE_DCPSERVERACCEPT = 4;	
		
		public static final byte CODE_DCPCLIENTACCEPT = 5;
		
		public static final byte CODE_DCPCLIENTREQUESTUPDATE = 6;
		
		public static final byte CODE_DCPSERVERCONFIRMUPDATE = 7;
		
		public static final byte CODE_DCPCLIENTREQUESTINFO = 8;
		
		public static final byte CODE_DCPSERVERSNEDSINFO = 9;
		
		public static final byte CODE_DCPDECLINECONNECTION = 10;
		
		public static final byte CODE_DCPCHATMESSAGE = 11;
		
		public static final byte CODE_DCPLOOKUPFAILED = 12;
		
		public static final byte CODE_DCPERROR = 13;
		
		public static final byte CODE_DCPNAMECOLLISION = 14;
		
		//Enumeration elements are constructed once upon class loading
		
		public static final MessageType UDPbroadcast = new MessageType(CODE_DCPBROADCAST, "UDP broadcast");
		
		public static final MessageType ServerHandShake = new MessageType(CODE_DCPSERVERHANDSHAKE, "Server hand shake");
		
		public static final MessageType ClientHandShake = new MessageType(CODE_DCPCLIENTHANDSHAKE, "Client hand shake");
		
		public static final MessageType ServerAccept = new MessageType(CODE_DCPSERVERACCEPT, "Server Accept");
		
		public static final MessageType ClientAccept = new MessageType(CODE_DCPCLIENTACCEPT, "Client Accept");
		
		public static final MessageType ClientRequestUpdate = new MessageType(CODE_DCPCLIENTREQUESTUPDATE, "Client request update");
		
		public static final MessageType ServerConfirmationUpdate = new MessageType(CODE_DCPSERVERCONFIRMUPDATE, "Server Confirmation of update");
		
		public static final MessageType ClientRequestInfo = new MessageType(CODE_DCPCLIENTREQUESTINFO, "Clinet request info");
		
		public static final MessageType ServerSendsInfo = new MessageType(CODE_DCPSERVERSNEDSINFO, "Server sends info");
		
		public static final MessageType DeclineConnectionMessage = new MessageType(CODE_DCPDECLINECONNECTION, "Declien current Message");
		
		public static final MessageType ChatMessage = new MessageType(CODE_DCPCHATMESSAGE, "Chat message");
		
		public static final MessageType LookupFailed = new MessageType(CODE_DCPLOOKUPFAILED, "Lookup failed");
		
		public static final MessageType Error = new MessageType(CODE_DCPERROR, "Error");
		
		public static final MessageType NameCollision = new MessageType(CODE_DCPNAMECOLLISION, "Name collision");
		
		public String toString(){
			return name;
		}
		
		public int compareTo(Object that){
			return ordinal -((MessageType)that).ordinal;
		}
		
		public static MessageType getTypeByCode( byte type )
	    {
	        for ( int ii = 0; ii < values.length; ii++ )
	            if ( values[ii].ordinal == type )
	                return values[ii];
	        return new MessageType( type, "Unrecognized" );
	    }
		
		public byte getCode(){
			return ordinal;
		}
		
		//private
		
		private final String name;
		
		private final byte ordinal;
		
		private MessageType(byte ordinal, String name){
			this.ordinal = ordinal;
			this.name = name;
		}
		
		private static final MessageType[]values =
			{UDPbroadcast, ServerHandShake, ClientHandShake, ServerAccept, ClientAccept, ClientRequestUpdate, 
			ServerConfirmationUpdate, ClientRequestInfo, ServerSendsInfo, DeclineConnectionMessage, ChatMessage, 
			LookupFailed, Error, NameCollision};
		
		
		
		public static final List<MessageType> VALUES = Collections.unmodifiableList(Arrays.asList( values ));
	}
