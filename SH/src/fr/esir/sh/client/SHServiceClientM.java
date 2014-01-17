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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.client.guicomponents.Point;
import fr.esir.sh.client.guicomponents.Rectangle;
import fr.esir.sh.server.SHServiceClient;
import fr.esir.sh.server.SHServiceImpl;

public class SHServiceClientM extends java.rmi.server.UnicastRemoteObject implements Serializable, SHServiceClient{
	
	private int clientId;
	private static final long serialVersionUID = 1L;
	private SHService shService;
	private SHServiceClientV shServiceClientV;
	private List<SHServiceClient> listClients= new ArrayList<SHServiceClient>();
	private Point myPoint;
	private List<Point> listPoints= new ArrayList<Point>();
	private int score= 0;
	private boolean[][] logicGameMap= null;
	//private List<SHService> listServers= new ArrayList<SHService>();
	private String serverHostAdress;
	private int serverPort;
	private List<SHService> listLinksOfServices= new ArrayList<SHService>();
	private Logger logger= LoggerFactory.getLogger(SHServiceClientM.class);
	
	public SHServiceClientM(int clientId, int x, int y, Color color, SHServiceClientV shServiceClientV, String serverHostAdress, int serverPort) throws RemoteException{
		
		super();		
		this.clientId= clientId;		
		this.serverHostAdress= serverHostAdress;
		this.serverPort= serverPort;
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
	
	@Override
	public String getServerHostAdress(){
		
		return this.serverHostAdress;
	}
	
	@Override
	public int getServerPort(){
		
		return this.serverPort;
	}
	
	@Override
	public List<SHService> getListLinksToServices(){
		
		return this.listLinksOfServices;
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
	
	public void reinitializeService(String hostAdress, int port){
		
		this.serverHostAdress= hostAdress;
		this.serverPort= port;
		this.shService= this.initializeService();
		logger.warn("The connexion of the client ("+this.clientId+") is reinitialized to the server at ("+this.serverHostAdress+", "+this.serverPort+")");
	}
	
	private SHService initializeService(){
		
		SHService shService;
		try {
			
			//We connect this client to the server
			shService = (SHService) Naming.lookup("rmi://"+this.serverHostAdress+":"+this.serverPort+"/SHService");
			//Then we get all the servers linked to this one, it will help us to change the server when this one fails
			listLinksOfServices.addAll(shService.getListServices());
			logger.warn("The client ("+this.clientId+") is connected to the server at ("+this.serverHostAdress+", "+this.serverPort+")");
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

	@Override
	public void removeOldService(SHService oldPrimaryService){
		
		if(listLinksOfServices.contains(oldPrimaryService)){
			
			listLinksOfServices.remove(oldPrimaryService);
			logger.warn("The client ("+this.clientId+") is not anymore connected to the old server.");
		}
			
	}
	
	@Override
	public void addNewService(SHService newPrimaryService)throws RemoteException{
		
		if(!listLinksOfServices.contains(newPrimaryService)){
			
			listLinksOfServices.add(newPrimaryService);
		}
		this.reinitializeService(newPrimaryService.getHostAdress(), newPrimaryService.getPort());
		logger.info("The client ("+this.clientId+") is now connected to the server at ("+newPrimaryService.getHostAdress()+":"+newPrimaryService.getHostAdress()+").");
	}
	
/*	@Override
	public void addServer(SHService shService) throws RemoteException {
	
		this.listServers.add(shService);
		try {
			
			logger.info("Client ("+this.clientId+") is adding the server ("+shService.getServerName()+")");
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the server to get it's name in order to display it.";
			logger.error(errorMsg);
		}
	}*/
	
}