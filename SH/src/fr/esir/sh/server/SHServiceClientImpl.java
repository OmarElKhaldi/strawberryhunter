package fr.esir.sh.server;

import java.awt.Color;
import java.rmi.RemoteException;

import fr.esir.sh.client.SHService;
import fr.esir.sh.client.SHServiceClientV;
import fr.esir.sh.client.guicomponents.Rectangle;

public interface SHServiceClientImpl extends java.rmi.Remote{

	public int getX() throws RemoteException;
	public int getY() throws RemoteException;
	public Color getColor() throws RemoteException;
	public void addRectangle(int id, int x, int y, Color color) throws RemoteException;
	public int getClientId() throws RemoteException;
	public SHService getSHService() throws RemoteException;
	public Rectangle getMyRectangle() throws RemoteException;
	public SHServiceClientV getView() throws RemoteException;
	public void getPointAndChange(Color color, int result[]) throws RemoteException;
	//public void addPoint(int x, int y) throws RemoteException;
	//public void initializeMyPoint(int x, int y) throws RemoteException;
	public void addScore() throws RemoteException;
	public int getScore() throws RemoteException;
	public void removeSweetFromServer(int x, int y) throws RemoteException;
}