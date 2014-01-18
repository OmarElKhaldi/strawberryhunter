package fr.esir.sh.server;

import java.rmi.RemoteException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.server.commonmethods.CommonMethodsForTest;

/**Scenario ==========================================================================================================
 * In this test, we create a primary server that we link with the clients. We simulate a crash of the primary server in
 * order to see if the fault tolerance is working perfectly.
 * =====================================================================================================================*/
public class SHBackUpTest2 {
	
	private static Logger logger;
	private static SHServiceServer primaryServer;
	private static SHServiceServer backupServer;
	
	@BeforeClass
	public static void onlyOnce(){
		
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
	}
}
