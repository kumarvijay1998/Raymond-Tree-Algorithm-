import java.util.LinkedList;
import java.util.Queue;
import daj.*;

class Prog extends Program{
	  public int number;
	  public Message msg;
	  public int Holder;//It will point to parent on the path to the root
	  public boolean sentRequest;//The node has sent request or not to parent for token access
	  public Queue<Integer> request_q;//Each node has fifo queue
	  public boolean haveToken=false; //false-don't have token; true-has token
	  private InChannelSet msgIn;
	  private OutChannelSet msgOut;
	  public boolean wantToenter;
	  private int node0Parent=0;
	  private int node1Parent=0;
	  private int node2Parent=0;
	  private int node3Parent=1;
	  
	  public Prog(int i)
	  { 
	    number = i;	
	    msg = null;
	    sentRequest=false;
	    wantToenter=false;
	    haveToken=false;
	    msgIn = new InChannelSet();
	    msgOut = new OutChannelSet();
	    request_q= new LinkedList<>();
	    //initial configuaration of holder variable.
	    
	    if(i==0) {
	    	Holder=0;
	    	haveToken=true;
	    	sentRequest=true;
	    	request_q.add(number);
	    	
	    }
	    else if(i==1) {
	    	Holder=0;
	    	wantToenter=true;
	    	
	    }
	    else if(i==2) {
	    	Holder=0;
	    	
	    }
	    else if(i==3) {
	    	Holder=1;
	    	
	    }
	  } 
	  
	
	  
	public void main() {
		// TODO Auto-generated method stub
		int outChannels, inChannels, i;
		inChannels = in().getSize();
		outChannels = out().getSize();
		for (i=0; i<inChannels; i++) {
			  msgIn.addChannel(in(i));
     	}
		 for (i=0; i<outChannels; i++) {
			  msgOut.addChannel(out(i));
		}
		 
		if(wantToenter) {
			//send request to parent
//			 OutChannel c=msgOut.getChannel(0);
//			 c.send(new Request(number, 0));
			if(haveToken) {
				//Execute CS
				wantToenter=false;
			}
			else {
				if(sentRequest==false) {
					//add itself into its queue and send request to parent
					((LinkedList<Integer>) request_q).push(number);
					sentRequest=true;
					int parent=getParent(number);
					//get link to parent
					int link_number=getLink(number,parent);
					//send request now to link number
					 OutChannel c=msgOut.getChannel(link_number);
					 c.send(new Request(number, parent));
					
				}
			}
		}
		
		
		//Code to Handle the request
		int fromChannel=msgIn.select();
		msg = (Msg)msgIn.getChannel(fromChannel).receive();
		if(((Msg) msg).getReceiver()==number) {
			//Check whether it is request or token
			if (msg instanceof Request){
				System.out.println("It is request");
				int sender_number=((Msg) msg).getSender();
				((LinkedList<Integer>) request_q).push(sender_number);
	
				System.out.println("Node"+((Msg) msg).getReceiver()+" Has received msg from "+sender_number);
				if(haveToken) {
					//means it is root node
					//send the token now
					int nextReceiver=request_q.peek();
					//System.out.println("Next receiver"+nextReceiver);
					//Send token to nextReceiver i.e. Top of of the queue
					int link_number=getLink(number,nextReceiver);
					 OutChannel c=msgOut.getChannel(link_number);
					 c.send(new Token(number, nextReceiver));
					 
					 request_q.remove();
					 //Check if more request in the queue
					 if(!request_q.isEmpty()) {
						 
					 }
					 
				}
				else {
					//non root node
					if(sentRequest) {
						//already sent request do nothing
					}
					else {
						//send request to parent
						sentRequest=true;
						int parent=getParent(number);
						//get link to parent
						int link_number=getLink(number,parent);
						//send request now to link number
						 OutChannel c=msgOut.getChannel(link_number);
						 c.send(new Request(number, parent));
						
					}
					
				}
			}
			else {
				//System.out.println("It is token");
				int nextElement=request_q.peek();
				if(nextElement==number) {
					//The sender has received token it can execute now
					System.out.println("Node "+number+" is Executing CS");
					haveToken=true;
				}
				
			}
		}
		 
	}



	private int getLink(int number2, int parent) {
		// TODO Auto-generated method stub
		if(number2==0) {
			if(parent==1) {
				return 0;
			}
			else if(parent==2) {
				return 1;
			}
		}
		else if(number2==1) {
			if(parent==0) {
				return 0;
			}
			else if(parent==3) {
				return 1;
			}
		}
		else if(number2==2) {
			if(parent==0) {
				return 0;
			}
		}
		else if(number2==3) {
			if(parent==1) {
				return 0;
			}
		}
		return -1;
	}



	private int getParent(int number2) {
		if(number2==0) {
			
			return node0Parent;
		}
		else if(number2==1) {
			return node1Parent;
		}
		else if(number2==2) {
			return node2Parent;
		}
		else if(number2==3) {
			return node3Parent;
		}
		return -1;
	}
	 public String getText()
	  {
	    
	    if (msg == null)
	      msgString = "(null)";
	    else
	      msgString = msg.getText();
	    return 
	      "\nmsg: " + msgString;
	  }
}