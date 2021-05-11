/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import donacion.donacion;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author raquelmolire
 */
public class replica {
    
    private static String nombre;

    public replica(String nombre) {
        this.nombre= nombre;
    }
    
    public static void ejecucion(){
        //1. Crear e instalr un gestor de seguridad si no lo está ya
        if(System.getSecurityManager() == null){ 
            System.setSecurityManager(new SecurityManager());
        }

        try{
            
            //2. Crear y exportar los objetos remotos => una instancia de donacion
            //Replica
            Registry reg1 = LocateRegistry.createRegistry(1099);
            donacion midonacion = new donacion();
            Naming.rebind(nombre, midonacion);       
        }
        catch(RemoteException | MalformedURLException e){
            System.out.println("Exception: "+ e.getMessage());
        }
    }
    
}
