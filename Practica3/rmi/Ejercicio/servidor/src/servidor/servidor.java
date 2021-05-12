//servidor.java

package servidor;

import donacion.donacion;
import iservidor.iservidor;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Iterator;

public class servidor{
    
    //Variables del servidor
    //private static replicas replicas;
    

    public servidor() {
        //replicas = new HashMap<>();
    }
    
    public static void main(String[] args){
        
        
                
        //1. Crear e instalr un gestor de seguridad si no lo está ya
        if(System.getSecurityManager() == null){ 
            System.setSecurityManager(new SecurityManager());
        }

        try{
            
            //2. Crear y exportar los objetos remotos => una instancia de donacion
            //Crea los objetos remotos  para la comunicacion cliente-servidor
            //Replica 1
            Registry reg1 = LocateRegistry.createRegistry(1099);
            donacion midonacion1 = new donacion();
            Naming.rebind("replica1", midonacion1);
            
            //Replica 2
            Registry reg2 = LocateRegistry.createRegistry(1100);
            donacion midonacion2 = new donacion();
            Naming.rebind("replica2", midonacion2);
            

            HashMap<String, donacion> replicas2  = new HashMap<>();
            
            //Añadimos las replicas en el array
            replicas2.put("replica1", midonacion1);
            replicas2.put("replica2", midonacion2);

            //replicas=replicas2;
            
            replicas replicaA = new replicas();
            replicaA.add("replica1", midonacion1);
            replicaA.add("replica2", midonacion2);

            
            midonacion1.setReplicas(replicaA);
            midonacion2.setReplicas(replicaA);
            
            
            System.out.println("Servidor RemoteException | MalformedURLExceptiondor preparado");
        }
        catch(RemoteException | MalformedURLException e){
            System.out.println("Exception: "+ e.getMessage());
        }
    }

    /*
    public String registrar(String cliente) {
        //Recorre las replicas buscando cual es la que tiene menos clientes registrados
       
        //Iterator<donacion> it = replicas.values().iterator();        
        Iterator<String> it = replicas.keySet().iterator();
        
        String claveReplica = it.next();
        donacion menosclientes = replicas.get(claveReplica);
        while(it.hasNext()){
            String n = it.next();
            donacion d = replicas.get(n);
            if( d.getNumClientes() < menosclientes.getNumClientes() ){
                menosclientes=d;
                claveReplica=n;
            }
        }
        
        menosclientes.addCliente(cliente);
        System.out.println("La clave es " + claveReplica);
        return claveReplica;
        
        //Llama a registrar a aquella que le corresponda, es decir la que menos clientes tenga registrados
        
        //Devuelve el nombre de la replica
    }*/
}