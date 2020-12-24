public class Token extends Msg {


protected boolean havetoken=false;

public Token (int sender, int receiver) {

super(sender, receiver);

this.havetoken=true;

}




public String getText () {

return "Token from : " + sender + " to : " +

receiver;

}

} // Token

