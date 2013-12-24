package fr.esir.sh.client;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.List;

import fr.esir.sh.server.SHServiceClientImpl;

public interface SHService extends java.rmi.Remote{
    
    public int getCellSize() throws RemoteException;
    public int getGridSize() throws RemoteException;
    public int getNumberOfSweets() throws RemoteException;
    public boolean[][] getLogicGameMap() throws RemoteException;
    public void addNewPlayer(SHServiceClientImpl shServiceClient, int id) throws RemoteException;
    public void movePlayer(SHServiceClientImpl shServiceClientImpl, char movement) throws RemoteException;
}
