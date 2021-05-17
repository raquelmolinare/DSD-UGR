// Fichero: icontador.java
// Define la Interfaz remota 

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Icontador extends Remote {
    int sumar() throws RemoteException;
    void sumar (int valor) throws RemoteException;
    public int incrementar() throws RemoteException;
}