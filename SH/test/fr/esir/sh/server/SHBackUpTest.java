package fr.esir.sh.server;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.List;
import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.esir.sh.client.SHService;
import fr.esir.sh.server.commonmethods.CommonMethodsForTest;

/**Scenario ==========================================================================================================
 * Creating a primary server, then a backup. After that, we link the list of the clients with the primary.
 * Therefore, those clients are linked to the backup server.
 * =====================================================================================================================*/
public class SHBackUpTest{
	
	private static Logger logger;
	private static int primaryLength;
	private static int backupLength;
	private static SHServiceServer primaryServer;
	private static SHServiceServer backupServer;
	private static int primaryClientsNb;
	private static int backupClientsNb;
	private static List<SHServiceClient> primaryListClients;
	private static List<SHServiceClient> backupListClients;
	
	@BeforeClass
	public static void setUp(){
		
		//We instantiate the logger
		logger= LoggerFactory.getLogger(SHServiceServer.class);
		
		//We create the primary server
		boolean isPrimary= true;
		primaryServer= new SHServiceServer("localhost", 8090, isPrimary, 1);
		
		//We create the backup Server
		isPrimary= false;
		backupServer= new SHServiceServer("localhost", 8091, isPrimary, 2);
		backupServer.linkToServer("localhost", 8090);
		
		//We load both of the servers.
		try {
			
			primaryServer.loadServer();
			backupServer.loadServer();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that loads the servers.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
		
		//We launch the clients
		CommonMethodsForTest.launchClients();
		
		//And we initialize the rest of the variables.
		initializeVariables();
	}
	
	@Test
	public void testReachPrimaryAndBackup(){
		
		assertTrue("RemoteException occured. Could not reach the method that gets the gameMap to extract the size of the primary landscape.", primaryLength != -1);
		assertTrue("RemoteException occured. Could not reach the method that gets the gameMap to extract the size of the backup landscape.", backupLength != -1);		
		
		assertTrue("RemoteException occured. Could not reach the method that gets the number of the clients from the primary server.", primaryClientsNb != -1);
		assertTrue("RemoteException occured. Could not reach the method that gets the number of the clients from the backup server.", backupClientsNb != -1);
		
		assertTrue("RemoteException occured. Could not reach the method that gets the list of the clients from the primary server.", primaryListClients != null);
		assertTrue("RemoteException occured. Could not reach the method that gets the list of the clients from the backup server.", backupListClients != null);
	}
	
	@Test
	public void testCompareLength(){

		//Verify if the length of the landscape is the same for the primary and the backup.
		assertEquals("Test failed. The length of the primary landscape is different than the one of the backup.", primaryLength, backupLength);
	}	
		
	@Test
	public void testCompareWidth(){
		
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
	public void testCompareNumberOfSweets(){
		
		boolean primaryGameMap[][]= getGameMapFromServer(primaryServer);
		boolean backupGameMap[][]= getGameMapFromServer(backupServer);
		
		int primaryNumberOfSweets= countNumberOfSweetsInGameMap(primaryGameMap);
		int backupNumberOfSweets= countNumberOfSweetsInGameMap(backupGameMap);
		
		//First we verify if the sweets are extracted from the primary and the backup servers.
		assertTrue(primaryNumberOfSweets > 0);
		assertTrue(backupNumberOfSweets > 0);
		
		//Then we verify if they extracted the same number of sweets.
		assertEquals(primaryNumberOfSweets, backupNumberOfSweets);
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

	@Test
	public void testCompareNumberOfClients(){
		
		//First, we verify that the primary and the backup servers are connected to clients.
		assertTrue(primaryClientsNb > 0);
		assertTrue(backupClientsNb > 0);
		
		//Then, we verify that they have the same number of clients.
		assertEquals(primaryClientsNb, backupClientsNb);
	}
	
	@Test
	public void testCompareClients(){
		
		assertTrue(primaryListClients.equals(backupListClients));
	}
	//Private methods===================================================================================================

	private static void initializeVariables(){
		
		primaryLength= getGameMapLengthFromServer(primaryServer);
		backupLength= getGameMapLengthFromServer(backupServer);
		
		primaryClientsNb= getNumberOfPlayersFromServer(primaryServer);
		backupClientsNb= getNumberOfPlayersFromServer(backupServer);
		
		primaryListClients= getClientsListFromServer(primaryServer);
		backupListClients=  getClientsListFromServer(backupServer);
	}

	private static int countNumberOfSweetsInGameMap(boolean gameMap[][]){
		
		int numberOfSweets= 0;
		
		for(int i= 0; i< gameMap.length; ++i){
			for(int j= 0; j< gameMap[i].length; ++j){
				
				if (gameMap[i][j])	++numberOfSweets;
			}
		}
		
		return numberOfSweets;
	}
	
	private static List<SHServiceClient> getClientsListFromServer(SHServiceServer server){
		
		List<SHServiceClient> listClients;
		try {
			
			SHService shService= server.getSHService();
			listClients = shService.getListClients();
		}
		catch (RemoteException e) {
			
			listClients= null;
		}
		
		return listClients;
	}
	
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
	
	private static int getNumberOfPlayersFromServer(SHServiceServer shServiceServer){
		
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
}
