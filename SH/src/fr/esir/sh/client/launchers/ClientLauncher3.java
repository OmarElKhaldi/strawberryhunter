package fr.esir.sh.client.launchers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.client.SHServiceClientM;
import fr.esir.sh.client.SHServiceClientV;
import fr.esir.sh.client.guicomponents.Commands;


public class ClientLauncher3 {
	
	public static void main(String[] args) {
		
		Logger logger= LoggerFactory.getLogger(SHServiceClientM.class);
	
		Commands commands= new Commands(KeyEvent.VK_T, KeyEvent.VK_G, KeyEvent.VK_F, KeyEvent.VK_H);
		SHServiceClientV shServiceClientV= new SHServiceClientV(3, commands);
		SHServiceClientM shServiceClient;
		
		try{
			
			shServiceClient = new SHServiceClientM(3, 4, 6, Color.ORANGE, shServiceClientV, "localhost", 8090);
		}
		catch(RemoteException e){
			
			String errorMsg= "RemoteException occured. The clients are not initialized, this is probably due to the server that doensn't answer to the requests of the clients.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg, e);
		}
		
		shServiceClient.addSweetsAndDisplay();
	}
}