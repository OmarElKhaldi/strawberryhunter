package fr.esir.sh.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import fr.esir.sh.client.SHService;

public class SHServiceServer{

   public SHServiceServer() {

     try{
       	
    	 LocateRegistry.createRegistry(8090);
    	 
    	 SHService shService = new SHServiceImpl();
     
    	 Naming.rebind("rmi://localhost:8090/SHService", shService);
     
     } 
     catch (Exception e) {
       
    	 System.out.println("Server Error: " + e);
     
     }
   
   }

   public static void main(String args[]) {
     	
	   new SHServiceServer();

   }

}