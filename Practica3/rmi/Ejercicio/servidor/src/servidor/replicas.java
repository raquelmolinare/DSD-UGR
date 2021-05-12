/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import iservidor.iservidor;
import donacion.donacion;
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
    
    public String toString(){
        String result = "";
        
        Iterator<String> it = replicas.keySet().iterator();
        
        String claveReplica = it.next();
        
        donacion donacion = replicas.get(claveReplica);
        try {
            result += "\n\t- Hola soy la replica "+claveReplica+" y tengo "+donacion.getNumClientes()+" clientes y "+donacion.getCantidadTotal()+" dinero donado";
            result += "\n\t\t Clientes => "+donacion.getClientes();
        } catch (RemoteException ex) {
            Logger.getLogger(replicas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(it.hasNext()){
            String n = it.next();
            donacion d = replicas.get(n);
            try {
                result += "\n\t- Hola soy la replica "+n+" y tengo "+d.getNumClientes()+" clientes y "+d.getCantidadTotal()+" dinero donado";
                result += "\n\t\t Clientes => "+d.getClientes();

            } catch (RemoteException ex) {
                Logger.getLogger(replicas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        return result;
    }
    
    @Override
    public String registrar(String cliente) {
       
        //Recorre las replicas buscando cual es la que tiene menos clientes registrados
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
        
        //En aquella replica que tenga menos clientes registra al cliente
        menosclientes.addCliente(cliente);
        
        //Devuelve el nombre de la replica
        System.out.println("La clave es " + claveReplica);
        return claveReplica;       
    }

    @Override
    public boolean yaRegistrado(String cliente) {      
        boolean existe=false;
        
        
        //Se recorren para cada replica los clientes
        Iterator<String> it = replicas.keySet().iterator();

        while(it.hasNext() && !existe){
            
            String n = it.next();
            donacion d = replicas.get(n);
            HashMap<String, Double> c = d.getClientes();

            //Si para alguna replica hay un cliente con ese nombre se devuelve true
            for (String nombre:c.keySet()) {
                System.out.println("Clave REPLICA: " + n + "----- NOMBRE CLIENTE: " + nombre);
                if (nombre.equals(cliente)){
                    existe=true;
                } 
            }
        }
        System.out.println(existe);
        return existe;
    }

    @Override
    public boolean igualNumClientes() {
        
        boolean iguales=true;
        
        //Recorre las replicas para saber si el numero de clientes que tienen son iguales
        Iterator<String> it = replicas.keySet().iterator();
        
        String n1 = it.next();
        donacion d1 = replicas.get(n1);
        while(it.hasNext() && iguales){
            String n2 = it.next();
            donacion d2 = replicas.get(n2);
            if( d1.getNumClientes() != d2.getNumClientes() ){
                iguales = false;
            }
        }
        
        return iguales;
        
    }
    
    
}

