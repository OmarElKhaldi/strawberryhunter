package fr.esir.sh.server;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BackupServerLauncher3 {
		
	public static void main(String[] args) {
		
		Logger logger= LoggerFactory.getLogger(SHServiceServer.class);
		
		SHServiceServer backupServer;
		boolean isPrimary= false;
		backupServer= new SHServiceServer("localhost", 8093, isPrimary, 4);
		backupServer.linkToServer("localhost", 8090);
		
		try {
			
			backupServer.loadServer();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that loads the server.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
	}
}
