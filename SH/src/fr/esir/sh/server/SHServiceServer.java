package fr.esir.sh.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import fr.esir.sh.client.SHService;

public class SHServiceServer {
	
	public SHServiceServer() {
	
		try {
			
			LocateRegistry.createRegistry(8090);
			
		}
		catch (RemoteException e) {
			
			throw new IllegalStateException(
					"RemoteException occured. The port number may be used. Try to use another port. "
							+ "If it's the case, change it in the rebind method below as well as for the client side.",
					e);
			
		}
		
		SHService shService;
		
		try {
			
			shService = new SHServiceImpl();
			
		}
		catch (RemoteException e) {
			
			throw new IllegalStateException(
					"RemoteException occured. Check if the instance of the SHServiceImpl is not null",
					e);
			
		}
		
		try {
			
			Naming.rebind("rmi://localhost:8090/SHService", shService);
			
		}
		catch (RemoteException e) {
			
			throw new IllegalStateException(
					"RemoteException occured. Verify if the reference of the service is not null.",
					e);
			
		}
		catch (MalformedURLException e) {
			
			throw new IllegalStateException(
					"MalformedURLException occured. Please check if the host name and/or the port number and/or the name of the service is/are correct.",
					e);
			
		}
		
	}
	
	public static void main(String args[]) {
	
		new SHServiceServer();
		
	}
	
}