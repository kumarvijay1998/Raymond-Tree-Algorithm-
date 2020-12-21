// --------------------------------------------------------------------------
// $Id: algorithm.tex,v 1.2 1997/11/06 14:37:28 schreine Exp schreine $
// a trivial test application
//
// (c) 1997, Wolfgang Schreiner <Wolfgang.Schreiner@risc.uni-linz.ac.at>
// http://www.risc.uni-linz.ac.at/software/daj
// --------------------------------------------------------------------------
import daj.*;

// ----------------------------------------------------------------------------
//
// the application itself
//
// ----------------------------------------------------------------------------
public class Main extends Application
{
  // --------------------------------------------------------------------------
  // main function of application
  // --------------------------------------------------------------------------
	public daj.Node[] nodes;
	public static final int nrOfProgs = 3;
	public AProg[] progs;
  public static void main(String[] args)
  {
    new Main().run();
  }

  // --------------------------------------------------------------------------
  // constructor for application
  // --------------------------------------------------------------------------
  public Main()
  {
    super("A Raymond Tree Algorithm", 400, 300);
  }

  // --------------------------------------------------------------------------
  // construction of network
  // --------------------------------------------------------------------------
  public void construct()
  {
	  
	 nodes[0] = node(progs[0]=new AProg(0, 1,0==(nrOfProgs-1)),String.valueOf(0), 160*(0%2)+80,
160*(0/2)+80);
	 nodes[1] = node(progs[1]=new AProg(1, 1,1==(nrOfProgs-1)),String.valueOf(1), 160*(1%2)+80,
160*(1/2)+80);
	 nodes[2] = node(progs[2]=new AProg(2, 1,2==(nrOfProgs-1)),String.valueOf(2), 160*(2%2)+80,
160*(2/2)+80);
//    Node node0 = node(new Prog(0), "0", 100, 100);
//    Node node1 = node(new Prog(1), "1", 150, 200);
//    Node node2 = node(new Prog(2), "2", 300, 150);
    link(nodes[0], nodes[1]);
    link(nodes[0],nodes[2]);
  }

  // --------------------------------------------------------------------------
  // informative message
  // --------------------------------------------------------------------------
  public String getText()
  {
    return "A Raymond Tree Algorithm Implementation";
  }

@Override
public void resetStatistics() {
	// TODO Auto-generated method stub
	
}
}