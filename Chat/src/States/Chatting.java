/*	CS 544
 * 	20130608
 * 	Kyle P., Gabriel S., Mengchen Z., Sid L.
 * 
 * 	This is the state for the client in which the user can communicate with the
 * 	other side by typing normally into STDIN.
 */
package States;
import Communications.TCP;
import Communications.UDPSender;
import Messages.ChatMsgMessage;
import Messages.ErrorMessage;
import Messages.Message;

//STATEFUL
public class Chatting extends State{
	
	//This method processes the input and returns the next state
	public State process(String input, TCP tcp, UDPSender us,Message udpMessage,Message tcpMessage,long timeEnteredState,boolean firstCall){
		if(firstCall){
			//if its the first time in this state print this message
			System.out.println("You can now chat with the other person");
		}
		//if the connection died
		if(tcp.getActive()==false){
			System.out.println("The other side disconnected");
			try{
				tcp.close();
			}
			catch(Exception e){}
			return new Disconnected();
		}
		//if you give any command not already cuaght
		else if(input.startsWith(":")){
			System.out.println("Invalid command");
			return this;
		}
		//if there is a message send it over tcp
		else if(!input.equals("")){
			Message message=new ChatMsgMessage(11,Message.minSize+Message.minSize,0,"",input);
			tcp.send(message);
			return this;
		}
		//if you receive a message print out the text
		else if(tcpMessage instanceof ChatMsgMessage && tcpMessage.getCorrect()){
			System.out.println("Your interlocutor says: \""+((ChatMsgMessage)tcpMessage).messages+"\"");
			return this;
		}
		//if you recieve any other kind of message, send an error message
		else if (tcpMessage != null) {
			tcp.send(new ErrorMessage(13,Message.minSize,0,"",new byte[0]));
			return this;
		}
		else{
			return this;
		}
	}
}
