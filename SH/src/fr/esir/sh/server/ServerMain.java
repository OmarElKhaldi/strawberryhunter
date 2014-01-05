package fr.esir.sh.server;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {
	
	public static void main(String[] args) {
	
		Logger logger= LoggerFactory.getLogger(SHServiceServer.class);
		
		//Scenario 1 ==========================================================================================================
		//Creating a primary server, then a backup. After that, we link the list of the clients with the primary.
		//Therefore, those clients are linked to the backup server.
		//=====================================================================================================================
		
/*		boolean isPrimary= true;
		String hostAdress= "localhost";
		int port= 8090;
		SHServiceServer shServiceServer= new SHServiceServer(hostAdress, port, isPrimary);
		
		isPrimary= false;
		port= 8091;
		SHServiceServer shServiceServer1= new SHServiceServer(hostAdress, port, isPrimary);
		shServiceServer1.linkToServer("localhost", 8090);
		try {
			
			shServiceServer.getSHService().addSweetsIfPrimary();
			shServiceServer1.getSHService().addSweetsIfPrimary();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that fills the servers with sweets.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}*/
		
		//We instantiate the logger
		logger= LoggerFactory.getLogger(SHServiceServer.class);
		
		//We create the primary server
		SHServiceServer primaryServer;
		boolean isPrimary= true;
		String hostAdress= "localhost";
		int port= 8090;
		primaryServer= new SHServiceServer(hostAdress, port, isPrimary);
		
		try {
			
			primaryServer.loadServer();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that loads the servers.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
		
		//We create the backup Server
		SHServiceServer backupServer;
		isPrimary= false;
		port= 8091;
		backupServer= new SHServiceServer(hostAdress, port, isPrimary);
		backupServer.linkToServer("localhost", 8090);
		
		//We load both of the servers.
		try {
			
			backupServer.loadServer();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that loads the servers.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
		
		//Scenario 2 ==========================================================================================================
		//Creating a primary server, then we load it by filling the logic matrix of sweets.
		//Creating a backup server, link it to the primary and loading the logic matrix of sweets from the primary server.
		//=====================================================================================================================

		/*SHServiceServer shServiceServer= new SHServiceServer("localhost", 8090, true);
		try {
			
			shServiceServer.getSHService().addSweetsIfPrimary();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that fills the primary server with sweets.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
		
		SHServiceServer shServiceServer1= new SHServiceServer("localhost", 8091, false);
		shServiceServer1.linkToServer("localhost", 8090);
		try {
			
			shServiceServer1.getSHService().addSweetsIfPrimary();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that fills the backup server with sweets.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}*/
		
		//Scenario 3========================================================================================================== 
		//Creating a primary server. We link the list of the clients to this one and we lanch the second server.
		//Consequently, after the link between the servers, the backup is linked to the list of the clients related to the 
		//primary one
		//====================================================================================================================
		
		//Creating the primary server
/*		boolean isPrimary= true;
		String hostAdress= "localhost";
		int port= 8090;
		SHServiceServer shServiceServer= new SHServiceServer(hostAdress, port, isPrimary);
		try {
			
			shServiceServer.getSHService().addSweetsIfPrimary();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that fills the primary server with sweets.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}*/
		
		
		//Creating the backup server
/*		boolean isPrimary= false;
		String hostAdress= "localhost";
		int port= 8091;
		SHServiceServer shServiceServer1= new SHServiceServer(hostAdress, port, isPrimary);
		try {
			
			shServiceServer1.getSHService().addSweetsIfPrimary();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that fills the primary server with sweets.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
		
		//Calling the primary server then we link it with the backup.
		isPrimary= true;
		port= 8090;
		SHServiceServer shServiceServer= new SHServiceServer(hostAdress, port, isPrimary);
		shServiceServer.linkToServer("localhost", 8091);*/
	}
}
