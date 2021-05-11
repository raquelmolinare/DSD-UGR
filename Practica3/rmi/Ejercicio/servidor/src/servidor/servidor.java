//servidor.java

package servidor;

import iservidor.iservidor;
import donacion.donacion;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Iterator;

public class servidor{
    
    //Variables del servidor
    private static HashMap<String, donacion> replicas; 

    public servidor() {
        replicas = new HashMap<>();
    }
    
    public static void main(String[] args){
        /*
        //1. Crear e instalr un gestor de seguridad si no lo está ya
        if(System.getSecurityManager() == null){ 
            System.setSecurityManager(new SecurityManager());
        }*/

        //try{
            
            //2. Crear y exportar los objetos remotos => una instancia de donacion
            //Replica 1
            /*Registry reg1 = LocateRegistry.createRegistry(1099);
            donacion midonacion1 = new donacion();
            Naming.rebind("mmidonacion1", midonacion1);
            
            //Replica 2
            Registry reg2 = LocateRegistry.createRegistry(1100);
            donacion midonacion2 = new donacion();
            Naming.rebind("mmidonacion2", midonacion2);
            
            //Añadimos las replicas en el array
            replicas.put("mmidonacion1", midonacion1);
            replicas.put("mmidonacion2", midonacion2);*/
            
            //Replica1
            replica r1 = new replica("r1");
            r1.ejecucion();
            
            System.out.println("Servidor RemoteException | MalformedURLExceptiondor preparado");
        /*}
        catch(RemoteException | MalformedURLException e){
            System.out.println("Exception: "+ e.getMessage());
        }*/
    }
}