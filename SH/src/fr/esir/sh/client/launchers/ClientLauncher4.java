package fr.esir.sh.client.launchers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.client.SHServiceClientM;
import fr.esir.sh.client.SHServiceClientV;
import fr.esir.sh.client.guicomponents.Commands;


public class ClientLauncher4 {
	
	public static void main(String[] args) {
		
		Logger logger= LoggerFactory.getLogger(SHServiceClientM.class);
	
		Commands commands= new Commands(KeyEvent.VK_9, KeyEvent.VK_O, KeyEvent.VK_I, KeyEvent.VK_P);
		SHServiceClientV shServiceClientV= new SHServiceClientV(4, commands);
		SHServiceClientM shServiceClient;
		
		try{
			
			shServiceClient = new SHServiceClientM(4, 7, 6, Color.YELLOW, shServiceClientV, "localhost", 8090);
		}
		catch(RemoteException e){
			
			String errorMsg= "RemoteException occured. The clients are not initialized, this is probably due to the server that doensn't answer to the requests of the clients.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg, e);
		}
		
		shServiceClient.addSweetsAndDisplay();
	}
}