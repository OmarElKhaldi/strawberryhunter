package fr.esir.sh.client.guicomponents;

import java.awt.Color;
import java.awt.Graphics;
import fr.esir.sh.client.SHServiceClientV;

public class Circle extends AbstractGridShape {
	
	private static final long serialVersionUID = 1L;

	public Circle(SHServiceClientV display) {
		  
	    super(display);
	    
	  }
	  
	  public void drawShape(Graphics g,int x,int y,int w,int h) {

	    g.setColor(Color.RED);
	    g.fillOval(x,y,w,h);
	  }
	  
} // EndClass Circle
