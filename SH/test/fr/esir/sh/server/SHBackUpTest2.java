package fr.esir.sh.server;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.server.commonmethods.CommonMethodsForTest;

/**Scenario ==========================================================================================================
 * In this test, we create a primary server with many backups. Then we launch the clients who will be connected
 * to that server. The main purpose is to test a multiple servers environment.
 * =====================================================================================================================*/
public class SHBackUpTest2 {
	
	private static Logger logger;
	private static SHServiceServer primaryServer;
	private static SHServiceServer backupServer1;
	private static SHServiceServer backupServer2;	
	@BeforeClass
	public static void onlyOnce(){
		
		//We instantiate the logger
		logger= LoggerFactory.getLogger(SHServiceServer.class);
		
		//We create the primary server
		boolean isPrimary= true;
		primaryServer= new SHServiceServer("localhost", 8090, isPrimary, 1);
		
		//We create a backup Server
		isPrimary= false;
		backupServer1= new SHServiceServer("localhost", 8091, isPrimary, 2);
		backupServer1.linkToServer("localhost", 8090);
		
		//We create another backup Server
		isPrimary= false;
		backupServer2= new SHServiceServer("localhost", 8092, isPrimary, 3);
		backupServer2.linkToServer("localhost", 8090);
		
		//We load both of the servers.
		try {
			
			primaryServer.loadServer();
			backupServer1.loadServer();
			backupServer2.loadServer();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that loads the servers.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
		
		//We launch the clients
		CommonMethodsForTest.launchClients();
	}
	
	@Test
	public void testCompareLength(){

		//Verify if the length of the landscape is the same for the primary and the backups.
		//assertEquals("Test failed. The length of the primary landscape is different than the one of the backup.", primaryLength, backup1Length);
		//assertEquals("Test failed. The length of the primary landscape is different than the one of the backup.", primaryLength, backup1Length);		
	}
}
