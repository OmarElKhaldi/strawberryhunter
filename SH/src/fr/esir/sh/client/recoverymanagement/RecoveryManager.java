package fr.esir.sh.client.recoverymanagement;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esir.sh.client.SHService;
import fr.esir.sh.client.SHServiceClientM;
import fr.esir.sh.server.SHServiceClient;

public class RecoveryManager{
	
	private SHServiceClient shServiceClient;
	private Logger logger= LoggerFactory.getLogger(SHServiceClientM.class);
	private SHService crashedService;
	private SHService primaryService;
	
	public RecoveryManager(SHServiceClient shServiceClient, SHService crashedService, char action){
	
		logger.info("Server crashed. Consequently, the recovery manager is called.");
		if(action == 'x')
			logger.warn("The last action sent by the client is not saved.");
		
		this.shServiceClient= shServiceClient;
		this.crashedService= crashedService;
		
		this.eraseServerFromGlobalList();		
		if(this.wasPrimary(crashedService))
			recoverFromPrimaryServer();
		else
			recoverFromBackupServer(shServiceClient);
		
		this.executeSavedAction(action);
	}
	
	private void recoverFromPrimaryServer(){
		
		this.setNewPrimaryService();
		this.notifyServersAboutNewPrimary();
	}
	
	private void recoverFromBackupServer(SHServiceClient shServiceClient){
		
		try {
			
			this.primaryService= shServiceClient.getSHService();
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the client to get it's primary service.";
			logger.error(errorMsg);
		}
		this.notifyAllClientsToEraseBackup();
	}
	
	private void setNewPrimaryService(){
		
		try{
			
			for(SHService shService : shServiceClient.getListLinksToServices()){
				
				if(!shService.equals(this.crashedService)){
					
					shService.setToNewPrimary();
					this.primaryService= shService;
					this.notifyAllClientsToChangePrimaryService(crashedService, primaryService);
					logger.info("The new primary server at the adress "+primaryService.getHostAdress()+":"+primaryService.getPort()+" is selected.");
					break;
				}
			}	
		}
		catch(RemoteException e){
			
			String errorMsg= "RemoteException occured. Could not reach the client to get it's globab services list.";
			logger.error(errorMsg);
		}

	}
	
	private void eraseServerFromGlobalList(){
		
		try{
			
			for(SHService shService : this.shServiceClient.getListLinksToServices()){
				
				if(!shService.equals(this.crashedService)){
					
					shService.removeService(this.crashedService);
				}
			}	
		}
		catch(RemoteException e){
			
			String errorMsg= "RemoteException occured. Could not reach the client to get it's globab services listww.";
			logger.error(errorMsg);
		}

	}
	
	private void notifyServersAboutNewPrimary(){
		
		try{
			
			for(SHService shService : shServiceClient.getListLinksToServices()){
			
				shService.setPrimary(this.primaryService);
			
			}
		}
		catch(RemoteException e){
			
			String errorMsg= "RemoteException occured. Could not reach the client to get it's globab services listaa.";
			logger.error(errorMsg);
		}

	}
	
	private void notifyAllClientsToChangePrimaryService(SHService oldPrimaryService, SHService newPrimaryService) throws RemoteException{
		
		newPrimaryService.notifyAllClientsToChangeOldToMe(oldPrimaryService);
	}
	
	private void notifyAllClientsToEraseBackup(){
		
		try {
			
			this.shServiceClient.getSHService().notifyAllClientsToEraseBackup(this.crashedService);
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the client to get it's primary services.";
			logger.error(errorMsg);
		}
	}
	
	private void executeSavedAction(char action){
				
			try {
				
				if (action == 'r')			primaryService.movePlayer(this.shServiceClient, 'r');
				else if (action == 'l')		primaryService.movePlayer(this.shServiceClient, 'l');
				else if (action == 'd')		primaryService.movePlayer(this.shServiceClient, 'd');
				else if (action == 'u')		primaryService.movePlayer(this.shServiceClient, 'u');
				
				logger.info("The action saved is now executed in the new primary server.");
			}
			catch (RemoteException e) {

				String errorMsg= "RemoteException occured. The recovery manager could not reach the new primary in order to execute the action saved.";
				logger.error(errorMsg);
				throw new IllegalStateException(errorMsg, e);
			}
	}
	
	
	private boolean wasPrimary(SHService crashedService){
		
		try {
			for(SHService service : this.shServiceClient.getListLinksToServices()){
				if(!service.equals(this.crashedService)){
					if(service.getIsPrimary()){
						return false;
					}
				}	
			}
		}
		catch (RemoteException e) {
			
			String errorMsg= "RemoteException occured. Could not reach the client to get it's globab services listxx.";
			logger.error(errorMsg);
		}
		return true;
	}
}