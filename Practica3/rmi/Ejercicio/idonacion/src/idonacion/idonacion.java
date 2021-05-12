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


public interface idonacion extends Remote{
    public String registrar(String cliente) throws RemoteException;
    public boolean donar(String cliente, int cantidad) throws RemoteException;
    public double getCantidadCliente(String cliente) throws RemoteException;
    public double getCantidadTotal() throws RemoteException;    
}
