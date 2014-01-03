package fr.esir.sh.server;

import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.esir.sh.client.SHService;

/**
 * This is a regression test of the server.
 */
public class SHServiceImplTest {

	static SHService shService;
	
	@BeforeClass
	public static void onlyOnce(){
		
		try {
			
			//Launch the server
			LocateRegistry.createRegistry(8090);
			shService = new SHServiceImpl();
			Naming.rebind("rmi://localhost:8090/SHService", shService);
			System.out.println("Server launched");
		}
		catch (RemoteException e) {
			
			throw new IllegalStateException("RemoteException. An error occured when launching the server, please check if it comes from the registry.", e);
		}
		catch (MalformedURLException e) {
			
			throw new IllegalStateException(
					"MalformedURLException occured. Please check if the host name and/or the port number and/or the name of the service is/are correct.",
					e);
			
		}
	}
	
	@Test
	public void testGetGridSize(){
		
		
		try {
			
			assertEquals(20, shService.getGridSize());	
		} catch (RemoteException e) {

			throw new IllegalStateException("RemoteException occured. " +
					"the grid size is not loaded, this number is initialized to 20", e);
			

		}
	}
	
	@Test
	public void testGetCellize(){
		
		try {
			
			assertEquals(20, shService.getCellSize());	
		} catch (RemoteException e) {

			throw new IllegalStateException("RemoteException occured. " +
					"the grid size is not loaded, this number is initialized to 20", e);
		}
	}
	
	@Test
	public void testGetNumberOfSweets(){
		
		
		try {

			assertEquals(10, shService.getNumberOfSweets());
		} catch (RemoteException e) {
			
			throw new IllegalStateException("RemoteException occured. Could not get the number of the sweets while testing the server.", e);
		}
	}
	
	@Test
	public void testGetLogicMap(){
		
		int numberOfSweets=0;
		
		try {
			
			boolean[][] landscape= shService.getLogicGameMap();
			
			for(int i= 0;i< shService.getGridSize(); ++i){
				for(int j= 0; j<shService.getGridSize(); ++j){
					
					if(landscape[i][j] == true)
						numberOfSweets++;
				}
			}
			
			assertEquals(numberOfSweets, shService.getNumberOfSweets());
			
		} catch (RemoteException e) {

			throw new IllegalStateException("RemoteException occured. Could not get the distribution of the sweets while testing the server.", e);
		}
	}
	
}
