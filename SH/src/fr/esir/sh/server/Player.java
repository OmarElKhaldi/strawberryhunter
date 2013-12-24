package fr.esir.sh.server;

import java.rmi.RemoteException;

public class Player{

	private int x;
	private int y;
	private SHServiceClient shServiceClientM;
	
	public Player(SHServiceClient shServiceClientM){
		
		this.shServiceClientM = shServiceClientM;
		
		try{
			
			this.x = this.shServiceClientM.getX();
			this.y = this.shServiceClientM.getY();
		}
		catch(RemoteException re){
			
			throw new IllegalStateException("RemoteException. Could not get the coordinates from the client. " +
					                         "They are initialized to 0");
		}
	}
	
	public int getX(){
		
		return this.x;
	}
	
	public int getY(){
		
		return this.y;
	}
	
	public void setX(int x) {
	
		this.x = x;
	}

	public void setY(int y) {
	
		this.y = y;
	}

	public SHServiceClient getShServiceClientM() {
		
		return shServiceClientM;
	}
}
