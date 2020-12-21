// ----------------------------------------------------------------------------
//
// a program class
//
// ----------------------------------------------------------------------------
import java.util.PriorityQueue;
import java.util.Queue;

import daj.*;
class Prog extends Program
{
  private int number;
  public int index;
  public Message msg;
  public boolean sent;
  public Queue<Integer> request_q = new PriorityQueue<Integer>();//Each node has fifo queue
  public int Holder;//It will point to parent on the path to the root
  public boolean Request=false;//The node has sent request or not to parent for token access
  public boolean Token=false; //false-don't have toke; true-has token
  
  // --------------------------------------------------------------------------
  // called for	initialization of program
  // --------------------------------------------------------------------------
  public Prog(int i)
  { 
    number = i;	
    msg = null;
    sent = false;
    //initial configuaration of holder variable.
    if(i==0) {
    	Holder=0;
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
  public void main()
  { 
    if (number == 0)
      {
//	out(0).send(new Msg(0));
//	out(1).send(new Msg(1));
//	sent = true;
     }
    
    //GlobalAssertion assertion = new NumberOfMessages();
    for (int i = 0; i < 5; i++)
      {
	//test(assertion);
	index = in().select();
	msg = in(index).receive();
	out(index).send(msg);
	msg = null;
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
      "sent: " + String.valueOf(sent) +
      "\nmsg: " + msgString;
  }
}