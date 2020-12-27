import java.util.LinkedList;
import java.util.Queue;
import daj.*;

class Prog extends Program{
	  public int number;
	  public Message msg;
	 // public int Holder;//It will point to parent on the path to the root
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
	    	//Holder=0;
	    	haveToken=true;
	    	sentRequest=true;
	    	request_q.add(number);
	    	
	    }
	    else if(i==1) {
	    	//Holder=0;
	    	
	    	
	    }
	    else if(i==2) {
	    	//Holder=0;
	    }
	    else if(i==3) {
	    	//Holder=1;
	    	wantToenter=true;
	    }
	  } 
	  
	
	  
	public void main() {
		int outChannels, inChannels, i;
		// TODO Auto-generated method stub
		for(int j=0;j<4;j++) {
		
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
				System.out.println("It is token");
				int nextElement=request_q.peek();
				System.out.println("Token is receive from"+((Msg) msg).getSender()+" and receiver is"+((Msg) msg).getReceiver());
				if(nextElement==number) {
					//The sender has received token it can execute now
					System.out.println("Node "+number+" is Executing CS");
					//Task Pending :I have to give visualized msg 
					request_q.remove();
					haveToken=true;
					if(request_q.isEmpty()) {
						//No more request are there
						sentRequest=false;
					}
					else {
						int nextReceiver=request_q.peek();
						//send the token to that receiver
						int link_number=getLink(number,nextReceiver);
						OutChannel c=msgOut.getChannel(link_number);
						c.send(new Token(number, nextReceiver));
						if(request_q.isEmpty()) {
							//No need to send the request just change the parent
							//Task Pending:Modify the parent
							sentRequest=false;
						}
						else {
							//send request again because the queue is not empty still
							sentRequest=true;
							int parent=nextReceiver;
							//get link to parent
							int link_number1=getLink(number,parent);
							//send request now to link number
							 OutChannel c1=msgOut.getChannel(link_number1);
							 c1.send(new Request(number, parent));
						}
					}
				}
				else {
					//Token is received but the node is not on top of queue so send it to the requestor
					int receiver=request_q.peek();
					request_q.remove();
					int link_number=getLink(number,receiver);
					OutChannel c=msgOut.getChannel(link_number);
					c.send(new Token(number, receiver));
					if(request_q.isEmpty()) {
						//No need to send the request just change the parent
						//Task Pending:Modify the parent
						sentRequest=false;
					}
					else {
						//send request again because the queue is not empty still
						sentRequest=true;
						int parent=receiver;
						//get link to parent
						int link_number1=getLink(number,parent);
						//send request now to link number
						 OutChannel c1=msgOut.getChannel(link_number1);
						 c1.send(new Request(number, parent));
					}
				}
				
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
		return "This part is yet to be implemented";
	    
//	    if (msg == null)
//	      msgString = "(null)";
//	    else
//	      msgString = msg.getText();
//		 if(haveToken) {
//			 return "Node "+number+" is Executing CS";
//		 }else {
//			 return " Request to "+((Msg) msg).getReceiver() +" is send";
//		 }
	  }
}