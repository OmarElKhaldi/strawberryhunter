package fr.esir.sh.server;

import java.awt.Color;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.esir.sh.client.SHService;
import fr.esir.sh.client.guicomponents.Rectangle;

public class SHServiceImpl extends java.rmi.server.UnicastRemoteObject implements SHService{
	
	private static final long serialVersionUID = 1L;
	private int cellSize = 20;
	private int gridSize = 20;
	private int numberOfSweets = 10;
	private List<SHServiceClient> listClients= new ArrayList<SHServiceClient>();
	private List<Player> listPlayers= new ArrayList<Player>();
	private boolean[][] gameMap;
	private SHServiceServer shServiceServer;
	private List<SHService> listServices= new ArrayList<SHService>();
	private Logger logger= LoggerFactory.getLogger(SHServiceImpl.class);
	
	public SHServiceImpl() throws java.rmi.RemoteException {
	
		super();
	}
	
	@Override
	public String getServerName(){
		
		return this.shServiceServer.getName();
	}
	
	@Override
	public void setServer(SHServiceServer shServiceServer){
		
		this.shServiceServer= shServiceServer;
	}
	
	@Override
	public int getCellSize() throws RemoteException {
	
		return cellSize;
	}

	@Override
	public int getGridSize() throws RemoteException {
	
		return gridSize;
	}
	
	@Override
	public int getNumberOfSweets() throws RemoteException {
		
		return this.numberOfSweets;
	}
	
	@Override
	public List<SHServiceClient> getListClients(){
		
		return this.listClients;
	}

	/*@Override
	public void linkToServiceImpl(SHService shService){
		
		this.listServices.add(shService);
		try {
			
			shService.linkToServiceFromOneSide(this);
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException ocured. ("+this.getServerName()+") successfully added a new server but not from the other side.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
	}
	
	@Override
	public void linkToServiceFromOneSide(SHService shService){
	
		this.listServices.add(shService);
	}*/
	
