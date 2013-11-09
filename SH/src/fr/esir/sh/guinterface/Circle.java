package fr.esir.sh.guinterface;

import java.awt.Color;
import java.awt.Graphics;

import fr.esir.sh.client.SHServiceClient;
import fr.esir.sh.server.SHServiceImpl;

public class Circle extends AbstractGridShape {
	
	  public Circle(SHServiceClient display) {
		  
	    super(display);
	    
	  }
	  
	  public void drawShape(Graphics g,int x,int y,int w,int h) {
		  
	    g.setColor(Color.RED);
	    
	    g.fillOval(x,y,w,h);
	    
	  }
	  
} // EndClass Circle
