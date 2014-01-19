package fr.esir.sh.server;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.List;

import fr.esir.sh.client.SHService;
import fr.esir.sh.client.SHServiceClientV;
import fr.esir.sh.client.guicomponents.Rectangle;

public interface SHServiceClient extends java.rmi.Remote{

	public int getX() throws RemoteException;
	public int getY() throws RemoteException;
	public Color getColor() throws RemoteException;
	public void addRectangle(int id, int x, int y, Color color) throws RemoteException;
	public int getClientId() throws RemoteException;
	public SHService getSHService() throws RemoteException;
	public Rectangle getMyRectangle() throws RemoteException;
	public SHServiceClientV getView() throws RemoteException;
	public void getPointAndChange(int rectId, int result[]) throws RemoteException;
	//public void addPoint(int x, int y) throws RemoteException;
	//public void initializeMyPoint(int x, int y) throws RemoteException;
	public void addScore() throws RemoteException;
	public int getScore() throws RemoteException;
	public void removeSweetFromServer(int x, int y) throws RemoteException;
	//public void addServer(SHService shService) throws RemoteException;
	public String getServerHostAdress() throws RemoteException;
	public int getServerPort() throws RemoteException;
	public List<SHService> getListLinksToServices() throws RemoteException;
	public void removeOldService(SHService oldPrimaryService) throws RemoteException;
	public void addNewService(SHService newPrimaryService)throws RemoteException;
	public void addNewServiceIntoList(SHService newPrimaryService)throws RemoteException;
	public void refreshServicesList(List<SHService> listServices) throws RemoteException;
	public void removeBackup(SHService crashedBackup) throws RemoteException;
}
