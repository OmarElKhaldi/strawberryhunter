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
	private String hostAdress;
	private int port;
	private SHService shService;
	private boolean isPrimary;
	private Logger logger= LoggerFactory.getLogger(SHServiceServer.class);
	
	public SHServiceServer(String hostAdress, int port, boolean isPrimary) {
	
		try {
			
			this.setName(isPrimary);
			this.setHostAdress(hostAdress);
			this.setPort(port);
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

	public String getHostAdress(){
		
		return this.hostAdress;
	}
	
	public void setHostAdress(String hostAdress){
		
		this.hostAdress= hostAdress;
	}
	
	public int getPort(){
		
		return this.port;
	}

	public void setPort(int port){
		
		this.port= port;
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
		
		
		try {
			SHService shService = (SHService) Naming.lookup("rmi://"+hostAdress+":"+port+"/SHService");
			shService.addService(this.getHostAdress(), this.getPort());
			if(shService.getIsPrimary())
				this.shService.addPrimaryIfBackup(shService);
		}
		catch (MalformedURLException e) {
			
			throw new IllegalStateException(
					"MalformedURLException occured. Please check if the host name and/or the port number and/or the name of the service is/are correct",
					e);
		}
		catch (RemoteException e) {
			
			throw new IllegalStateException(
					"RemoteException occured. ("+this.getName()+") could not reach a server to add him in the server's set.",
					e);
		}
		catch (NotBoundException e) {
			
			throw new IllegalStateException(
					"NotBoundException occured while lookimg up or unbinding in the registry a name that has no associated binding.",
					e);
		}
	}

	public void loadServer() throws RemoteException{
		
		this.getSHService().addSweetsIfPrimary();
		this.getSHService().getPlayersFromPrimaryIfBackup();
	}
}