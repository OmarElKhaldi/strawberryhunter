package fr.esir.sh.client.guicomponents;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

import fr.esir.sh.client.SHServiceClientV;

public class Rectangle extends AbstractGridShape implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int rectId;
	private int x;
	private int y;
	private Color color;
	SHServiceClientV shServiceClientV;
	
	public Rectangle(SHServiceClientV display, int rectId, int x, int y, Color color) {
		  
	   	super(display);
	   	this.shServiceClientV= display;
	   	this.setCoordinates(x, y);
	   	this.rectId= rectId;
	   	this.color= color;
	}
	  
	public void drawShape(Graphics g,int x,int y,int w,int h) {
		  
		g.setColor(this.color);
	    g.fillRect(x,y,w,h);
	}
	  
	public void setCoordinates(int x, int y){
		  
		this.setX(x);
		this.setY(y);
	}

	public SHServiceClientV getSHServiceClientV(){
		  
		return this.shServiceClientV;
	}
	
	public int getRectId() {
	
		return rectId;
	}

	
	public void setRectId(int rectId) {
	
		this.rectId = rectId;
	}
	
	public Color getColor(){
		
		return this.color;
	}	
}