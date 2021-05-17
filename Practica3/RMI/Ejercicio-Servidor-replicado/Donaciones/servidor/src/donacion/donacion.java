/**
 * Desarrollo de sistemas distribuidos 2020-2021
 * Páctica 3: RMI
 * Autor: Raquel Molina Reche
 * 
 * Esta clase implementa los metodos de la interfaz entre el Cliente y el Servidor
 */

package donacion;


import idonacion.idonacion;
import iservidor.iservidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;
import java.rmi.*;

import java.util.HashMap;
import java.util.Iterator;
import servidor.replicas;


public class donacion extends UnicastRemoteObject implements idonacion{

    private HashMap<String, Double> clientes; 
    private double cantidad;
    private int numDonaciones;
    private iservidor iservidor;
    
    private String host;
    private String nombre;
    private int puerto;
    
    //colores
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    public donacion( String host, String nombre,int puerto, boolean inicial) throws RemoteException{
        this.clientes = new HashMap<>();
        this.cantidad = 0;
        this.numDonaciones = 0;
        this.host = host;
        this.nombre = nombre;
        this.puerto = puerto;
        
        //Se instala el gestor de seguridad
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }

        try{
            //Crea el stub para el cliente especificando el nombre del servidor
            //System.out.println(BLUE+"Replica buscando al servidor de comunicacion de las replicas..."+RESET);
            Registry mireg = LocateRegistry.getRegistry("127.0.0.1", 1099);
            this.iservidor = (iservidor)mireg.lookup("comunicacionSS");
            
        }
        catch(NotBoundException | RemoteException e){
            System.err.println("Exception del sistema: "+ e);
        }
        
    }
   
    @Override
    public HashMap<String, String> registrar(String cliente) throws RemoteException{
        HashMap<String, String> replicaResult;
        replicaResult = new HashMap<>();
        
        //Se comunica con el resto de hebras para conocer si ya existe el cliente en alguna
        if( !iservidor.yaRegistrado(cliente) ){
            //Si todavia no esta registrado en ninguna
           
            //Se comunica con el resto de replicas para saber cual tiene menos clientes registrados
            // y se registre al cliente en la que corresponda
            replicaResult = iservidor.registrar(cliente); 
        }
        else{
            //Nombre
            replicaResult.put("NOMBRE","ERROR");
            //Puerto
            replicaResult.put("PUERTO", "0");
        }
        
        return replicaResult;
    }
    
    @Override
    public void registrarDefinitiva(String cliente) throws RemoteException {
        clientes.put(cliente,0.0);
        System.out.println(BLUE+"\nRegistrar: "+clientes.toString()+RESET);
    }
    
    public boolean donar(String cliente, double cantidad) throws RemoteException{
        System.out.println(BLUE+"\nDonar: "+RESET);
        
        //Solo cuenta la donacion si la cantidad a donar es mayor de 0
        if(cantidad > 0.0){
            
            if(clientes.containsKey(cliente)){          
            
                this.numDonaciones++;

                this.cantidad += cantidad;

                double donado = this.clientes.get(cliente)+cantidad;
                this.clientes.put(cliente, donado);

                iservidor.registrarDonacion(this.nombre, cliente, cantidad);

                System.out.println(BLUE+"\n   "+clientes.toString()+RESET);

                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    @Override
    public HashMap<String, String> iniciarSesion(String cliente) throws RemoteException {
        System.out.println(BLUE+"\nIniciar Sesion: "+cliente+RESET);
        HashMap<String, String> replicaResult;
        replicaResult = new HashMap<>();
        
        //Si el cliente pertenece a esta replica, no hace falta qeu se comuque con las demas
        //devuelve su información al cliente
        if( this.clientes.containsKey(cliente) ){
            //Nombre
            replicaResult.put("NOMBRE",this.nombre);
            //Puerto
            replicaResult.put("PUERTO", Integer.toString(this.puerto));
        }
        else{
            //Se comunica con el resto de hebras para conocer si existe en alguna 
            //y devolver la que corresponda
            replicaResult = iservidor.iniciarSesion(cliente);
        }
        
        return replicaResult;
    }
     public double getCantidadCliente(String cliente) throws RemoteException{
        System.out.println(BLUE+"\ngetCantidadCliente: "+RESET);
        if( this.clientes.containsKey(cliente) ){
            return this.clientes.get(cliente);
        }
        else{
            return -1.0;
        }
    }
    
    @Override
    public double getCantidadReplica() throws RemoteException {
        System.out.println(BLUE+"\ngetCantidadReplica: "+RESET);
        return this.cantidad;
    }
    
    public double getCantidadTotal() throws RemoteException{
         System.out.println(BLUE+"\ngetCantidadTotal: "+RESET);
        //Se comunica con el resto de replicas para conocer el valor total
        return iservidor.getcantidadTotal();
        
    }

    @Override
    public double getCantidadSistema() throws RemoteException {
        System.out.println(BLUE+"\ngetCantidadSistema: "+RESET);
        return this.iservidor.getcantidadTotal();
    }

    @Override
    public int getNumDonacionesReplica() throws RemoteException {
        System.out.println(BLUE+"\ngetNumDonacionesReplica: "+RESET);
        return this.numDonaciones;
    }

    @Override
    public int getNumDonacionesSistema() throws RemoteException {
        System.out.println(BLUE+"\ngetNumDonacionesSistema: "+RESET);
        return this.iservidor.getNumDonacionesTotal();        
    }

    @Override
    public int getNumClientesReplica() throws RemoteException {
        System.out.println(BLUE+"\ngetNumClientesReplica: "+RESET);
        return this.clientes.size();
    }

    @Override
    public int getNumClientesSistema() throws RemoteException {
        System.out.println(BLUE+"\ngetNumClientesSistema: "+RESET);
        return this.iservidor.getNumClientesTotal();
    }

    @Override
    public String getNombreMayorDonacionReplica() throws RemoteException {
        System.out.println(BLUE+"\ngetNombreMayorDonacionReplica: "+RESET);
        
        String nombreMayor = "";
        
        //Recorre los clientes buscando la mayor donacion
        if( clientes.size() > 0  ){
            Iterator<String> it = clientes.keySet().iterator();

            nombreMayor = it.next();
            double cantMayor = clientes.get(nombreMayor); 
            while(it.hasNext()){
                String n = it.next();
                double c = clientes.get(n);

                if( cantMayor < c ){
                    cantMayor=c;
                    nombreMayor=n;
                }
            }
        }
        
        return nombreMayor;
    }

    @Override
    public String getNombreMayorDonacionSistema() throws RemoteException {
        System.out.println(BLUE+"\ngetNombreMayorDonacionSistema: "+RESET);
        return this.iservidor.getNombreMayorDonacionTotal();
    }

    @Override
    public double getCantidadMayorDonacionReplica() throws RemoteException {
        System.out.println(BLUE+"\ngetCantidadMayorDonacionReplica: "+RESET);
        //Recorre los clientes buscando la mayor donacion
        double cantMayor=-1.0;
        if( clientes.size() > 0  ){
            Iterator<String> it = clientes.keySet().iterator();

            String nombreMayor = it.next();
            cantMayor = clientes.get(nombreMayor); 
            while(it.hasNext()){
                String n = it.next();
                double c = clientes.get(n);

                if( cantMayor < c ){
                    cantMayor=c;
                    nombreMayor=n;
                }
            }
        }
        
        return cantMayor;
    }

    @Override
    public double getCantidadMayorDonacionSistema() throws RemoteException {
        System.out.println(BLUE+"\ngetCantidadMayorDonacionSistema: "+RESET);
        return this.iservidor.getCantidadMayorDonacionTotal();
    }

    @Override
    public boolean darDeBaja(String cliente) throws RemoteException {
        System.out.println(BLUE+"\nDar de Baja: "+RESET);
        //Se borra en la replica
        this.clientes.remove(cliente);
        
        //Se avisa al servidor
        return this.iservidor.darDeBaja(cliente);
    }
    
    
    //----METODOS PROPIOS---
    
    public String getHost() {
        return this.host;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    public int getPuerto() {
        return this.puerto;
    }
    
    public double getCantidad() {
        return this.cantidad;
    }

    public int getNumClientes() {
        return this.clientes.size();
    }
    
    public boolean existeCliente(String cliente) {
        return this.clientes.containsKey(cliente);
    }
    
    public HashMap<String, Double>  getClientes() {
        return this.clientes;
    }
    
    public void addCliente(String cliente){
        clientes.put(cliente, 0.0);
    }
    
    public boolean eliminarCliente(String cliente){
        clientes.remove(cliente);
        return true;
    }
    
    public void registrarDonacion(String cliente, double cantidad){
        //Solo cuenta la donacion si la cantidad a donar es mayor de 0
        if(cantidad > 0.0){
            this.numDonaciones++;

            this.cantidad += cantidad;

            double donado = this.clientes.get(cliente)+cantidad;
            this.clientes.put(cliente, donado);
        }

    }

}