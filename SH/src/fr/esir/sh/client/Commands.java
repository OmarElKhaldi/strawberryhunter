package fr.esir.sh.client;

import java.io.Serializable;


public class Commands implements Serializable{
	
	private int up;
	private int down;
	private int left;
	private int right;
	
	public Commands(int up, int down, int left, int right){
		
		this.up= up;
		this.down= down;
		this.left= left;
		this.right= right;
	}

	
	public int getUp() {
	
		return up;
	}

	
	public int getDown() {
	
		return down;
	}

	
	public int getLeft() {
	
		return left;
	}

	
	public int getRight() {
	
		return right;
	}
}
