// Fichero: Cliente_Ejemplo.java
// Codigo del cliente

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente_Ejemplo_Multi_Threaded implements Runnable{

    public String nombre_objeto_remoto = "Ejemplo_I"; //Mismo nombre que la instancia de la clase Ejemplo
    public String server;

    public Cliente_Ejemplo_Multi_Threaded( String server){
        //Se almacena el nombre del host servidor
        this.server = server;
    }

    public void run(){
        System.out.println("Buscando el objeto remoto");

        try{

            //Obtiene una referencia al RMi registry EN LA MAQUINA DEL SERVIDOR
            Registry registry = LocateRegistry.getRegistry(server);
            //Se invoca el objeto remoto y se busca en el rmiregistry
            Ejemplo_I instancia_local = (Ejemplo_I) registry.lookup(nombre_objeto_remoto); 

            System.out.println("Invocando el objeto remoto");

            instancia_local.escribir_mensaje( Thread.currentThread().getName() ); //Se llama a los métodos remotos
        }
        catch(Exception e){
            System.err.println("Ejemplo_I de exeption:");
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        //Se instala el gestor de seguridad
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }

        //Se obtiene el numero de hebras pasado por parametro
        int n_hebras = Integer.parseInt(args[1]);
        
        //Se crea un vector de clientes
        Cliente_Ejemplo_Multi_Threaded[] v_clientes = new Cliente_Ejemplo_Multi_Threaded[n_hebras];
        
        //Se crea un vector de hebras
        Thread[] v_hebras = new Thread[n_hebras];

        for(int i=0; i < n_hebras; i++){
            //A cada hebra se le pasa el nombre del servidor
            v_clientes[i] = new Cliente_Ejemplo_Multi_Threaded(args[0]);

            //Cada hebra llama a la funcion Cliente_Ejemplo_Multi_Threaded que tiene el nombre del servidor
            // y el nombre de la hebra ("Cliente "+i ) que se usará en el rmetodo escribir_mensaje
            v_hebras[i] = new Thread(v_clientes[i], "Cliente "+i);
            v_hebras[i].start();
        }

    }

}