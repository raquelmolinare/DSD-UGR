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
            
            idonacion midonacion = (idonacion)mireg.lookup("r1");

            System.out.println("Primer cliente");
            midonacion.registrar("Raquel");
            midonacion.donar("Ana",50);
            midonacion.donar("Raquel",100);
            System.out.println("El total donado en esta replica ha sido "+midonacion.getCantidadTotal());            
            
            System.out.println("El total donado por Raquel ha sido "+midonacion.getCantidadCliente("Raquel"));            
            System.out.println("El total donado por Ana ha sido "+midonacion.getCantidadCliente("Ana"));



            
            /*
            // Pone el contador al valor inicial 0
            System.out.println("Poniendo contador a 0");
            micontador.sumar(0);

            // Obtiene hora de comienzo
            long horacomienzo = System.currentTimeMillis();

            // Incrementa 1000 veces
            System.out.println("Incrementando...");
            for(int i = 0; i < 1000; i++ ) {
                micontador.incrementar();
            }

            // Obtiene hora final, realiza e imprime calculos
            long horafin = System.currentTimeMillis();
            System.out.println("Media de las RMI realizadas = "+ ((horafin -horacomienzo)/1000f)+ " msegs");
            System.out.println("RMI realizadas = "+ micontador.sumar());
            */

        }
        catch(NotBoundException | RemoteException e){
            System.err.println("Exception del sistema: "+ e);
        }
        System.exit(0);
    }
}