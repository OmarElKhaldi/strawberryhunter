package fr.esir.sh.server;

import java.rmi.RemoteException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.server.commonmethods.CommonMethodsForTest;

/**Scenario 1 ==========================================================================================================
 * In this test, we create a primary server that we link with the clients. After that we will add a backup server that 
 * we will link to the primary server. The backup has to extract all the data of the primary in order to be updated.
 * =====================================================================================================================*/
public class SHBackUpTest2 {
	
	private static Logger logger;
	private static SHServiceServer primaryServer;
	
	@BeforeClass
	public static void onlyOnce(){
		
		//We instantiate the logger
		logger= LoggerFactory.getLogger(SHServiceServer.class);
		
		//We create the primary server
		boolean isPrimary= true;
		String hostAdress= "localhost";
		int port= 8090;
		primaryServer= new SHServiceServer(hostAdress, port, isPrimary);
		
		//We load the primary server.
		try {
			
			primaryServer.loadServer();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that loads the primary server.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
		
		//We launch the clients
		CommonMethodsForTest.launchClients();
	}
}
