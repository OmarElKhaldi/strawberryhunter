package fr.esir.sh.client;

import java.rmi.RemoteException;
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
    public void linkToServiceImpl(SHService shService) throws RemoteException;
    public void setLogicGameMap(boolean[][] gameMap) throws RemoteException;
    public void addSweetsIfPrimary() throws RemoteException;
}