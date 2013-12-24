package fr.esir.sh.server;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.esir.sh.client.SHService;
import fr.esir.sh.client.guicomponents.Rectangle;

public class SHServiceImpl extends java.rmi.server.UnicastRemoteObject implements SHService{
	
	private static final long serialVersionUID = 1L;
	private int cellSize = 20;
	private int gridSize = 20;
	private int numberOfSweets = 10;
	List<SHServiceClientImpl> listClients= new ArrayList<SHServiceClientImpl>();
	List<Player> listPlayers= new ArrayList<Player>();
	boolean[][] gameMap = addSweetsIntoLandscape();
	
	public SHServiceImpl() throws java.rmi.RemoteException {
	
		super();
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
	public boolean[][] getLogicGameMap()  throws RemoteException{
		
		return this.gameMap;		
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
		
		return gameMap;
	}
	
	private boolean verifyIfEmptyPos(int x, int y){
		
		boolean empty= true;
		
		for(Player player: this.listPlayers)
			
			if(player.getX() == x && player.getY() == y)	empty= false;
		
		return empty;
	}

	private void movePlayerToRight(SHServiceClientImpl shServiceClient)
			throws RemoteException {	

		Player player= fetch(shServiceClient);
		
		if(player == null)	
			throw new IllegalStateException("The player to move doesn't exist");
	
		int result[]= moveToRight(player, shServiceClient);
		
		//TODO Mettre ceci dans une fonction a part
		for(SHServiceClientImpl client: listClients){
			
			client.getPointAndChange(shServiceClient.getColor(), result);
		}
	}
	
	private int[] moveToRight(Player player, SHServiceClientImpl shServiceClient) 
			throws RemoteException{
		
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

	private void movePlayerToLeft(SHServiceClientImpl shServiceClient)
			throws RemoteException {
	
		
		Player player= fetch(shServiceClient);
		
		if(player == null)
			
			throw new IllegalStateException("The player to move doesn't exist");
		
		int result[] = moveToLeft(player, shServiceClient);
		//shServiceClient.changeRectanglePos(result[0], result[1]);
		for(SHServiceClientImpl client: listClients){
			
			client.getPointAndChange(shServiceClient.getColor(), result);
		}
	}

	private int[] moveToLeft(Player player, SHServiceClientImpl shServiceClient) 
			throws RemoteException{
		
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
	

	private void movePlayerToDown(SHServiceClientImpl shServiceClient)
			throws RemoteException {
	
		Player player= fetch(shServiceClient);
		
		if(player == null)
			
			throw new IllegalStateException("The player to move doesn't exist");
		
		int result[] = moveToDown(player, shServiceClient);
		//shServiceClient.changeRectanglePos(result[0], result[1]);
		for(SHServiceClientImpl client: listClients){
			
			client.getPointAndChange(shServiceClient.getColor(), result);
		}
	}
	
	private int[] moveToDown(Player player, SHServiceClientImpl shServiceClient)
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
	

	private void movePlayerToUp(SHServiceClientImpl shServiceClientM)
			throws RemoteException {
		
		Player player= fetch(shServiceClientM);
		
		if(player == null)
			
			throw new IllegalStateException("The player to move doesn't exist");

		int result[] = moveToUp(player, shServiceClientM);
		//shServiceClientM.changeRectanglePos(result[0], result[1]);
		for(SHServiceClientImpl client: listClients){
			
			client.getPointAndChange(shServiceClientM.getColor(), result);
		}
	}
	
	private int[] moveToUp(Player player, SHServiceClientImpl shServiceClient)
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
	
	private void verifyIfStrawberryCollected(SHServiceClientImpl shServiceClient){
		
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
	
	private void verifyIfAllSweetsConsumed(SHServiceClientImpl shServiceClient){
		
		try{
			
			if(numberOfSweets == 0){
				
				shServiceClient.addScore();
				SHServiceClientImpl winner= this.getWinner();
				ScoreDisplayer scoreDisplayer= new ScoreDisplayer(winner.getClientId(), winner.getColor(), winner.getScore());
			}
		}catch(RemoteException e){
			
			throw new IllegalStateException("RemoteException. Could not load the client to get his id");
		}

	}
	
	private SHServiceClientImpl getWinner(){
		
		int max=0;
		SHServiceClientImpl winner= null;
		
		try{
			for(SHServiceClientImpl client: listClients){
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
	
	private Player fetch(SHServiceClientImpl shServiceClient){
		
		Player player= null;
		
		for(Player bufferPlayer: listPlayers)
			if(shServiceClient.equals(bufferPlayer.getShServiceClientM())) player= bufferPlayer;
		
		return player;
	}

	@Override
	public void addNewPlayer(SHServiceClientImpl shServiceClient, int id) throws RemoteException {
		
		listClients.add(shServiceClient);
		
		Player player1= new Player(shServiceClient);
		listPlayers.add(player1);
		
		addRectangleIntoLandscape();
	}
	
	private void addRectangleIntoLandscape() throws RemoteException{
		
		for(SHServiceClientImpl clientReceiver: listClients){ 
			for(SHServiceClientImpl clientSender: listClients){
				
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
	public synchronized void movePlayer(SHServiceClientImpl shServiceClientImpl,
			char movement) throws RemoteException {
		
		if(movement == 'r') this.movePlayerToRight(shServiceClientImpl);
		if(movement == 'l') this.movePlayerToLeft(shServiceClientImpl);
		if(movement == 'u') this.movePlayerToUp(shServiceClientImpl);
		if(movement == 'd') this.movePlayerToDown(shServiceClientImpl);	
	}
	
}