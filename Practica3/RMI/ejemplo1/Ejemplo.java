// Fichero: Ejemplo.java
// Implementa la interfaz remota

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.lang.Thread;

public class Ejemplo implements Ejemplo_I{
    public Ejemplo(){
        super();
    }

    //Implementacion de la interfaz
    public void escribir_mensaje(int id_proceso){
        //Se recibe una peticion
        System.out.println("Recibida peticion de proceso: "+id_proceso);

        //Si ese proceso tiene id 0
        if(id_proceso == 0){
            //Se duerme durante 5 segundos
            try{
                System.out.println("Empezamos a dormir");
                Thread.sleep(5000);
                System.out.println("Terminamos de dormir");
            }
            catch(Exception e){
                System.err.println("Ejemplo de exeption:");
                e.printStackTrace();
            }
        }
        //Se muestra el id de la hebra o proceso
        System.out.println("\nHebra "+id_proceso);
    }

    public static void main(String[] args){
        // Una vex que se implementa el objeto sermoto se necesita crear el objeto remoto inicial
        // y exportar al entorno RMI para habilitar la recepción de invocaciones remotas

        //1. Crear e instalr un gestor de seguridad si no lo está ya
        if(System.getSecurityManager() == null){ 
            System.setSecurityManager(new SecurityManager());
        }

        try{
            //2. Crear y exportar los objetos remotos
            String nombre_objeto_remoto = "Ejemplo_I"; 
            Ejemplo_I prueba = new Ejemplo(); //Se crea una instancia
            Ejemplo_I stub = (Ejemplo_I) UnicastRemoteObject.exportObject(prueba,0); //0 -> puerto anonimo (se selecciona en tiempo de ejecucion)

            //3. Registrar objeto remoto en el registro RMI
            Registry registry = LocateRegistry.getRegistry();
            //Se exporta y se le da un nombre con el que identificarla en el RMI registry
            registry.rebind(nombre_objeto_remoto,stub); 
            System.out.println("Ejemplo bound");
        }
        catch(Exception e){
            System.err.println("Ejemplo de exeption:");
            e.printStackTrace();
        }

    }
}