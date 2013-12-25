package fr.esir.sh.client.guicomponents;

import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.JComponent;
import fr.esir.sh.client.SHServiceClientV;

abstract class AbstractGridShape extends JComponent implements Serializable{

	private static final long serialVersionUID = 1L;
	/* Position of the shape in the grid */
	  public int x = 0 ;
	  public int y = 0 ;
	  private SHServiceClientV dis;

	  public AbstractGridShape(SHServiceClientV display) {
	    dis = display ;
	    dis.add(this);
	    dis.pack(); // somehow needed or add does not work properly
	  }
	  
	  public void setX(int x) {
		
		  this.x = x;
	  }

	  public void setY(int y) {
		
		  this.y = y;
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
	   * size of the SHServiceImpl associated with the AbstractGridShape */
	  public void paint(Graphics g) {
		  
		this.drawShape(g,
				dis.getCellSize()/2 + x*dis.getCellSize(), 
                dis.getCellSize()/2 + y*dis.getCellSize(), 
                dis.getCellSize(), dis.getCellSize());
	  }

	  public void moveRect(int[] delta) {
	  
		x = (x+delta[0]+dis.getGridSize())%dis.getGridSize() ;
		y = (y+delta[1]+dis.getGridSize())%dis.getGridSize() ;
	  }
	} // EndClass AbstractGridShape
