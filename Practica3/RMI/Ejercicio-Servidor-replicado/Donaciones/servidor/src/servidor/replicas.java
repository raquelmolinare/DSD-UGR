/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import iservidor.iservidor;
import donacion.donacion;
import idonacion.idonacion;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;
import java.rmi.*;


import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;


/**
 *
 * @author raquelmolire
 */
public class replicas extends UnicastRemoteObject implements iservidor{
    
    private static ArrayList<donacion> replicas;
    
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";

    public replicas() throws RemoteException{
        this.replicas = new ArrayList();
        
        //Se instala el gestor de seguridad
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }
        
    }
    
    public static void add(String nombre, donacion d){
        //System.out.print(GREEN+"\nadd "+RESET);
        replicas.add(d);
    }
    
   
    public String toString(){
        String result = "";
                
        for (donacion replica : replicas) {
            result += GREEN+"\n\t   ---"+replica.getNombre()+": Clientes => "+replica.getClientes()+RESET;
        }
        return result;
    }
    
    @Override
    public HashMap<String, String> registrar(String cliente) throws RemoteException {
        //System.out.print(GREEN+"\tRegistrar "+RESET);
         
        HashMap<String, String> replicaResult;
        replicaResult = new HashMap<>();
        
        //Recorre las replicas buscando cual es la que tiene menos clientes registrados
        int minimo=-1;
        donacion menosclientes;
        if(replicas.size() > 0){
            menosclientes =replicas.get(0);
            minimo = menosclientes.getNumClientes();
            
            for (donacion replica : replicas) {
                if( replica.getNumClientes() < minimo){
                    menosclientes = replica;
                    minimo = replica.getNumClientes();
                }
            }
            
            //En aquella replica que tenga menos clientes registra al cliente 
            //1.para la copia local de replcias
            menosclientes.addCliente(cliente);
            
            //2.para la replica en si
            Registry mireg = LocateRegistry.getRegistry(menosclientes.getHost(), menosclientes.getPuerto());
            try {
                idonacion d = (idonacion)mireg.lookup(menosclientes.getNombre());
                d.registrarDefinitiva(cliente);
            } catch (NotBoundException ex) {
                java.util.logging.Logger.getLogger(replicas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (AccessException ex) {
                java.util.logging.Logger.getLogger(replicas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
             
            //Se devuelve la replica definitiva para el cliente
            System.out.print(GREEN+this.toString()+RESET);
            //Nombre
            replicaResult.put("NOMBRE",menosclientes.getNombre());
            //Puerto
            replicaResult.put("PUERTO", Integer.toString(menosclientes.getPuerto()));
       
        }
        return replicaResult;
    }

    @Override
    public boolean yaRegistrado(String cliente) throws RemoteException{
        boolean existe=false;

        for(int i=0; i < replicas.size() && !existe; i++) {
            if( replicas.get(i).existeCliente(cliente)){
                existe = true;
            }
        }
        
        return existe;
    }

    @Override
    public void addReplica(String host, String nombre, int puerto) throws RemoteException {
        donacion d = new donacion(host,nombre,puerto,false);
        
        replicas.add(d);        
    }

    @Override
    public void registrarDonacion(String nombreReplica, String cliente, double cantidad) throws RemoteException {
        System.out.print(GREEN+"\n\tRegistrar Donacion "+RESET);
        
        //Añade el valor donado por el cliente en la replica
        for(donacion replica : replicas) {
            String n = replica.getNombre();
            if( n.equals(nombreReplica) ){
                replica.registrarDonacion(cliente,cantidad);
            }
        }
        
        System.out.print(GREEN+this.toString()+RESET);
        
    }

    @Override
    public double getcantidadTotal() throws RemoteException {
        //System.out.print(GREEN+"\n\tgetCantidadTotal "+RESET);
        double resultado = 0.0;
        
        //Recorrer las replicas para conocer la cantidad de cada una
        for(donacion replica : replicas) {
            resultado+=replica.getCantidad();
        }
        
        return resultado;
    }

    @Override
    public HashMap<String, String> iniciarSesion(String cliente) throws RemoteException {
        System.out.print(GREEN+"\tIniciar Sesion: "+cliente+RESET);
        
        
        HashMap<String, String> replicaResult;
        replicaResult = new HashMap<>();

        //Recorre las replicas buscando cual es en la que está registrado
        boolean encontrado=false;
        
        for(int i=0; i < replicas.size() && !encontrado; i++) {
            if( replicas.get(i).existeCliente(cliente)){
                encontrado = true;
                //Nombre
                replicaResult.put("NOMBRE",replicas.get(i).getNombre());
                //Puerto
                replicaResult.put("PUERTO", Integer.toString(replicas.get(i).getPuerto()));
            }
        }
        
        if(!encontrado){
            replicaResult = new HashMap<>();
            //Nombre
            replicaResult.put("NOMBRE","ERROR");
            //Puerto
            replicaResult.put("PUERTO", "0");
        }
        
        return replicaResult;
    }

    @Override
    public int getNumDonacionesTotal() throws RemoteException {
        System.out.print(GREEN+"\n\tgetNumDonacionesTotal "+RESET);
        int resultado = 0;

        //Recorrer las replicas para conocer la cantidad de cada una
        for(donacion replica : replicas) {
            resultado+=replica.getNumDonacionesReplica();
        }

        return resultado;
    }

    @Override
    public int getNumClientesTotal() throws RemoteException {
        System.out.print(GREEN+"\n\tgetNumClientesTotal "+RESET);
        int resultado = 0;

        //Recorrer las replicas para conocer la cantidad de cada una
        for(donacion replica : replicas) {
            resultado+=replica.getNumClientesReplica();
        }

        return resultado;
    }

    @Override
    public String getNombreMayorDonacionTotal() throws RemoteException {
        System.out.print(GREEN+"\n\tgetNombreMayorDonacionTotal "+RESET);
        String resultado = "";
        String nombreMayor ="";
        double cantMayor = -1.0;
        
        if( replicas.size() > 0  ){
            //Recorrer las replicas para conocer la mayor donacion y su nombre
            nombreMayor = replicas.get(0).getNombreMayorDonacionReplica();
            cantMayor = replicas.get(0).getCantidadMayorDonacionReplica(); 
            for(int i=1; i < replicas.size(); i++) {
                String n = replicas.get(i).getNombreMayorDonacionReplica();
                double cant = replicas.get(i).getCantidadMayorDonacionReplica();
                if( cantMayor < cant ){
                    cantMayor=cant;
                    nombreMayor=n;
                }
            }
        }
        
        resultado = nombreMayor;

        return resultado;
    }

    @Override
    public double getCantidadMayorDonacionTotal() throws RemoteException {
        System.out.print(GREEN+"\n\tgetCantidadeMayorDonacionTotal "+RESET);
        double resultado = -1.0;
        String nombreMayor ="";
        double cantMayor=-1.0;
        
        if( replicas.size() > 0  ){
            //Recorrer las replicas para conocer la mayor donacion y su nombre
            nombreMayor = replicas.get(0).getNombreMayorDonacionReplica();
            cantMayor = replicas.get(0).getCantidadMayorDonacionReplica(); 
            for(int i=1; i < replicas.size(); i++) {
                String n = replicas.get(i).getNombreMayorDonacionReplica();
                double cant = replicas.get(i).getCantidadMayorDonacionReplica();
                if( cantMayor < cant ){
                    cantMayor=cant;
                    nombreMayor=n;
                }
            }
        }
        
        resultado = cantMayor;

        return resultado;
    }

    @Override
    public boolean darDeBaja(String cliente) throws RemoteException {
        for(donacion replica : replicas) {
            if( replica.existeCliente(cliente) ){
                return replica.eliminarCliente(cliente);
            }
        }
        return false;
    }

     
}

