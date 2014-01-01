package fr.esir.sh.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.client.SHService;

public class SHServiceServer{
	
	private String name;
	private Logger logger= LoggerFactory.getLogger(SHServiceServer.class);
	private SHService shService;
	private boolean isPrimary;
	
	public SHServiceServer(String hostAdress, int port, boolean isPrimary) {
	
		try {
			
			this.setName(isPrimary);
			this.setIsPrimary(isPrimary);
			LocateRegistry.createRegistry(port);
			shService = new SHServiceImpl();
			Naming.rebind("rmi://"+hostAdress+":"+port+"/SHService", shService);
			shService.setServer(this);
		    logger.info(this.name+" server launched");
		}
		catch (RemoteException e) {
			
			throw new IllegalStateException(
					"RemoteException occured. Verify if the reference of the service is not null.", e);
		}
		catch (MalformedURLException e) {
			
			throw new IllegalStateException(
					"MalformedURLException occured. Please check if the host name and/or the port number and/or the name of the service is/are correct.", e);
		}
		
	}
	
	public String getName(){
		
		return this.name;
	}
	
	private void setName(boolean isPrimary){
		
		if(isPrimary)
			this.name= "Primary";
		
		else
			this.name= "Backup";
	}
	
	public SHService getSHService(){
		
		return this.shService;
	}
	
	public boolean getIsPrimary(){
		
		return this.isPrimary;
	}
	
	public void setIsPrimary(boolean isPrimary){
		
		this.isPrimary= isPrimary;
	}
	
	public void linkToServer(String hostAdress, int port){
		
		try{
			
			SHService shService = (SHService) Naming.lookup("rmi://"+hostAdress+":"+port+"/SHService");
			this.shService.linkToServiceImpl(shService);
			logger.info("("+this.name+") server is linked to ("+shService.getServerName()+")");
		}
		catch(NotBoundException e){
			
			throw new IllegalStateException(
					"NotBoundException occured while lookimg up or unbinding in the registry a name that has no associated binding.",
					e);
		}
		catch(RemoteException e){
			
			throw new IllegalStateException(
					"RemoteException occured. Maybe an exception occured during the execution of a remote method call",
					e);
		}
		catch(MalformedURLException e){
			
			throw new IllegalStateException(
					"MalformedURLException occured. Please check if the host name and/or the port number and/or the name of the service is/are correct",
					e);
		}
	}
	
	public static void main(String args[]) {
	
		Logger logger= LoggerFactory.getLogger(SHServiceServer.class);
		
		
		
		boolean isPrimary= false;
		String hostAdress= "localhost";
		int port= 8091;	
		SHServiceServer shServiceServer= new SHServiceServer(hostAdress, port, isPrimary);
		
		isPrimary= true;
		port= 8090;
		SHServiceServer shServiceServer1= new SHServiceServer(hostAdress, port, isPrimary);
		shServiceServer1.linkToServer("localhost", 8091);
		try {
			
			shServiceServer.getSHService().addSweetsIfPrimary();
			shServiceServer1.getSHService().addSweetsIfPrimary();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the method that fills the primary server with sweets.";
			logger.error(errorMsg);
			throw new IllegalStateException();
		}
	}	
}