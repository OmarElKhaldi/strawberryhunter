package fr.esir.sh.server;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerMain {
	
	public static void main(String[] args) {
	
		Logger logger= LoggerFactory.getLogger(SHServiceServer.class);
		boolean isPrimary= true;
		String hostAdress= "localhost";
		int port= 8090;
		SHServiceServer shServiceServer= new SHServiceServer(hostAdress, port, isPrimary);
		
/*		isPrimary= true;
		port= 8090;
		SHServiceServer shServiceServer1= new SHServiceServer(hostAdress, port, isPrimary);
		shServiceServer1.linkToServer("localhost", 8091);*/
		try {
			
			shServiceServer.getSHService().addSweetsIfPrimary();
			//shServiceServer1.getSHService().addSweetsIfPrimary();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that fills the primary server with sweets.";
			logger.error(errorMsg);
			throw new IllegalStateException();
		}
	}
}
