package fr.esir.sh.server;

import java.rmi.RemoteException;

import fr.esir.sh.client.SHService;

public class SHServiceImpl extends java.rmi.server.UnicastRemoteObject implements SHService{


	private static final long serialVersionUID = 1L;

	public SHServiceImpl() throws java.rmi.RemoteException {
		
            super();
    }

	@Override
	public String getHello(String name) throws RemoteException {
		
		String result = "Hello "+name+" !";
		
		return result;
		
	}
	
}
