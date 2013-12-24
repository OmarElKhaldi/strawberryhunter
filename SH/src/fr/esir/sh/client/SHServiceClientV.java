package fr.esir.sh.client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import fr.esir.sh.client.guicomponents.Circle;
import fr.esir.sh.client.guicomponents.Rectangle;
import fr.esir.sh.server.SHServiceClientImpl;

public class SHServiceClientV extends JFrame implements KeyListener, Serializable, java.rmi.Remote{

	private static final long serialVersionUID = 1L;
	private int clientId;
	private int cellSize;
	private int gridSize;
	private int x;
	private int y;
	private Color color;
	private Container myContainer;
	private Circle[][] gameMap;
	private Rectangle myRectangle;
	List<Rectangle> listRectangles= new ArrayList<Rectangle>();
	private SHServiceClientImpl shServiceClientM;
	private Commands commands;
	
	public SHServiceClientV(int clientId, Commands commands){
		
		super();
		this.clientId= clientId;
		this.commands= commands;
	}
	
	public int getCellSize() {
		
		return this.cellSize;	
	}

	public void setCellSize(int cellSize){
		
		this.cellSize= cellSize;
	}
	
	public int getGridSize() {
	
		return gridSize;
	}
	
	public void setGridSize(int gridSize) {
	
		this.gridSize = gridSize;
	}
	
	public int getX() {
	
		return this.x;
	}

	
	public void setX(int x) {
	
		this.x= x;
	}

	
	public int getY() {
	
		return this.y;
	}

	
	public void setY(int y) {
	
		this.y= y;
	}
	
	public Color getColor() {
	
		return this.color;
	}

	public void setColor(Color color) {
		
		this.color= color;
	}
	
	public Rectangle getMyRectangle(){
		
		return this.myRectangle;
	}
	
	public List<Rectangle> getListRectangles(){
		
		return this.listRectangles;
	}
	
	public SHServiceClientImpl getModel(){
		
		return this.shServiceClientM;
	}

	public void initializeFrame(){
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		int location=this.clientId*30;
		setLocation(location, location);
	}
	
	public void initializeContainer(){
		
		this.gameMap= new Circle[gridSize][gridSize];
		this.myContainer = getContentPane();
		this.myContainer.setPreferredSize(new Dimension(cellSize * (gridSize + 1), cellSize * (gridSize + 1) ));
	    this.pack();
	}
	
	public void display(){

	    setVisible(true);
	    addKeyListener(this);
	}
	
	public void addNewPlayer(){
		
		this.myRectangle = new Rectangle(this, this.clientId, this.x, this.y, this.color);
		this.listRectangles.add(this.myRectangle);
/*		try {
			
			this.shServiceClientM.initializeMyPoint(x, y);
		}
		catch (RemoteException e) {
			
			throw new IllegalStateException("RemoteException occured. Could not add a new point.", e);
		}*/
	}
	
	public void addRectangle(int id, int x, int y, Color color){
		
		Rectangle rectangle = new Rectangle(this, id, x, y, color);
		this.listRectangles.add(rectangle);
		this.repaint();
	}
	
	public void addSweetsIntoGUI(boolean[][] logicGameMap){
					    
	    for(int i= 0; i< gridSize; ++i){
	    	for(int j= 0; j< gridSize; ++j){		
	    		if(logicGameMap[i][j] == true){
	    			
	    		      gameMap[i][j] = new Circle(this);
	    		      gameMap[i][j].setGridPos(i,j);
	    		}
	    	}
	    }
	}
	
	public void addModelToView(SHServiceClientImpl shServiceClientM){
		
		this.shServiceClientM= shServiceClientM;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		SHService shService= null;
		
		try {
			
			shService = this.shServiceClientM.getSHService();
		}
		catch (RemoteException e1) {
			
			throw new IllegalStateException("RemoteException occured. Could not get the service to move the players on the map.", e1);
		}
		  		  
		try{
			  
			if (keyCode == this.commands.getRight())	shService.movePlayer(this.shServiceClientM, 'r');
		    else if (keyCode == this.commands.getLeft()) shService.movePlayer(this.shServiceClientM, 'l');
			else if (keyCode == this.commands.getDown()) shService.movePlayer(this.shServiceClientM, 'd');
			else if (keyCode == this.commands.getUp()) shService.movePlayer(this.shServiceClientM, 'u');
		  }
		  catch(RemoteException re){
			  
			System.err.println("RemoteException occured. Could not reach the server to move the player. " +
			  				   "Consequently, the player will stay in his place.");
		  }
	}
	
	public void changeRectanglePos(int pointId, int result[]){

		for(Rectangle rectangle: listRectangles){

			if(rectangle.getRectId() == pointId){
				
				rectangle.setX(result[0]);
				rectangle.setY(result[1]);
				rectangle.repaint();
				removeCircleIfExists(result[0], result[1]);
			}		
		}
	}
	
	public void changeRectanglePos(Color color, int result[]){


		for(Rectangle rectangle: listRectangles){
			if(rectangle.getColor().equals(color)){

				rectangle.setX(result[0]);
				rectangle.setY(result[1]);
				rectangle.repaint();
				boolean removed= removeCircleIfExists(result[0], result[1]);
				if(removed){
					
					try{
						
						SHServiceClientImpl bufferModel= rectangle.getSHServiceClientV().getModel();
						Rectangle bufferRectangle= bufferModel.getMyRectangle();
						Color buffercolor= bufferRectangle.getColor();
						
						if(rectangle.getColor() == buffercolor){
							
							SHServiceClientImpl model= rectangle.getSHServiceClientV().getModel();
							model.addScore();
						}
						
					}catch(RemoteException e){
						
						throw new IllegalStateException("RemoteException occured. Could not get the data of the model from the server.", e);
					}
					
				}
			}
		}
	}

	private boolean removeCircleIfExists(int x, int y){
				  
		  if(this.gameMap[x][y] != null){
			  
			  Circle c = gameMap[x][y];
			  myContainer.remove(c);
			  this.gameMap[x][y] = null;
			  try {
				  
				this.shServiceClientM.removeSweetFromServer(x, y);
				
			  }
			  catch (RemoteException e) {
				
				throw new IllegalStateException("RemoteException. Could not access to the server to remove the sweet from the logic gamemap", e);
			  }
			  pack();
			  
			  return true;
		  }
		  
		  return false;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
}