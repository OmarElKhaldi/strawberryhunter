package fr.esir.sh.client;

import java.rmi.RemoteException;
import java.util.List;

import fr.esir.sh.server.SHServiceClient;
import fr.esir.sh.server.SHServiceImpl;
import fr.esir.sh.server.SHServiceServer;

public interface SHService extends java.rmi.Remote{
    
    public int getCellSize() throws RemoteException;
    public int getGridSize() throws RemoteException;
    public int getNumberOfSweets() throws RemoteException;
    public boolean[][] getLogicGameMap() throws RemoteException;
    public void addNewPlayer(SHServiceClient shServiceClient, int id) throws RemoteException;
    public void movePlayer(SHServiceClient shServiceClientImpl, char movement) throws RemoteException;
    public void setServer(SHServiceServer shServiceServer) throws RemoteException;
    public String getServerName() throws RemoteException;
    public void setLogicGameMap(boolean[][] gameMap) throws RemoteException;
    public void addSweetsIfPrimary() throws RemoteException;
    public void addService(String hostAdress, int port) throws RemoteException;
    public List<SHServiceClient> getListClients() throws RemoteException;
    public void getPlayersFromPrimaryIfBackup() throws RemoteException;
    public boolean getIsPrimary() throws RemoteException;
    public void addPrimaryIfBackup(SHService shService) throws RemoteException;
    public boolean[][] getGameMap() throws RemoteException;
    public void getGameMapFromPrimaryIfBackup() throws RemoteException;
    public List<SHService> getListServices() throws RemoteException;
    public void setToNewPrimary() throws RemoteException;
    public void removeService(SHService shService) throws RemoteException;
    public void setPrimary(SHService newPrimaryService) throws RemoteException;
    public void notifyAllClientsToChangeOldToMe(SHService oldPrimaryService) throws RemoteException;
    public String getHostAdress() throws RemoteException;
    public int getPort() throws RemoteException;
    public void notifyAllClientsToEraseBackup(SHService crashedBackup) throws RemoteException;
    public void refreshServicesList(List<SHService> primaryServiceList) throws RemoteException;
}