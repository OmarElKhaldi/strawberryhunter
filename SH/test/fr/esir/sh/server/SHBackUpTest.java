package fr.esir.sh.server;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.client.SHService;

/**Scenario 1 ==========================================================================================================
 * Creating a primary server, then a backup. After that, we link the list of the clients with the primary.
 * Therefore, those clients are linked to the backup server.
 * =====================================================================================================================*/
public class SHBackUpTest{
	
	static Logger logger;
	private static int primaryLength;
	private static int backupLength;
	private static SHServiceServer primaryServer;
	private static SHServiceServer backupServer;
	private static int primaryClientsNb;
	private static int backupClientsNb;
	
	@BeforeClass
	public static void onlyOnce(){
		
		logger= LoggerFactory.getLogger(SHServiceServer.class);
		
		boolean isPrimary= true;
		String hostAdress= "localhost";
		int port= 8090;
		primaryServer= new SHServiceServer(hostAdress, port, isPrimary);
		
		isPrimary= false;
		port= 8091;
		backupServer= new SHServiceServer(hostAdress, port, isPrimary);
		backupServer.linkToServer("localhost", 8090);
		try {
			
			primaryServer.getSHService().addSweetsIfPrimary();
			backupServer.getSHService().addSweetsIfPrimary();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that fills the servers with sweets.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
		
		primaryLength= getGameMapLengthFromServer(primaryServer);
		backupLength= getGameMapLengthFromServer(backupServer);
		
		
	}
	
	@Test
	public void testReachPrimaryAndBackup(){
		
		assertTrue("RemoteException occured. Could not reach the method that gets the gameMap to extract the size of the primary landscape.", primaryLength != -1);
		assertTrue("RemoteException occured. Could not reach the method that gets the gameMap to extract the size of the backup landscape.", backupLength != -1);		
		assertTrue("RemoteException occured. Could not reach the method that gets the number of the clients.", primaryClientsNb != -1);
	}
	
	@Test
	public void testLength(){

		//Verify if the length of the landscape is the same for the primary and the backup.
		assertEquals("Test failed. The length of the primary landscape is different than the one of the backup.", primaryLength, backupLength);
	}	
		
	@Test
	public void testWidth(){
		
		boolean primaryGameMap[][]= getGameMapFromServer(primaryServer);
		boolean backupGameMap[][]= getGameMapFromServer(backupServer);
		
		assertNotNull("RemoteException occured. Could not reach the method that gets the primary server's gamemap ", primaryGameMap);
		assertNotNull("RemoteException occured. Could not reach the method that gets the backup server's gamemap ", backupGameMap);
		
		for(int i= 0; i< primaryLength; ++i){
			
			int primaryLength= primaryGameMap[i].length;
			int backupLength= backupGameMap[i].length;
			assertEquals(primaryLength, backupLength);
		}
	}
	
	@Test
	public void testCompareLandscapeContents(){
		
		boolean primaryGameMap[][]= getGameMapFromServer(primaryServer);
		boolean backupGameMap[][]= getGameMapFromServer(backupServer);
		
		for(int i= 0; i< primaryGameMap.length; ++i){
			for(int j= 0; j< primaryGameMap[i].length; ++j){
				
				boolean containsSweet= primaryGameMap[i][j];
				assertEquals(containsSweet, backupGameMap[i][j]);
			}
		}
	}	

	public void testNumberOfPlayers(){
		

	}
	
	//Private methods===================================================================================================
	private static int getGameMapLengthFromServer(SHServiceServer server){
		
		int primaryLength;
		
		try {
			SHService service= server.getSHService();
			boolean gameMap[][] = service.getLogicGameMap();
			primaryLength= gameMap.length;
		}
		catch (RemoteException e) {
			
			primaryLength= -1;
		}
		
		return primaryLength;
	}
	
	private boolean[][] getGameMapFromServer(SHServiceServer shServiceServer){
		
		boolean gameMap[][];
		
		try{
			
			SHService shService= shServiceServer.getSHService();
			shService = primaryServer.getSHService();
			gameMap= shService.getLogicGameMap();
		}
		catch(RemoteException e){
			
			gameMap= null;
		}
		
		return gameMap;
	}
	
	private int getNumberOfPlayersFromServer(SHServiceServer shServiceServer){
		
		int playersNb;
		
		try {
			
			SHService shService= shServiceServer.getSHService();
			List<SHServiceClient> listClients= shService.getListClients();
			playersNb= listClients.size();
		}
		catch (RemoteException e) {
			
			playersNb= -1;
		}
		
		return playersNb;
	}
}