	@Override
	public void addService(String hostAdress, int port){
		
		try {
			
			SHService shService = (SHService) Naming.lookup("rmi://"+hostAdress+":"+port+"/SHService");
			this.listServices.add(shService);
		}
		catch (MalformedURLException e) {
			
			String errorMsg= "MalformedURLException occured. Please check if the host name and/or the port number and/or the name of the service is/are correct";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg, e);
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. ("+this.getServerName()+") could not reach the server to add on the server's network.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg, e);
		}
		catch (NotBoundException e) {
			
			String errorMsg= "NotBoundException occured while lookimg up or unbinding in the registry a name that has no associated binding."; 
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg, e);
		}
	}
	
	@Override
	public boolean[][] getLogicGameMap()  throws RemoteException{
		
		return this.gameMap;		
	}
	
	@Override
	public void setLogicGameMap(boolean[][] gameMap){
		
		this.gameMap = gameMap;
	}
	
	@Override
	public void addSweetsIfPrimary(){
		
		if(this.shServiceServer.getIsPrimary()){
			
			gameMap = addSweetsIntoLandscape();
		}
			
	}

	private boolean[][] addSweetsIntoLandscape(){
		
		boolean[][] gameMap = new boolean[gridSize][gridSize];
	    Random rand = new Random();
		for(int i = 0; i < numberOfSweets; i++) {
			
			int j, k;	      
			
			do {
	    	  
				j = rand.nextInt(gridSize);
				k = rand.nextInt(gridSize);
			} while (gameMap[j][k] == true || !(verifyIfEmptyPos(j, k)));
			
			gameMap[j][k] = true;
			
	    }
		
		//TODO: Put this code below in another function
		for(SHService shService : listServices){
			
			try {
				
				shService.setLogicGameMap(gameMap);
			}
			catch (RemoteException e) {
				
				String errorMsg= "RemoteException occured. Could not reach the servers to set their logic game map.";
				logger.error(errorMsg);
				throw new IllegalStateException(errorMsg);
			}
		}
			
		return gameMap;
	}
	
	private boolean verifyIfEmptyPos(int x, int y){
		
		boolean empty= true;
		for(Player player: this.listPlayers)
			if(player.getX() == x && player.getY() == y)	empty= false;
		
		return empty;
	}

	private void movePlayerToRight(SHServiceClient shServiceClient) throws RemoteException {

		logger.info("The ("+this.getServerName()+") server is moving the player to the right");
		
		Player player= fetch(shServiceClient);
		
		if(player == null)	
			throw new IllegalStateException("The player to move doesn't exist");
	
		int result[]= moveToRight(player, shServiceClient);
		
		//TODO Mettre ceci dans une fonction a part
		for(SHServiceClient client: listClients){
			
			client.getPointAndChange(shServiceClient.getColor(), result);
		}
	}
	
	private int[] moveToRight(Player player, SHServiceClient shServiceClient) throws RemoteException{
		
		int x = player.getX();
		int y = player.getY();
		if(x < gridSize - 1 ){
			if(verifyIfEmptyPos(x+1, y)){
				
				x = (x + 1);
				player.setX(x);
				this.verifyIfStrawberryCollected(shServiceClient);
			}
		}
		int result[] = {x, y};
		return result;
	}

	private void movePlayerToLeft(SHServiceClient shServiceClient) throws RemoteException {
	
		
		Player player= fetch(shServiceClient);
		
		if(player == null)
			
			throw new IllegalStateException("The player to move doesn't exist");
		
		int result[] = moveToLeft(player, shServiceClient);
		//shServiceClient.changeRectanglePos(result[0], result[1]);
		for(SHServiceClient client: listClients){
			
			client.getPointAndChange(shServiceClient.getColor(), result);
		}
	}

	private int[] moveToLeft(Player player, SHServiceClient shServiceClient) throws RemoteException{
		
		int x = player.getX();
		int y = player.getY();
		if(x>0){
			if(verifyIfEmptyPos(x-1, y)){
				
				x = (x - 1) % this.getGridSize();
				if(x == -1) x = 19;
				player.setX(x);
				this.verifyIfStrawberryCollected(shServiceClient);
			}
		}
		int result[] = {x, y};
		return result;
	}
	

	private void movePlayerToDown(SHServiceClient shServiceClient)
			throws RemoteException {
	
		Player player= fetch(shServiceClient);
		
		if(player == null)
			
			throw new IllegalStateException("The player to move doesn't exist");
		
		int result[] = moveToDown(player, shServiceClient);
		//shServiceClient.changeRectanglePos(result[0], result[1]);
		for(SHServiceClient client: listClients){
			
			client.getPointAndChange(shServiceClient.getColor(), result);
		}
	}
	
	private int[] moveToDown(Player player, SHServiceClient shServiceClient)
		throws RemoteException{
		
		int x = player.getX();
		int y = player.getY();
		if(y < gridSize - 1){
			if(verifyIfEmptyPos(x, y+1)){
				
				y = (y + 1) % this.getGridSize();
				player.setY(y);
				this.verifyIfStrawberryCollected(shServiceClient);
			}
		}
		int result[] = {x, y};
		return result;
	}
	

	private void movePlayerToUp(SHServiceClient shServiceClientM)
			throws RemoteException {
		
		Player player= fetch(shServiceClientM);
		
		if(player == null)
			
			throw new IllegalStateException("The player to move doesn't exist");

		int result[] = moveToUp(player, shServiceClientM);
		//shServiceClientM.changeRectanglePos(result[0], result[1]);
		for(SHServiceClient client: listClients){
			
			client.getPointAndChange(shServiceClientM.getColor(), result);
		}
	}
	
	private int[] moveToUp(Player player, SHServiceClient shServiceClient)
		throws RemoteException{

		int x = player.getX();
		int y = player.getY();
		if(y > 0){
			if(verifyIfEmptyPos(x, y-1)){
				
				y = (y - 1) % this.getGridSize();
				if(y == -1) y = 19;
				player.setY(y);
				this.verifyIfStrawberryCollected(shServiceClient);
			}
		}
		int result[] = {x, y};
		return result;
	}
	
	private void verifyIfStrawberryCollected(SHServiceClient shServiceClient){
		
		Player player= fetch(shServiceClient);
		
		if(player == null)	
			throw new IllegalStateException("The player to move doesn't exist");
		
		
		int x = player.getX();
		int y = player.getY();
		
		if(this.gameMap[x][y] == true){
			
			this.gameMap[x][y]= false;
			this.numberOfSweets --;
			
			verifyIfAllSweetsConsumed(shServiceClient);
		}	
	}
	
	private void verifyIfAllSweetsConsumed(SHServiceClient shServiceClient){
		
		try{
			
			if(numberOfSweets == 0){
				
				shServiceClient.addScore();
				SHServiceClient winner= this.getWinner();
				ScoreDisplayer scoreDisplayer= new ScoreDisplayer(winner.getClientId(), winner.getColor(), winner.getScore());
			}
		}catch(RemoteException e){
			
			throw new IllegalStateException("RemoteException. Could not load the client to get his id");
		}

	}
	
	private SHServiceClient getWinner(){
		
		int max=0;
		SHServiceClient winner= null;
		
		try{
			for(SHServiceClient client: listClients){
				if(client.getScore() > max){
					
					winner= client;
					max= client.getScore();
				}
			}
		}catch(RemoteException e){
			
			throw new IllegalStateException("RemoteException occured. Could not get the client to display his data in the end of the game", e);
		}
		
		return winner;
	}
	
	private Player fetch(SHServiceClient shServiceClient){
		
		Player player= null;
		
		for(Player bufferPlayer: listPlayers)
			if(shServiceClient.equals(bufferPlayer.getShServiceClientM())) player= bufferPlayer;
		
		return player;
	}

	@Override
	public void addNewPlayer(SHServiceClient shServiceClient, int id) throws RemoteException {
		
		//We notify the other servers about the update
		for(SHService shService : listServices)
			shService.addNewPlayer(shServiceClient, id);
		
		//Then we answer to the request of the client
		listClients.add(shServiceClient);
		Player player1= new Player(shServiceClient);
		listPlayers.add(player1);
		shServiceClient.addServer(this);
		logger.info("Server ("+this.getServerName()+") is adding the client ("+shServiceClient.getClientId()+")");
		addRectangleIntoLandscape();
	}
	
	private void addRectangleIntoLandscape() throws RemoteException{
		
		for(SHServiceClient clientReceiver: listClients){ 
			for(SHServiceClient clientSender: listClients){
				
				int senderId= clientSender.getClientId();
				int x= clientSender.getX();
				int y= clientSender.getY();
				Color color= clientSender.getColor();				
				
				int receiverId= clientReceiver.getClientId();
				Rectangle rectangle= clientReceiver.getMyRectangle();			
				
				if(senderId != receiverId)
					
					clientReceiver.addRectangle(senderId, x, y, color);
				
			}
		}
	}

	@Override
	public synchronized void movePlayer(SHServiceClient shServiceClientImpl,
			char movement) throws RemoteException {
		
		//First, we send the request to the servers linked to this one for the update
		for(SHService shServiceImpl: listServices)
			shServiceImpl.movePlayer(shServiceClientImpl, movement);
		
		//Then we execute the request
		if(movement == 'r') this.movePlayerToRight(shServiceClientImpl);
		if(movement == 'l') this.movePlayerToLeft(shServiceClientImpl);
		if(movement == 'u') this.movePlayerToUp(shServiceClientImpl);
		if(movement == 'd') this.movePlayerToDown(shServiceClientImpl);	
	}
	
}