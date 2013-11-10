package fr.esir.sh.guinterface;

import java.awt.Color;
import java.awt.Graphics;

import fr.esir.sh.client.SHServiceClient;

public class Rectangle extends AbstractGridShape {
	
	private static final long serialVersionUID = 1L;

	public Rectangle(SHServiceClient display) {
		  
	    super(display);
	    
	  }
	  
	  public void drawShape(Graphics g,int x,int y,int w,int h) {
		  
	    g.setColor(Color.BLUE);
	    
	    g.fillRect(x,y,w,h);
	    
	  }
	  
	  public void setCoordinates(int x, int y){
		  
		  this.x = x;
		  
		  this.y = y;
		  
	  }
	  
} // EndClass Rectangle
