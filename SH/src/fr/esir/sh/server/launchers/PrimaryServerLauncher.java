package fr.esir.sh.server.launchers;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.server.SHServiceServer;


public class PrimaryServerLauncher {
	
	public static void main(String[] args) {
	
		Logger logger= LoggerFactory.getLogger(SHServiceServer.class);
		
		SHServiceServer primaryServer;
		boolean isPrimary= true;
		String hostAdress= "localhost";
		int port= 8090;
		primaryServer= new SHServiceServer(hostAdress, port, isPrimary);
		
		try {
			
			primaryServer.loadServer();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that loads the server.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}
	}

}
