package fr.esir.sh.guinterface;

import java.awt.Color;
import java.awt.Graphics;

public class Circle extends AbstractGridShape {
	  public Circle(ExampleDisplay display) {
	    super(display);
	  }
	  public void drawShape(Graphics g,int x,int y,int w,int h) {
	    g.setColor(Color.RED);
	    g.fillOval(x,y,w,h);
	  }
	} // EndClass Circle
