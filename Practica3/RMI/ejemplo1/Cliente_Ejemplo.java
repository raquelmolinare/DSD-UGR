// Fichero: Cliente_Ejemplo.java
// Codigo del cliente

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente_Ejemplo{

    public static void main(String args[]){
        //Se instala el gestor de seguridad
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }

        try{
            String nombre_objeto_remoto = "Ejemplo_I"; //Mismo nombre que la instancia de la clase Ejemplo
            System.out.println("Buscando el objeto remoto");
            //Obtiene una referencia al RMi registry EN LA MAQUINA DEL SERVIDOR
            Registry registry = LocateRegistry.getRegistry(args[0]);
            //Se invoca el objeto remoto y se busca en el rmiregistry
            Ejemplo_I instancia_local = (Ejemplo_I) registry.lookup(nombre_objeto_remoto); 
            System.out.println("Invocando el objeto remoto");
            instancia_local.escribir_mensaje(Integer.parseInt(args[1])); //Se llama a los métodos remotos
        }
        catch(Exception e){
            System.err.println("Ejemplo_I de exeption:");
            e.printStackTrace();
        }
    }

}