package fr.esir.sh.guinterface;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

public class ExampleDisplay extends JFrame implements KeyListener {

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
/*	  int cellSize = 20 ;
	  int gridSize = 20 ;
	  Map<Integer,int[]> moveTable = new HashMap<Integer,int[]>() ;
	  Rectangle myRectangle = new Rectangle(this) ;
	  Container myContainer ;
	  int numberOfSweets = 10 ;

	   gameMap contains the plan of the sweets to collect initialized to
	   * null by default 
	  Circle[][] gameMap = new Circle[gridSize][gridSize]; 

	  public ExampleDisplay() {
	    super();
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(false);
	    setLocation(30, 30);
	    myContainer = getContentPane();
	    myContainer.setPreferredSize(new Dimension(cellSize * (gridSize + 1), cellSize * (gridSize + 1) ));
	    pack();
	    
	    // adding the red circles for a bit of landscape
	    Random rand = new Random();

	    for(int i = 0; i < numberOfSweets; i++) {
	      int j, k;
	      do {
	        j = rand.nextInt(gridSize);
	        k = rand.nextInt(gridSize);
	      } while (gameMap[j][k]!=null);

	      gameMap[j][k] = new Circle(this);
	      gameMap[j][k].setGridPos(j,k);
	    } // EndFor i

	    setVisible(true);

	    moveTable.put(KeyEvent.VK_DOWN ,new int[] { 0,+1});
	    moveTable.put(KeyEvent.VK_UP   ,new int[] { 0,-1});
	    moveTable.put(KeyEvent.VK_LEFT ,new int[] {-1, 0});
	    moveTable.put(KeyEvent.VK_RIGHT,new int[] {+1, 0});
	    
	    addKeyListener(this);

	  } // EndConstructor ExampleDisplay

	   needed to implement KeyListener 
	  public void keyTyped   (KeyEvent ke){}
	  public void keyReleased(KeyEvent ke){}
	  
	   where the real work happens: reacting to key being pressed 
	  public void keyPressed (KeyEvent ke){ 
	   
		  int keyCode = ke.getKeyCode();
	    
		  if (!moveTable.containsKey(keyCode)) return ;
	    
		  myRectangle.moveRect(moveTable.get(keyCode));
	    
		  if (gameMap[myRectangle.x][myRectangle.y]!=null) {
	      
			  Circle c = gameMap[myRectangle.x][myRectangle.y];
	      
			  myContainer.remove(c);
	      
			  pack();
	      
			  gameMap[myRectangle.x][myRectangle.y]=null;
	      
			  numberOfSweets--;
	      
			  if (numberOfSweets==0) {
	        
				  System.out.println("You've won. Congratulations!");
	        
				  System.exit(0);
	      
			  }
	      
			  System.out.println("Only "+numberOfSweets+" sweet(s) remaining...");
	    
		  }
	    
		  repaint();
	  
	  } // EndMethod keyPressed

	  public static void main(String[] a) {
	    JFrame window = new ExampleDisplay();
	  } // EndMethod main
*/	} // EndClass ExampleDisplay
