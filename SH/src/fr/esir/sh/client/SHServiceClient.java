package fr.esir.sh.client;

import java.rmi.Naming;			//Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException;	//Import the RemoteException class so you can catch it
import java.net.MalformedURLException;	//Import the MalformedURLException class so you can catch it
import java.rmi.NotBoundException;	//Import the NotBoundException class so you can catch it

public class SHServiceClient {

    public static void main(String[] args) {
        
	try {

	    // Create the reference to the remote object through the remiregistry			
            SHService shService = (SHService)
                           Naming.lookup("rmi://localhost:8090/SHService");
            
	    // Now use the reference c to call remote methods
	    System.out.println("This is the result="+ shService.getHello("World"));
        }
        // Catch the exceptions that may occur - rubbish URL, Remote exception
	// Not bound exception or the arithmetic exception that may occur in 
	// one of the methods creates an arithmetic error (e.g. divide by zero)
	catch (MalformedURLException murle) {
            System.out.println();
            System.out.println("MalformedURLException");
            System.out.println(murle);
        }
        catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        }
        catch (NotBoundException nbe) {
            System.out.println();
            System.out.println("NotBoundException");
            System.out.println(nbe);
        }
        catch (java.lang.ArithmeticException ae) {
            System.out.println();
            System.out.println("java.lang.ArithmeticException");
            System.out.println(ae);
        }
    }
}