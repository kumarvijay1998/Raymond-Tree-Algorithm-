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
  public int index;
  public Message msg;
  public int Holder;//It will point to parent on the path to the root
  public boolean sentRequest;//The node has sent request or not to parent for token access
  Queue<Integer> request_q = new LinkedList<>();//Each node has fifo queue
  public static boolean haveToken=false; //false-don't have token; true-has token
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
   
    msgIn = new InChannelSet();
    msgOut = new OutChannelSet();
    
    //initial configuaration of holder variable.
    
    if(i==0) {
    	Holder=0;
    	haveToken=true;
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
	  msgOut.getChannel(Holder).send(new Request(number, Holder));
  }
//  public void get_request(int sendingnode,int receivingnode) {
//	  
//  }
  public void main()
  { 
		int fromChannel, outChannels, inChannels, i;
	  	Msg msg;


	  inChannels = in().getSize();

	  for (i=0; i<inChannels; i++) {
		  msgIn.addChannel(in(i));
	  }
	  
	  outChannels = out().getSize();

	  for (i=0; i<outChannels; i++) {

	  msgOut.addChannel(out(i));

	  }
    
    while(true) {
    	if(haveToken) {
    		
    		//check whether the top is same as the node otherwise send this token to the top
    		if(request_q.isEmpty()||(request_q.peek()==number)) {
    			//node itself will execute
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
    			}
    			
    			
    		}else {
    			int requestornode=request_q.remove();
    			msgOut.getChannel(i).send(new Token(number,requestornode));
    			haveToken=false;
    		}
    		
    		
    		//break;
    		

    	}
    	else if(!haveToken && !sentRequest) {
    		//send request to parent i.e. where holder is pointing to
    		//sendRequest(nodeid,holder);
    		sentRequest=true;
    		request_q.add(number);
    		send_request(number,Holder);
    		break;
    	
    		
    	}
    	fromChannel=msgIn.select();

    	msg = (Msg)msgIn.getChannel(fromChannel).receive();
    	if (msg.getReceiver() == number) {
    		if (msg instanceof Request) {
    			//add sender to queue
        		request_q.add(number);
        		//if no request sent then send
        		if(number==Holder&&haveToken) {
        			//give token directly to the sender && set holder to point to that node 
        			Holder=msg.getSender();
        			if(!request_q.isEmpty()) {
        				send_request(number,Holder);
        			}
        			
        		}
        		else if(!sentRequest) {
        			send_request(number,Holder);
        		}else {
        			request_q.add(msg.getSender());
        		}
    		}else {
    			haveToken = true;
    			sentRequest = false;
    		}
    		
    	}
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
     // "sent: " + String.valueOf(sent) +
      "\nmsg: " + msgString;
  }
}