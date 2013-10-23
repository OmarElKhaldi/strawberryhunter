package fr.esir.sh.client;

public interface SHService extends java.rmi.Remote{
	
    public String getHello(String name) throws java.rmi.RemoteException;

}
