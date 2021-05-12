/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package donacion;

import iservidor.iservidor;
import donacion.donacion;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raquelmolire
 */
public class replicas implements iservidor{
    
    private static HashMap<String, donacion> replicas;

    public replicas() {
        replicas = new HashMap<>();
    }
    
    public void add(String nombre, donacion d){
        replicas.put(nombre, d);
    }

    @Override
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
    }
    
    
}
