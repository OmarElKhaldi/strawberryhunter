package fr.esir.sh.client.recoverymanagement;

import java.rmi.RemoteException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.client.SHService;
import fr.esir.sh.client.SHServiceClientM;
import fr.esir.sh.server.SHServiceClient;

public class RecoveryManager{
	
	private SHServiceClient shServiceClient;
	private Logger logger= LoggerFactory.getLogger(SHServiceClientM.class);
	private SHService oldPrimaryService;
	private SHService newPrimaryService;
	
	public RecoveryManager(SHServiceClient sHServiceClient){
	
		logger.info("The primary server crashed. Consequently, the recovery manager is created to select a new server as the primary.");
		try {
	
			this.shServiceClient= sHServiceClient;
			this.oldPrimaryService= this.shServiceClient.getSHService();
			this.setNewPrimaryService();
			this.erasePrimaryFromAllOldServers();
			notifyServersAboutNewPrimary();
		}
		catch (RemoteException e1) {
			
			String errorMsg= "RemoteException occured. The recovery manager could not reach the backup server in order to change it into primary.";
			logger.error(errorMsg);
			throw new IllegalStateException(errorMsg, e1);
		}
	}
	
	private void setNewPrimaryService() throws RemoteException{
		
		for(SHService shService : shServiceClient.getListLinksToServices()){
			
			if(shService != null){
				
				shService.setToNewPrimary();
				this.newPrimaryService= shService;
				this.notifyAllClientsToChangePrimaryService(oldPrimaryService, newPrimaryService);
				logger.info("The new primary server at the adress "+newPrimaryService.getHostAdress()+":"+newPrimaryService.getPort()+" is selected.");
				break;
			}
		}	
	}
	
	private void erasePrimaryFromAllOldServers() throws RemoteException{
		
		for(SHService shService : this.shServiceClient.getListLinksToServices()){
			
			if(shService != null){
				
				shService.removeService(this.oldPrimaryService);
			}
		}	
	}
	
	private void notifyServersAboutNewPrimary() throws RemoteException{
		
		for(SHService shService : shServiceClient.getListLinksToServices()){
			
			if(shService != null){
				
				shService.setPrimary(this.newPrimaryService);
			}
		}
	}
	
	private void notifyAllClientsToChangePrimaryService(SHService oldPrimaryService, SHService newPrimaryService) throws RemoteException{
		
		newPrimaryService.notifyAllClientsToChangeOldToMe(oldPrimaryService);
	}
	
	//If the next element is alive (!= null)
	//He becomes primary
		//Change the boolean
		//Change SHServiceClient.listServers
		//Change from the model and the view of the client the reference of the primary server.
		//Change SHServiceClient.serverHostAdress
		//Change SHServiceClient.serverPort
		//Notify all the servers that pointed into the primary server to point into the new one.
		//Add the new primary into the SHServiceImpl.primaryService for all those who use to point into the old one.
		//
}
