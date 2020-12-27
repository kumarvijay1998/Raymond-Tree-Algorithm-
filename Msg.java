


public class Msg extends daj.Message

{

protected int sender, receiver;


public Msg (int sender, int receiver) {

this.sender = sender;

this.receiver = receiver;

}


public String getText () {

return "Message from : " + sender + " to : " +

receiver;

}


public int getSender() {

return sender;

}


public int getReceiver() {

return receiver;

}

} // Msg