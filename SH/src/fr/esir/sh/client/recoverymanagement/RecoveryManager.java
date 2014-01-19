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
		
			this.shServiceClient= sHServiceClient;
			try{
				
				this.oldPrimaryService= this.shServiceClient.getSHService();
			}
			catch(RemoteException e){
				
				e.printStackTrace();
			}
			this.erasePrimaryFromAllOldServers();
			this.setNewPrimaryService();
			this.notifyServersAboutNewPrimary();
			this.executeSavedAction(action);
	}
	
	private void setNewPrimaryService(){
		
		try{
			
			
			
			for(SHService shService : shServiceClient.getListLinksToServices()){
				
				if(!shService.equals(this.oldPrimaryService)){
					
					shService.setToNewPrimary();
					this.newPrimaryService= shService;
					this.notifyAllClientsToChangePrimaryService(oldPrimaryService, newPrimaryService);
					logger.info("The new primary server at the adress "+newPrimaryService.getHostAdress()+":"+newPrimaryService.getPort()+" is selected.");
					break;
				}
			}	
		}
		catch(RemoteException e){
			
			e.printStackTrace();
		}

	}
	
	private void erasePrimaryFromAllOldServers(){
		
		try{
			
			for(SHService shService : this.shServiceClient.getListLinksToServices()){
				
				if(!shService.equals(this.oldPrimaryService)){
					
					shService.removeService(this.oldPrimaryService);
				}
			}	
		}
		catch(RemoteException e){
			
			e.printStackTrace();
		}

	}
	
	private void notifyServersAboutNewPrimary(){
		
		try{
			
			for(SHService shService : shServiceClient.getListLinksToServices()){
				
				if(shService != null){
					
					shService.setPrimary(this.newPrimaryService);
				}
			}
		}
		catch(RemoteException e){
			
			e.printStackTrace();
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