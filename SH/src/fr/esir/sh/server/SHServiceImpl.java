package fr.esir.sh.server;

import java.rmi.RemoteException;
import java.util.Random;
import fr.esir.sh.client.SHService;

public class SHServiceImpl extends java.rmi.server.UnicastRemoteObject
		implements SHService {
	
	private static final long serialVersionUID = 1L;
	
	private int cellSize = 20;
	
	private int gridSize = 20;
	
	private int numberOfSweets = 10;
	
	boolean[][] gameMap = addSweetsIntoLandscape();;
	
	public SHServiceImpl() throws java.rmi.RemoteException {
		
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
	
	public boolean[][] addSweetsIntoLandscape(){
		
		boolean[][] gameMap = new boolean[gridSize][gridSize];
		
	    Random rand = new Random();
		
		for(int i = 0; i < numberOfSweets; i++) {
			
			int j, k;	      
			
			do {
	    	  
				j = rand.nextInt(gridSize);
	        
				k = rand.nextInt(gridSize);
	        
			} while (gameMap[j][k] == true);
			
			gameMap[j][k] = true;
			
	    }
		
		return gameMap;
		
	}
	
	@Override
	public boolean[][] getLogicGameMap()  throws RemoteException{
		
		return this.gameMap;
		
	}
	
}