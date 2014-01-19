package fr.esir.sh.client.launchers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.client.SHServiceClientM;
import fr.esir.sh.client.SHServiceClientV;
import fr.esir.sh.client.guicomponents.Commands;


public class ClientLauncher5 {
	
	public static void main(String[] args) {
		
		Logger logger= LoggerFactory.getLogger(SHServiceClientM.class);
	
		Commands commands= new Commands(KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V);
		SHServiceClientV shServiceClientV= new SHServiceClientV(5, commands);
		SHServiceClientM shServiceClient;
		
		try{
			
			shServiceClient = new SHServiceClientM(5, 8, 8, Color.GRAY, shServiceClientV, "localhost", 8090);
		}
		catch(RemoteException e){
			
			String errorMsg= "RemoteException occured. The clients are not initialized, this is probably due to the server that doensn't answer to the requests of the clients.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg, e);
		}
		
		shServiceClient.addSweetsAndDisplay();
	}
}
