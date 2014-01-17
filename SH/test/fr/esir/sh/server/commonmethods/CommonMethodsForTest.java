package fr.esir.sh.server.commonmethods;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

import fr.esir.sh.client.SHServiceClientM;
import fr.esir.sh.client.SHServiceClientV;
import fr.esir.sh.client.guicomponents.Commands;


public class CommonMethodsForTest {
	
	public static void launchClients(){
		
		String serverHostAdress= "localhost";
		int serverPort= 8090;
		
		Commands commands1= new Commands(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
		Commands commands2= new Commands(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);		
		Commands commands3= new Commands(KeyEvent.VK_T, KeyEvent.VK_G, KeyEvent.VK_F, KeyEvent.VK_H);
		
		SHServiceClientV shServiceClientV1= new SHServiceClientV(1, commands1);
		SHServiceClientV shServiceClientV2= new SHServiceClientV(2, commands2);
		SHServiceClientV shServiceClientV3= new SHServiceClientV(3, commands3);
		
		try {
			
			SHServiceClientM shServiceClient1 = new SHServiceClientM(1, 0, 0, Color.BLUE, shServiceClientV1, serverHostAdress, serverPort);
			SHServiceClientM shServiceClient2 = new SHServiceClientM(2, 2, 2, Color.MAGENTA, shServiceClientV2, serverHostAdress, serverPort);
			SHServiceClientM shServiceClient3 = new SHServiceClientM(3, 4, 6, Color.GREEN, shServiceClientV3, serverHostAdress, serverPort);
			
			//Make sure that all the players are added in the map, so the sweets can not take their places.
			shServiceClient1.addSweetsAndDisplay();
			shServiceClient2.addSweetsAndDisplay();
			shServiceClient3.addSweetsAndDisplay();
		}
		catch (RemoteException e) {

			throw new IllegalStateException("RemoteException occured. The clients are not initialized, this is probably due to the server that doensn't answer to the requests of the clients.", e);
		}
	}
}
