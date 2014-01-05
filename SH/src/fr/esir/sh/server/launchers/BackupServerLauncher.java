package fr.esir.sh.server.launchers;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.server.SHServiceServer;


public class BackupServerLauncher {
	
	public static void main(String[] args) {
		
		Logger logger= LoggerFactory.getLogger(SHServiceServer.class);
		
		SHServiceServer backupServer;
		boolean isPrimary= false;
		String hostAdress="localhost";
		int port= 8091;
		backupServer= new SHServiceServer(hostAdress, port, isPrimary);
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
