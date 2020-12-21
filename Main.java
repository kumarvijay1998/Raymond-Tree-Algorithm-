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
  public static void main(String[] args)
  {
    new Main().run();
  }

  // --------------------------------------------------------------------------
  // constructor for application
  // --------------------------------------------------------------------------
  public Main()
  {
    super("Raymond Tree Program", 400, 300);
  }

  // --------------------------------------------------------------------------
  // construction of network
  // --------------------------------------------------------------------------
  public void construct()
  {
    Node node0 = node(new Prog(0), "0", 200, 50);
    Node node1 = node(new Prog(1), "1", 150, 150);
    Node node2 = node(new Prog(2), "2", 250, 150);
    Node node3 = node(new Prog(3), "3", 120, 230);
    link(node0, node1);
    link(node1, node0);
    link(node0, node2);
    link(node2, node0);
    link(node1, node3);
    link(node3, node1);
    
  }

  // --------------------------------------------------------------------------
  // informative message
  // --------------------------------------------------------------------------
  public String getText()
  {
    return "Raymond Tree Algorithm implementation with DAJ";
  }

@Override
public void resetStatistics() {
	// TODO Auto-generated method stub
	
}
}