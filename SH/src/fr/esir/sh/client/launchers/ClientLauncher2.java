package fr.esir.sh.client.launchers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.client.SHServiceClientM;
import fr.esir.sh.client.SHServiceClientV;
import fr.esir.sh.client.guicomponents.Commands;


public class ClientLauncher2 {
	
	public static void main(String[] args) {
		
		Logger logger= LoggerFactory.getLogger(SHServiceClientM.class);
	
		Commands commands= new Commands(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);
		SHServiceClientV shServiceClientV= new SHServiceClientV(2, commands);
		SHServiceClientM shServiceClient;
		
		try{
			
			shServiceClient = new SHServiceClientM(2, 2, 2, Color.CYAN, shServiceClientV, "localhost", 8090);
		}
		catch(RemoteException e){
			
			String errorMsg= "RemoteException occured. The clients are not initialized, this is probably due to the server that doensn't answer to the requests of the clients.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg, e);
		}
		
		shServiceClient.addSweetsAndDisplay();
	}
}
