// ----------------------------------------------------------------------------
//
// a program class
//
// ----------------------------------------------------------------------------
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import daj.*;
class Prog extends Program
{
  private int number;
  
  public Message msg;
  public int Holder;//It will point to parent on the path to the root
  public boolean sentRequest;//The node has sent request or not to parent for token access
  Queue<Integer> request_q = new LinkedList<>();//Each node has fifo queue
  public boolean haveToken=false; //false-don't have token; true-has token
  private InChannelSet msgIn;
  private OutChannelSet msgOut;
  
  // --------------------------------------------------------------------------
  // called for	initialization of program
  // --------------------------------------------------------------------------
  public Prog(int i)
  { 
    number = i;	
    msg = null;
    sentRequest=false;
    haveToken=false;
    msgIn = new InChannelSet();
    msgOut = new OutChannelSet();
    
    //initial configuaration of holder variable.
    
    if(i==0) {
    	Holder=0;
    	haveToken=true;
    	sentRequest=true;
    	request_q.add(number);
    }
    else if(i==1) {
    	Holder=0;
    }
    else if(i==2) {
    	Holder=0;
    }
    else if(i==3) {
    	Holder=1;
    }
  } 
 
  // --------------------------------------------------------------------------
  // called for	 execution of program
  // --------------------------------------------------------------------------
  public void send_request(int sendingnode,int parentnode) {
	  System.out.println("Request is received from node "+sendingnode+" and its parent node is "+parentnode);
	  OutChannel c=msgOut.getChannel(Holder);
	  System.out.println("c="+c);
	  c.send(new Request(sendingnode, parentnode));
	  //send(new Request(sendingnode, parentnode));
  }
//  public void get_request(int sendingnode,int receivingnode) {
//	  
//  }
  public void main()
  { 
		int fromChannel, outChannels, inChannels, i;
	  	Msg msg;


	  inChannels = in().getSize();
	  System.out.println(number+"inchannels="+inChannels);
	  for (i=0; i<inChannels; i++) {
		  msgIn.addChannel(in(i));
		//  System.out.println("Node"+number+"Inchannels"+msgIn.getChannel(i));
	  }
	  
	  outChannels = out().getSize();

	  System.out.println(number+"outchannels="+outChannels);

	  for (i=0; i<outChannels; i++) {
	  msgOut.addChannel(out(i));
	 // System.out.println("Node "+number +"Outchannel "+msgOut.getChannel(i));
	  }
    
    while(true) {
    	
    	
    	
    	
    	System.out.println("node "+number+"have token"+haveToken+" and sent Request"+sentRequest);
    	if(haveToken&&sentRequest) {
    		
    		//check whether the top is same as the node otherwise send this token to the top
    		System.out.println("peek of "+number+" is "+request_q.peek());
    		if(request_q.isEmpty()||(request_q.peek()==number)) {
    			//node itself will execute
    			request_q.remove();
    			haveToken=false;
    			sentRequest=false;
    			System.out.println("This Node will now USE CS");
    			/*

        		Enter critical section ...

        		*/
    			// give token to the next "requester"
        		//Fetch the next request and send the token to that node from the queue
    			if(!request_q.isEmpty()) {
        		int nextReceiver=request_q.remove(); 
        		//send token to nextReceiver
        		msgOut.getChannel(i).send(new Token(number,nextReceiver));
        		haveToken=false;
        		if(!request_q.isEmpty()) {
        			//send request and set holder point to that node
        			Holder=nextReceiver;
        			sentRequest=true;
        			send_request(number,Holder);
        		}
    			}else {
    				System.out.println("Hello Test");
    				haveToken=true;
    			}
    			
    			
    			
    		}else {
    			int requestornode=request_q.remove();
    			msgOut.getChannel(i).send(new Token(number,requestornode));
    			haveToken=false;
    			if(!request_q.isEmpty()) {
        			//send request and set holder point to that node
        			Holder=requestornode;
        			sentRequest=true;
        			send_request(number,Holder);
        		}
    		}
    		
    		
    		//break;
    		

    	}
    	else if(!haveToken && !sentRequest) {
    		//send request to parent i.e. where holder is pointing to
    		//sendRequest(nodeid,holder);
    		System.out.println("node "+number+"Holder="+Holder);
    		sentRequest=true;
    		request_q.add(number);
    		send_request(number,Holder);
    	}
    	System.out.println("Going to block");
    	fromChannel=msgIn.select();
    	System.out.println("fromChannel "+fromChannel);
    	msg = (Msg)msgIn.getChannel(fromChannel).receive();
    	System.out.println("Released");
    	System.out.println(msg);
    	
    }
  
  }

  // --------------------------------------------------------------------------
  // called for	display of program state
  // --------------------------------------------------------------------------
  public String getText()
  {
    String msgString;
    if (msg == null)
      msgString = "(null)";
    else
      msgString = msg.getText();
    return 
      "Sent request from "+number+" to parent node"+Holder;
  }
}