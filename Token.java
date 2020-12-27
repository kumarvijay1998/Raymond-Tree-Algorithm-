public class Token extends Msg {

//protected FIFO map;


public Token (int sender, int receiver) {

super(sender, receiver);

//this.map = map;

}


//public FIFO getMap() {
//
//return map;
//
//}


public String getText () {

return "Token from : " + sender + " to : " +

receiver;

}

} // Token