package fr.esir.sh.client;

import java.rmi.RemoteException;

public interface SHService extends java.rmi.Remote{

    public int getNumberOfSweets() throws RemoteException;
    
    public int getCellSize() throws RemoteException;
    
    public int getGridSize() throws RemoteException;
    
    public boolean[][] getLogicGameMap() throws RemoteException;
    
    public int[] movePlayerToRight() throws RemoteException;
    
    public int[] movePlayerToLeft() throws RemoteException;

    public int[] movePlayerToDown() throws RemoteException;
    
    public int[] movePlayerToUp() throws RemoteException;
    
    public int getPlayerXPos() throws RemoteException;
    
    public int getPlayerYPos() throws RemoteException;
    
}
