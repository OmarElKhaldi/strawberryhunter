package fr.esir.sh.server;

import java.net.MalformedURLException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.client.SHService;

public class SHServiceServer extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private int number;
	private String hostAdress;
	private int port;
	private SHService shService;
	private boolean isPrimary;
	private Logger logger= LoggerFactory.getLogger(SHServiceServer.class);
	private JLabel lblName;
	
	public SHServiceServer(String hostAdress, int port, boolean isPrimary, int number) {
	
		super("Server GUI");
		this.setName(isPrimary);
		this.setNumber(number);
		lblName= new JLabel("Server type:  ("+this.getName()+")   -   Number:  ("+this.getNumber()+")");
		this.setHostAdress(hostAdress);
		this.setPort(port);
		this.setIsPrimary(isPrimary);
		
		try {
			
			LocateRegistry.createRegistry(port);
			shService = new SHServiceImpl();
			Naming.rebind("rmi://"+hostAdress+":"+port+"/SHService", shService);
			shService.setServer(this);
		    logger.info(this.name+" server launched.");
		}
		catch (RemoteException e) {
			
			throw new IllegalStateException(
					"RemoteException occured. Verify if the reference of the service is not null.", e);
		}
		catch (MalformedURLException e) {
			
			throw new IllegalStateException(
					"MalformedURLException occured. Please check if the host name and/or the port number and/or the name of the service is/are correct.", e);
		}
		
		this.add(lblName);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(250, 100);
		this.setVisible(true);
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

	private int getNumber(){
		
		return this.number;
	}
	
	private void setNumber(int number){
		
		this.number= number;
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
	
	public void setToNewPrimary(){
		
		this.isPrimary= true;
		this.setName(this.isPrimary);
		this.lblName.setText("Server type:  ("+this.getName()+")   -   Number:  ("+this.getNumber()+")");
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
			
			logger.info("Server at ("+hostAdress+":"+port+") successfully linked to server at("+this.hostAdress+":"+this.port+")");
		}
		catch (MalformedURLException e) {
			
			String errorMsg= "MalformedURLException occured. Please check if the host name and/or the port number and/or the name of the service is/are correct";
			logger.error(errorMsg);
			throw new IllegalStateException( errorMsg ,e);
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. ("+this.getName()+") could not reach a server to add him in the server's set.";
			logger.error(errorMsg);
			throw new IllegalStateException( errorMsg ,e);
			
		}
		catch (NotBoundException e) {
			
			String errorMsg= "NotBoundException occured while lookimg up or unbinding in the registry a name that has no associated binding.";
			logger.error(errorMsg);
			throw new IllegalStateException( errorMsg ,e);
		}
	}

	public void loadServer() throws RemoteException{
		
		this.getSHService().addSweetsIfPrimary();
		this.getSHService().getPlayersFromPrimaryIfBackup();
		this.getSHService().getGameMapFromPrimaryIfBackup();
		logger.info(this.name+" server loaded.");
		
	}
}