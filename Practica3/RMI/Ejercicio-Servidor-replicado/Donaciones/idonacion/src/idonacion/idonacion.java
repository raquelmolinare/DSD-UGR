/**
 * Desarrollo de sistemas distribuidos 2020-2021
 * Páctica 3: RMI
 * Autor: Raquel Molina Reche
 * 
 * Esta es la interfaz entre el Cliente y el Servidor
 */

package idonacion;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;


public interface idonacion extends Remote{
    public HashMap<String, String> registrar(String cliente) throws RemoteException;
    public HashMap<String, String> iniciarSesion(String cliente) throws RemoteException;
    public boolean donar(String cliente, double cantidad) throws RemoteException; 
    public double getCantidadCliente(String cliente) throws RemoteException;
    public double getCantidadReplica() throws RemoteException;
    public double getCantidadSistema() throws RemoteException;  
    public int getNumDonacionesReplica() throws RemoteException;
    public int getNumDonacionesSistema() throws RemoteException;
    public int getNumClientesReplica() throws RemoteException;
    public int getNumClientesSistema() throws RemoteException;
    public String getNombreMayorDonacionReplica() throws RemoteException;
    public String getNombreMayorDonacionSistema() throws RemoteException;
    public double getCantidadMayorDonacionReplica() throws RemoteException;
    public double getCantidadMayorDonacionSistema() throws RemoteException;
    public boolean darDeBaja(String cliente) throws RemoteException;
    public void registrarDefinitiva(String cliente) throws RemoteException;
}
