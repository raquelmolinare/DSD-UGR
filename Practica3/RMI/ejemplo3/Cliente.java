// Fichero: cliente.java
// Código del cliente

import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;
import java.rmi.*;
import java.rmi.registry.Registry;

public class Cliente {
    public static void main( String[] args) {
        //Se instala el gestor de seguridad
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }

        try{
            //Crea el stub para el cliente especificando el nombre del servidor
            Registry mireg = LocateRegistry.getRegistry("127.0.0.1", 1099);
            
            Icontador micontador = (Icontador)mireg.lookup("mmicontador");

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

        }
        catch(NotBoundException | RemoteException e){
            System.err.println("Exception del sistema: "+ e);
        }
        System.exit(0);
    }
}