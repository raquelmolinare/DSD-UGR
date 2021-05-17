// Fichero: servidor.java
// Código del servidor 

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class Servidor{
    public static void main(String[] args){
        //1. Crear e instalr un gestor de seguridad si no lo está ya
        if(System.getSecurityManager() == null){ 
            System.setSecurityManager(new SecurityManager());
        }

        try{
            //2. Crear y exportar los objetos remotos => una instancia de contador
            Registry reg = LocateRegistry.createRegistry(1099);
            Contador micontador = new Contador();
            Naming.rebind("mmicontador", micontador);

            //suma = 0

            System.out.println("Servidor RemoteException | MalformedURLExceptiondor preparado");
        }
        catch(Exception e){
            System.err.println("Ejemplo de exeption:");
            e.printStackTrace();
        }
    }
}