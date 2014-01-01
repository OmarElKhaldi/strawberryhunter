package fr.esir.sh.client;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import fr.esir.sh.client.guicomponents.Point;
import fr.esir.sh.client.guicomponents.Rectangle;
import fr.esir.sh.server.SHServiceClient;

public class SHServiceClientM extends java.rmi.server.UnicastRemoteObject implements Serializable, SHServiceClient{
	
	private int clientId;
	private static final long serialVersionUID = 1L;
	private SHService shService;
	private SHServiceClientV shServiceClientV;
	private List<SHServiceClient> listClients= new ArrayList<SHServiceClient>();
	private Point myPoint;
	private List<Point> listPoints= new ArrayList<Point>();
	private int score= 0;
	boolean[][] logicGameMap= null;
	
	public SHServiceClientM(int clientId, int x, int y, Color color, SHServiceClientV shServiceClientV) throws RemoteException{
		
		super();		
		this.clientId= clientId;		
		this.shServiceClientV= shServiceClientV;
		this.shServiceClientV.setX(x);
		this.shServiceClientV.setY(y);
		this.shServiceClientV.setColor(color);		
		shService  = initializeService();
		this.initializeCellSize();		
		this.initializeGridSize();		
		this.shServiceClientV.initializeContainer();		
		logicGameMap= initializeGameMap();		
		this.shServiceClientV.addModelToView(this);		
		this.shServiceClientV.setTitle("Player "+clientId);	
		this.shServiceClientV.initializeFrame();
		this.addNewPlayer();	    
	}
	
	public void addSweetsAndDisplay(){
		
		this.shServiceClientV.addSweetsIntoGUI(this.logicGameMap);
		this.shServiceClientV.display();
	}
	
	@Override
	public SHService getSHService(){
		
		return this.shService;
	}
	
	@Override
	public int getX(){
		
		return this.shServiceClientV.getX();
	}
	
	@Override
	public int getY(){
		
		return this.shServiceClientV.getY();
	}
	
	@Override
	public Color getColor(){
		
		return this.shServiceClientV.getColor();
	}
	
	@Override
	public Rectangle getMyRectangle(){
		
		return this.shServiceClientV.getMyRectangle();
	}
	
	@Override
	public SHServiceClientV getView(){
		
		return this.shServiceClientV;
	}
	
	@Override
	public void addScore(){
		
		this.score++;
	}
	
	@Override
	public int getScore(){
		
		return this.score;
	}
	
	public void addNewPlayer(){
		
		try{
			
			this.shServiceClientV.addNewPlayer();
		    shService.addNewPlayer(this, clientId);
		}
		catch(RemoteException e){
			
			e.printStackTrace();
		}
	}
	
	public void addRectangle(int id, int x, int y, Color color) throws RemoteException{
		
		//this.listPoints.add(new Point(id, x, y));
		this.shServiceClientV.addRectangle(id, x, y, color);
	}
	
/*	@Override
	public void initializeMyPoint(int x, int y){
		
		try {
			myPoint= new Point(clientId, x, y);
			this.listPoints.add(myPoint);
		}
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
/*	@Override
	public void addPoint(int x, int y) throws RemoteException{
		
		Point point = new Point(clientId, x, y);
		this.listPoints.add(point);
	}*/
	
	private SHService initializeService(){
		
		SHService shService;
		try {
			
			shService = (SHService) Naming.lookup("rmi://localhost:8090/SHService");
		}
		catch (MalformedURLException e) {
			
			throw new IllegalStateException(
					"MalformedURLException occured. Please check if the host name and/or the port number and/or the name of the service is/are correct",
					e);
		}
		catch (RemoteException e) {
			
			throw new IllegalStateException(
					"RemoteException occured. Maybe an exception occured during the execution of a remote method call",
					e);
		}
		catch (NotBoundException e) {
			
			throw new IllegalStateException(
					"NotBoundException occured while lookimg up or unbinding in the registry a name that has no associated binding.",
					e);
		}
		
		return shService;
		
	}
	
	private void initializeCellSize(){
		
		int cellSize= 20;
		
		try {
			
			cellSize = shService.getCellSize();
		
		} 
		catch (RemoteException e) {
		
			System.err.println("RemoteException occured. " +
					"the cell size is not loaded, this number is initialized to 20");
			
		}
		
		this.shServiceClientV.setCellSize(cellSize);		
	}
	
	private void initializeGridSize(){
		
		int gridSize= 20;
		
		try {
			
			gridSize = shService.getGridSize();
		
		} 
		catch (RemoteException e) {
		
			System.err.println("RemoteException occured. " +
					"the grid size is not loaded, this number is initialized to 20");
			
		}
		
		this.shServiceClientV.setGridSize(gridSize);
	}
	
	public void removeSweetFromServer(int x, int y){
		
		this.logicGameMap[x][y]= false;
	}
	
	private boolean[][] initializeGameMap(){
		
		boolean[][] gameMap = null;
		
		try{
			
			gameMap = shService.getLogicGameMap();
		}
		catch (RemoteException e) {
			
			System.err.println("RemoteException occured. the sweets are not loaded in the game map." +
					"Consequently, this player would not have no sweet in his/her map");
		}
		return gameMap;
	}
	
	@Override
	public int getClientId() throws RemoteException{
	
		return clientId;
	}
	
	public boolean equals(SHServiceClientM shServiceClientM){
		
		
		int otherId= -1;
		
		try {
			otherId = shServiceClientM.getClientId();
		}
		catch (RemoteException e) {
			
			throw new IllegalStateException("RemoteExceptionOccured. Could not get the client id", e);
		}
		
		if(this.clientId == otherId && this.clientId != -1)				
			return true;
		
		return false;
	}

	@Override
	public void getPointAndChange(Color color, int result[]){

		shServiceClientV.changeRectanglePos(color, result);
	}
	
	public static void main(String[] args) throws InterruptedException {
	
		Commands commands1= new Commands(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
		Commands commands2= new Commands(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);		
		Commands commands3= new Commands(KeyEvent.VK_T, KeyEvent.VK_G, KeyEvent.VK_F, KeyEvent.VK_H);
		
		SHServiceClientV shServiceClientV1= new SHServiceClientV(1, commands1);
		SHServiceClientV shServiceClientV2= new SHServiceClientV(2, commands2);
		SHServiceClientV shServiceClientV3= new SHServiceClientV(3, commands3);
		
		try {
			
			SHServiceClientM shServiceClient1 = new SHServiceClientM(1, 0, 0, Color.BLUE, shServiceClientV1);
			SHServiceClientM shServiceClient2 = new SHServiceClientM(2, 2, 2, Color.MAGENTA, shServiceClientV2);
			SHServiceClientM shServiceClient3 = new SHServiceClientM(3, 4, 6, Color.GREEN, shServiceClientV3);
			
			//Make sure that all the players are added in the map, so the sweets can not take their places.
			shServiceClient1.addSweetsAndDisplay();
			shServiceClient2.addSweetsAndDisplay();
			shServiceClient3.addSweetsAndDisplay();
		}
		catch (RemoteException e) {

			throw new IllegalStateException("RemoteException occured. The clients are not initialized, this is probably due to the server that doensn't answer to the requests of the clients.", e);
		}
		
		
	}
	
}