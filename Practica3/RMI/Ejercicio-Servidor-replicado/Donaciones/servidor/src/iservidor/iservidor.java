/**
 * Desarrollo de sistemas distribuidos 2020-2021
 * Páctica 3: RMI
 * Autor: Raquel Molina Reche
 * 
 * Esta es la interfaz entre las diferentes réplicas del servidor, maneja una comunicación Servidor-Servidor
 */

package iservidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;


public interface iservidor extends Remote{
    public void addReplica(String host, String nombreReplica, int puerto) throws RemoteException;
    public HashMap<String, String> registrar(String cliente) throws RemoteException;
    public boolean yaRegistrado(String cliente) throws RemoteException;
    public void registrarDonacion(String nombreReplica, String cliente, double cantidad) throws RemoteException;
    public HashMap<String, String> iniciarSesion(String cliente) throws RemoteException;
    public double getcantidadTotal() throws RemoteException;
    public int getNumDonacionesTotal() throws RemoteException;
    public int getNumClientesTotal() throws RemoteException;
    public String getNombreMayorDonacionTotal() throws RemoteException;
    public double getCantidadMayorDonacionTotal() throws RemoteException;
    public boolean darDeBaja(String cliente) throws RemoteException;
}

