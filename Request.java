public class Request extends Msg {


public Request (int sender, int receiver) {

super(sender, receiver);

}


public String getText () {

return "Request from : " + sender + " to : " +

receiver;

}

} // Request