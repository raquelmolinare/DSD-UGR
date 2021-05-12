/**
 * Desarrollo de sistemas distribuidos 2020-2021
 * Páctica 3: RMI
 * Autor: Raquel Molina Reche
 * 
 * Esta clase implementa los metodos de la interfaz entre el Cliente y el Servidor
 */

package donacion;

import servidor.replicas;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;

import idonacion.idonacion;
import iservidor.iservidor;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Iterator;


public class donacion extends UnicastRemoteObject implements idonacion{

    //private ArrayList<String> clientes;
    private HashMap<String, Double> clientes; 
    private int cantidad;
    private int numDonaciones;
    private iservidor iservidor;
        
        

    public donacion() throws RemoteException{
        clientes = new HashMap<>();
        cantidad = 0;
        numDonaciones = 0;
        iservidor = new replicas();
    }
    
    @Override
    public String registrar(String cliente) throws RemoteException{
        
        //Se comunica con el resto de hebras para conocer si ya existe el cliente en alguna
        if( !iservidor.yaRegistrado(cliente) ){
            //Si todavia no esta registrado en ninguna
            
            //Se comunica con el resto de replicas para saber cual tiene menos clientes registrados
            String replicaDefinitiva = iservidor.registrar(cliente);
            
            //En esa comunicacion ya se registra al cliente en la hebra que corresponde
            //Se devuelve el nombre de la replica definitiva al cliente
            return replicaDefinitiva;
        }
        else{
            return "ERROR";
        }
         
    }
    
    public boolean donar(String cliente, int cantidad) throws RemoteException{
        
        if(clientes.containsKey(cliente)){          
            this.numDonaciones++;
            
            this.cantidad += cantidad;
            
            double donado = this.clientes.get(cliente)+cantidad;
            this.clientes.put(cliente, donado);
            
            System.out.println(iservidor.toString());
            
            return true;
        }
        else{
            return false;
        }
    }
   
    public double getCantidadCliente(String cliente) throws RemoteException{
        if( this.clientes.containsKey(cliente) ){
            return this.clientes.get(cliente);
        }
        else{
            return -1.0;
        }
    }
    
    public double getCantidadTotal() throws RemoteException{
        return this.cantidad;
    }

    public int getNumClientes() {
        return this.clientes.size();
    }
    
    public HashMap<String, Double>  getClientes() {
        return this.clientes;
    }
    
    public void addCliente(String nombre){
        clientes.put(nombre, 0.0);
    }
    
     public void setReplicas(replicas r){
        iservidor = r;
    }

    /*
    @Override
    public String registrar(String cliente){
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
        
        System.out.println("La clave es " + claveReplica);
        return claveReplica;
        
        //Llama a registrar a aquella que le corresponda, es decir la que menos clientes tenga registrados
        
        //Devuelve el nombre de la replica
        
    }*/

    
}