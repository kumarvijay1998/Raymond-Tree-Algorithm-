import java.util.LinkedList;
import java.util.Queue;
import daj.*;

class Prog extends Program{
	  public int number;
	  public Message msg;
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
	    //initial configuaration of wantToenter  variable and haveToken variable.
	    
	    if(i==0) {
	    	haveToken=true;
	    	wantToenter=true;
	    }
	    else if(i==1) {
	    	wantToenter=true;
	    	//haveToken=true;
	    	
	    }
	    else if(i==2) {
	    	wantToenter=true;
	    }
	    else if(i==3) {
	    	wantToenter=true;
	    }
	  } 
	  
	
	  
	public void main() {
		int outChannels, inChannels, i;
		// TODO Auto-generated method stub
		 GlobalAssertion assertion=new only_one();
		 test(assertion);
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
			if(haveToken) {//Means it is root node
				request_q.add(number);//add to its own queue
				if(request_q.peek()==number) {
					wantToenter=false;
					request_q.remove();
					//Execute CS
					System.out.println("Node "+number+"  is Executing CS");
					if(!request_q.isEmpty()) {
						haveToken=false;
						//send the token to the requesting node and point to that node
						int nextReceiver=request_q.peek();
						request_q.remove();
						int link_number=getLink(number,nextReceiver);
						OutChannel c=msgOut.getChannel(link_number);
						c.send(new Token(number, nextReceiver));
						
						//Update parent of the current node i.e. point to the node which has token 
						if(number==0) {
							node0Parent=nextReceiver;
						}
						else if(number==1) {
							node1Parent=nextReceiver;
						}
						else if(number==2) {
							node2Parent=nextReceiver;
						}
						else if(number==3) {
							node3Parent=nextReceiver;
						}
						
						//If after giving token there is some request left then request token from the node which have token i.e. node number to whome we are sending our token
						if(!request_q.isEmpty()) {
							sentRequest=true;
							int link_number1=getLink(number,nextReceiver);
							//send request now to link number
							 OutChannel c1=msgOut.getChannel(link_number1);
							 c1.send(new Request(number, nextReceiver));
						}
					}
					//wantToenter=false;
				}
				else {
					haveToken=false;
					//send the token to the requesting node and point to that node
					int nextReceiver=request_q.peek();
					request_q.remove();
					int link_number=getLink(number,nextReceiver);
					OutChannel c=msgOut.getChannel(link_number);
					c.send(new Token(number, nextReceiver));
					
					//Update parent of the current node i.e. point to the node which has token 
					if(number==0) {
						node0Parent=nextReceiver;
					}
					else if(number==1) {
						node1Parent=nextReceiver;
					}
					else if(number==2) {
						node2Parent=nextReceiver;
					}
					else if(number==3) {
						node3Parent=nextReceiver;
					}
					
					//If after giving token there is some request left then request token from the node which have token i.e. node number to whome we are sending our token
					if(!request_q.isEmpty()) {
						sentRequest=true;
						int link_number1=getLink(number,nextReceiver);
						//send request now to link number
						 OutChannel c1=msgOut.getChannel(link_number1);
						 c1.send(new Request(number, nextReceiver));
					}
					
				}
				
				
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
				//System.out.println("It is request");
				int sender_number=((Msg) msg).getSender();
				((LinkedList<Integer>) request_q).add(sender_number);
	
				System.out.println("Node"+((Msg) msg).getReceiver()+" Has received msg from "+sender_number);
				if(haveToken) {
					//means it is root node
					//send the token now
					haveToken=false;
					int nextReceiver=request_q.peek();
					//System.out.println("Next receiver"+nextReceiver);
					//Send token to nextReceiver i.e. Top of of the queue
					int link_number=getLink(number,nextReceiver);
					 OutChannel c=msgOut.getChannel(link_number);
					 c.send(new Token(number, nextReceiver));
					 System.out.println("Sending Token from Node  "+number+" to Node "+nextReceiver);
					 request_q.remove();
					 
					 //Modify parent
					 if(number==0) {
							node0Parent=nextReceiver;
					}
					else if(number==1) {
							node1Parent=nextReceiver;
					}
					else if(number==2) {
							node2Parent=nextReceiver;
					}
					else if(number==3) {
							node3Parent=nextReceiver;
					}
					 //Check if more request in the queue
					 if(!request_q.isEmpty()) {
						 //Change parent and send request;
						    sentRequest=true;
							int link_number1=getLink(number,nextReceiver);
							//send request now to link number
							OutChannel c1=msgOut.getChannel(link_number1);
							c1.send(new Request(number, nextReceiver));
						 
						 
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
				System.out.println("Token is receive from"+((Msg) msg).getSender()+" and receiver is"+((Msg) msg).getReceiver());
				if(nextElement==number) {
					haveToken=true;
					wantToenter=false;
					//The sender has received token it can execute now
					System.out.println("Node "+number+" is Executing CS");
					//Task Pending :I have to give visualized msg 
					request_q.remove();
					if(request_q.isEmpty()) {
						//No more request are there
						sentRequest=false;
					}
					else {
						
						int nextReceiver=request_q.peek();
						request_q.remove();
						//send the token to that receiver
						int link_number=getLink(number,nextReceiver);
						OutChannel c=msgOut.getChannel(link_number);
						c.send(new Token(number, nextReceiver));
						haveToken=false;
						//Change the Holder i.e. modify pointer
						if(number==0) {
							node0Parent=nextReceiver;
						}
						else if(number==1) {
							node1Parent=nextReceiver;
						}
						else if(number==2) {
							node2Parent=nextReceiver;
						}
						else if(number==3) {
							node3Parent=nextReceiver;
						}
						
						
						
						
						//Check for new requests
						if(request_q.isEmpty()) {
							//No need to send the request just change the parent
							sentRequest=false;
						}
						else {
							//send request again because the queue is still not empty 
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
					//Change the Holder i.e. modify pointer
					if(number==0) {
						node0Parent=receiver;
					}
					else if(number==1) {
						node1Parent=receiver;
					}
					else if(number==2) {
						node2Parent=receiver;
					}
					else if(number==3) {
						node3Parent=receiver;
					}
					
					
					
					if(request_q.isEmpty()) {
						//No need to send the request just change the parent
						//Task Pending:Modify the parent
						sentRequest=false;
					}
					else {
						//send request again because the queue is still not empty 
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
		//System.out.println("sender "+number2+" receiver"+parent);
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
		return "Node is Executing CS "+haveToken;
	  }
}



class only_one extends GlobalAssertion {
    public String getText() {
        return("More than 1 process entered the critical region!");
    }

	@Override
	public boolean test(Program prog[]) {
		// TODO Auto-generated method stub
		 int count=0;
       for (int j=0; j<prog.length; j++) {
           if (((Prog)prog[j]).haveToken) count++;
       }
       return(count<=1);
	}
}