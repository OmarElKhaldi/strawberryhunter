package fr.esir.sh.client.launchers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.client.SHServiceClientM;
import fr.esir.sh.client.SHServiceClientV;
import fr.esir.sh.client.guicomponents.Commands;


public class ClientLauncher1 {
	
	public static void main(String[] args) {
		
		Logger logger= LoggerFactory.getLogger(SHServiceClientM.class);
	
		Commands commands= new Commands(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
		SHServiceClientV shServiceClientV= new SHServiceClientV(1, commands);
		SHServiceClientM shServiceClient;
		
		try{
			
			shServiceClient = new SHServiceClientM(1, 0, 0, Color.BLUE, shServiceClientV, "localhost", 8090);
		}
		catch(RemoteException e){
			
			String errorMsg= "RemoteException occured. The clients are not initialized, this is probably due to the server that doensn't answer to the requests of the clients.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg, e);
		}
		
		shServiceClient.addSweetsAndDisplay();
	}
}
