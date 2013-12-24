package fr.esir.sh.client;

import java.rmi.RemoteException;
import fr.esir.sh.server.SHServiceClient;

public interface SHService extends java.rmi.Remote{
    
    public int getCellSize() throws RemoteException;
    public int getGridSize() throws RemoteException;
    public int getNumberOfSweets() throws RemoteException;
    public boolean[][] getLogicGameMap() throws RemoteException;
    public void addNewPlayer(SHServiceClient shServiceClient, int id) throws RemoteException;
    public void movePlayer(SHServiceClient shServiceClientImpl, char movement) throws RemoteException;
}
