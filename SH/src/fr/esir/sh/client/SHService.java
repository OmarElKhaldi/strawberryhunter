package fr.esir.sh.client;

import java.rmi.RemoteException;
import fr.esir.sh.server.Player;

public interface SHService extends java.rmi.Remote{

    public int getNumberOfSweets() throws RemoteException;
    
    public int getCellSize() throws RemoteException;
    
    public int getGridSize() throws RemoteException;
    
    public boolean[][] getLogicGameMap() throws RemoteException;
    
    public void movePlayerToRight() throws RemoteException;
    
    public void movePlayerToLeft() throws RemoteException;

    public void movePlayerToDown() throws RemoteException;
    
    public void movePlayerToUp() throws RemoteException;
    
    public int getPlayerXPos() throws RemoteException;
    
    public int getPlayerYPos() throws RemoteException;
    
}
