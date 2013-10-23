package fr.esir.sh.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SHServiceClient {
	
	public static void main(String[] args) {
	
		try {
			
			// Create the reference to the remote object through the
			// remiregistry
			SHService shService = (SHService) Naming
					.lookup("rmi://localhost:8090/SHService");
			
			// Now use the reference c to call remote methods
			System.out.println("This is the result="
					+ shService.getHello("World"));
		}
		catch (MalformedURLException e) {
			
			throw new IllegalStateException(
					"MalformedURLException occured. Please check if the host name and/or the port number and/or the name of the service is/are correct",
					e);
			
		}
		catch (RemoteException e) {
			
			throw new IllegalStateException(
					"MalformedURLException occured. Maybe an exception occured during the execution of a remote method call",
					e);
			
		}
		catch (NotBoundException e) {
			
			throw new IllegalStateException(
					"NotBoundException occured while lookimg up or unbinding in the registry a name that has no associated binding.",
					e);
			
		}
	}
}