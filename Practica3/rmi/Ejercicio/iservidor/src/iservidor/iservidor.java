/**
 * Desarrollo de sistemas distribuidos 2020-2021
 * Páctica 3: RMI
 * Autor: Raquel Molina Reche
 * 
 * Esta es la interfaz entre las diferentes réplicas del servidor, maneja una comunicación Servidor-Servidor
 */

package iservidor;

public interface iservidor{
    String registrar(String cliente);
    //int getNumClientes() throws RemoteException;   
}
