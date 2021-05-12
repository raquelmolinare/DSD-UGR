// cliente.java
package cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.*;
import java.rmi.registry.Registry;
import java.util.Scanner;

import idonacion.idonacion;

public class cliente {
    
    //Variables información cliente
    private String cliente;
    private int numReplica=-1;
    private static idonacion  midonacionPrimera;
    private static idonacion midonacionDefinitiva;
    
    public static void main( String[] args) {
        
        String host = "127.0.0.1";
        Scanner teclado = new Scanner (System.in);
        //System.out.print ("Escriba el nombre o IP del servidor: ");
        //host = teclado.nextLine();
        
        
        //Se instala el gestor de seguridad
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }

        try{
            //Crea el stub para el cliente especificando el nombre del servidor
            System.out.println("Buscando al servidor...");
            Registry mireg = LocateRegistry.getRegistry(host, 1099);
            
            midonacionPrimera = (idonacion)mireg.lookup("replica2");
            
            //-------------------MENU---------------
            
            String nombreReplica = midonacionPrimera.registrar("Raquel");
            
            idonacion midonacionDefinitiva = (idonacion)mireg.lookup(nombreReplica);
            midonacionDefinitiva.donar("Ana",50);
            midonacionDefinitiva.donar("Raquel",100);
            System.out.println("El total donado en esta replica ha sido "+midonacionDefinitiva.getCantidadTotal());            
            
            System.out.println("El total donado por Raquel ha sido "+midonacionDefinitiva.getCantidadCliente("Raquel"));            
            System.out.println("El total donado por Ana ha sido "+midonacionDefinitiva.getCantidadCliente("Ana"));



        }
        catch(NotBoundException | RemoteException e){
            System.err.println("Exception del sistema: "+ e);
        }
        System.exit(0);
    }
}