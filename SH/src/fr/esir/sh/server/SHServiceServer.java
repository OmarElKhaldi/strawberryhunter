package fr.esir.sh.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import fr.esir.sh.client.SHService;

public class SHServiceServer{
	
	public SHServiceServer() {
	
		try {
			
			//Launch the server
			LocateRegistry.createRegistry(8090);
			SHService shService = new SHServiceImpl();
			Naming.rebind("rmi://localhost:8090/SHService", shService);
			System.out.println("Server launched");
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