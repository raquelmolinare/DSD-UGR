// Fichero: contador.java
// Implementa la Interfaz remota

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;

public class Contador extends UnicastRemoteObject implements Icontador{
    private int suma;

    public Contador() throws RemoteException{

    }

    public int sumar() throws RemoteException{
        return suma;
    }

    public void sumar(int valor) throws RemoteException{
        suma = valor;
    }

    public int incrementar() throws RemoteException {
        suma++;
        return suma;
    }
}