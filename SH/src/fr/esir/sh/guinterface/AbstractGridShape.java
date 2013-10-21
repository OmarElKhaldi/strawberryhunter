package fr.esir.sh.guinterface;

import java.awt.Graphics;
import javax.swing.JComponent;

abstract class AbstractGridShape extends JComponent {

	  /* Position of the shape in the grid */
	  public int x = 0 ;
	  public int y = 0 ;
	  private ExampleDisplay dis ;

	  public AbstractGridShape(ExampleDisplay display) {
	    dis = display ;
	    dis.add(this);
	    dis.pack(); // somehow needed or add does not work properly
	  }

	  /*
	   * Set the positions of the shape in grid coordinates
	   */
	  public void setGridPos(int someX,int someY) {
	    x = someX ; y = someY ;
	  }

	  abstract public void drawShape(Graphics g,int x,int y,int w,int h);

	  /* delegates drawing proper to drawShape. Transform the grid
	   * coordinates of the shape into pixel coordinates, using the cell
	   * size of the ExampleDisplay associated with the AbstractGridShape */
	  public void paint(Graphics g) {
	    this.drawShape(g,
	                   dis.cellSize/2 + x*dis.cellSize, 
	                   dis.cellSize/2 + y*dis.cellSize, 
	                   dis.cellSize, dis.cellSize);
	  }

	  public void moveRect(int[] delta) {
	    x = (x+delta[0]+dis.gridSize)%dis.gridSize ;
	    y = (y+delta[1]+dis.gridSize)%dis.gridSize ;
	  }
	} // EndClass AbstractGridShape
