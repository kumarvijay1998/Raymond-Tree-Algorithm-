public class AProg extends daj.Program {


private int progID, curDir;

private boolean haveToken, sentRequest;

private InChannelSet msgIn;

private OutChannelSet msgOut;

private FIFO map;

private Random rand;


// Constructor

public AProg (int progID, int curDir,

boolean startWithToken) {

this.progID = progID;

if (haveToken = startWithToken) {

this.curDir = progID;

}

else {

this.curDir = curDir;

}

msgIn = new InChannelSet();

msgOut = new OutChannelSet();

rand = new Random();

map = new FIFO();

sentRequest = false;

}



public String getText() {

return "curDir : " + curDir + " have token : " +

haveToken + " sent request : " + sentRequest;

} // getText



// Main loop of the Node

public void main() {

int fromChannel, outChannels, inChannels, i;

Msg msg;

FIFO tokenMap;


inChannels = in().getSize();

for (i=0; i<inChannels; i++) {

msgIn.addChannel(in(i));

}

outChannels = out().getSize();

for (i=0; i<outChannels; i++) {

msgOut.addChannel(out(i));

}


// Program main loop

while(true) {

if (haveToken) {

/*

Enter critical section ...

*/

// give token to the next "requester"

if ((i = map.getNextReceiver()) >= 0) {

haveToken = false;

msgOut.getChannel(i).send(new Token(progID, i,

map));

map = new FIFO();

curDir = i;

}

}

if (!haveToken && !sentRequest) {

// request Token

msgOut.getChannel(curDir).send(new

Request(progID, curDir));

curDir = progID;

sentRequest = true;

}

fromChannel=msgIn.select();

msg = (Msg)msgIn.getChannel(fromChannel).receive();

if (msg.getReceiver() == progID) {

if (msg instanceof Request) {

if (curDir == progID) {

// add the request to the local map

map.addToFIFO(msg.getSender());

}

else {

msgOut.getChannel(curDir).send(new

// fwd the request

Request(i=msg.getSender(), curDir));

curDir = i;

msg = null;

}

}

else { // Token

haveToken = true;

sentRequest = false;

// merge the local and the tokenMap;

tokenMap = ((Token)msg).getMap();

while ((i=map.getNextReceiver()) >= 0) {

tokenMap.addToFIFO(i);

}

map = tokenMap;

tokenMap = null;

msg = null;

}

} // if (msg.getReceiver() == progID)

else {

// should never be reached

System.out.println("Error ! Got Msg from : " +

msg.getSender() + " wanted receiver : " +

msg.getReceiver());

}

} // while (true) main loop

} // main

} // AProg


