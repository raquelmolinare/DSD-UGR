//servidor.java

package servidor;

import donacion.donacion;
import iservidor.iservidor;

import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.rmi.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class servidor{
    
    //Variables del servidor
    private static String nombre;
    private static int puerto;
    

    public servidor() {
    }
    
    public static void main(String[] args){
        String host = "127.0.0.1";

        if(System.getSecurityManager() == null){ 
            System.setSecurityManager(new SecurityManager());
        }

        try{
            Scanner teclado = new Scanner (System.in);
            System.out.println("¿Es el primer servidor que se inicia?");
            System.out.println("  0: No");
            System.out.println("  1: Si");
            System.out.print("  --Introducce una opción: ");
            int opcion = teclado.nextInt();

            boolean inicial=false;
            replicas misreplicas = null;
            iservidor miiservidor = null;

            //PARA LA COMUNICACIÓN SERVIDOR-SERVIDOR
            //Si es el servidor de inicializacion este crea el registry
            if(opcion == 1){
                inicial=true;

                Registry mireg = LocateRegistry.createRegistry(1099);
                misreplicas = new replicas();
                mireg.bind("comunicacionSS", misreplicas);

            }
            
            //Crea el stub para el cliente especificando el nombre del servidor
            Registry mireg1 = LocateRegistry.getRegistry(host, 1099);
            miiservidor= (iservidor)mireg1.lookup("comunicacionSS");
            
            
            //PARA LA COMUNICACIÓN CLIENTE-SERVIDOR
            //Crear el canal para la comunicacion con los clientes
            if( inicial ){
                
                //Replica
                nombre="replica1";
                puerto=1100;
                Registry mireg2 = LocateRegistry.createRegistry(puerto);
                donacion midonacion1 = new donacion(host,nombre, puerto, inicial);
                mireg2.rebind(nombre, midonacion1);

                miiservidor.addReplica(host,nombre,puerto);
                
            }
            else{
                System.out.print ("Puerto : ");
                puerto = teclado.nextInt();
                System.out.print ("Nombre : ");
                nombre = teclado.next();
                
                //Replica
                Registry mireg2 = LocateRegistry.createRegistry(puerto);
                donacion midonacion1 = new donacion(host,nombre, puerto, inicial);
                mireg2.rebind(nombre, midonacion1);

                miiservidor.addReplica(host,nombre,puerto);   
            }
            
            System.out.println("\n => Servidor "+nombre+" preparado!\n");
        }
        catch (AlreadyBoundException | NotBoundException | AccessException ex ) {
        //catch (AlreadyBoundException ex ) {
                Logger.getLogger(servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(RemoteException e){
            System.out.println("Exception: "+ e.getMessage());
        }
        
        
    }

}