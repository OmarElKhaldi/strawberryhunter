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
	
	public RecoveryManager(SHServiceClient sHServiceClient, char action){
	
		logger.info("The primary server crashed. Consequently, the recovery manager is created to select a new server as the primary.");
		if(action == 'x')
			logger.warn("The last action sent by the client is not saved.");
		
		try {
	
			this.shServiceClient= sHServiceClient;
			this.oldPrimaryService= this.shServiceClient.getSHService();
			this.setNewPrimaryService();
			this.erasePrimaryFromAllOldServers();
			this.notifyServersAboutNewPrimary();
			this.executeSavedAction(action);
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
	
	private void executeSavedAction(char action){
				
			try {
				
				if (action == 'r')			newPrimaryService.movePlayer(this.shServiceClient, 'r');
				else if (action == 'l')		newPrimaryService.movePlayer(this.shServiceClient, 'l');
				else if (action == 'd')		newPrimaryService.movePlayer(this.shServiceClient, 'd');
				else if (action == 'u')		newPrimaryService.movePlayer(this.shServiceClient, 'u');
				
				logger.info("The action saved is now executed in the new primary server.");
			}
			catch (RemoteException e) {

				String errorMsg= "RemoteException occured. The recovery manager could not reach the new primary in order to execute the action saved.";
				logger.error(errorMsg);
				throw new IllegalStateException(errorMsg, e);
			}
	}
}