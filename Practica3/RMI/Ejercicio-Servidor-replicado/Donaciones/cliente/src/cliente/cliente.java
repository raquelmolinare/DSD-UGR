// cliente.java
package cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.*;
import java.rmi.registry.Registry;
import java.util.Scanner;

import idonacion.idonacion;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class cliente {
    
    //Variables información cliente
    private static String cliente;
    private static idonacion  midonacionPrimera;
    private static idonacion midonacionDefinitiva;
    
    //COLORES
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String PURPLE = "\u001B[35m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    
    public static void main( String[] args) {
        
        String host = "127.0.0.1";
        Scanner teclado = new Scanner (System.in);
        teclado.useLocale(Locale.ENGLISH);
           
        //Se instala el gestor de seguridad
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }

        try{
            //Crea el stub para el cliente especificando el nombre del servidor
            System.out.println("Buscando al servidor...");
            Registry mireg = LocateRegistry.getRegistry(host, 1100);
            midonacionPrimera = (idonacion)mireg.lookup("replica1");
            
            //-------------------MENU---------------
            
            HashMap<String, String> replicaDefinitiva;
            String nombreReplica = "";
            int puertoReplica;
            
            Registry miregDefinitivo;
            boolean salir;
            int opcion;
            double cant;
            boolean result;
            String nombreConsultado="";
            int num;
            boolean volver;
            
            int subMenu=0;
            int menuPrincipal=0;
            
            while( menuPrincipal != 3){
              System.out.println("\nOpciones disponibles:");
              System.out.println("  1: Iniciar Sesion");
              System.out.println("  2: Registrarse");
              System.out.println("  3: Salir");

              System.out.print("  --Introducce una opción: ");

              menuPrincipal = teclado.nextInt();
              teclado.nextLine();

                switch(menuPrincipal){
                    case 1: //Inicio Sesion
                        System.out.println(YELLOW_BACKGROUND+"\n\t----INICIO SESIÓN----"+RESET);

                        System.out.print("\t    Introduce el nombre: ");
                        cliente = teclado.next();
                        
                        //Intento inicio sesion
                        boolean logeado=false;
                        
                        replicaDefinitiva = midonacionPrimera.iniciarSesion(cliente);

                        nombreReplica = replicaDefinitiva.get("NOMBRE");
                        puertoReplica = Integer.valueOf (replicaDefinitiva.get("PUERTO"));
                        
                        if( !nombreReplica.equals("ERROR") ){
                            miregDefinitivo = LocateRegistry.getRegistry(host, puertoReplica);
                            midonacionDefinitiva = (idonacion)miregDefinitivo.lookup(nombreReplica);
                            logeado = true;
                            System.out.println(GREEN+"\t    Inicio de sesion correcto..."+RESET);
                        }
                        
                        salir = false;
                        while( !logeado&& !salir){
                            System.out.println(RED+"\t    Error!! El nombre introducido no es correcto"+RESET);
                            System.out.println("\t    Opciones disponibles:");
                            System.out.println("\t       1: Reintentar");
                            System.out.println("\t       2: Atrás");

                            System.out.print("\t       --Introducce una opción: ");

                            opcion = teclado.nextInt();
                            teclado.nextLine();
                            
                            switch(opcion) {
                                case 1:
                                    System.out.print("\t    Introduce el nombre: ");
                                    cliente = teclado.next();
                        
                                    replicaDefinitiva = midonacionPrimera.iniciarSesion(cliente);
                                    replicaDefinitiva = midonacionPrimera.iniciarSesion(cliente);

                                    nombreReplica = replicaDefinitiva.get("NOMBRE");
                                    puertoReplica = Integer.valueOf (replicaDefinitiva.get("PUERTO"));

                                    if( !nombreReplica.equals("ERROR") ){
                                        miregDefinitivo = LocateRegistry.getRegistry(host, puertoReplica);
                                        midonacionDefinitiva = (idonacion)miregDefinitivo.lookup(nombreReplica);
                                        logeado = true;
                                        System.out.println(GREEN+"\t    Inicio de sesion correcto..."+RESET);
                                    }
                                    break;
                                case 2: 
                                    salir=true;
                                    break;
                                default:
                                  System.out.println("ERROR: Opcion no valida\n");
                            }
                            
                        }
                        
                        if(logeado){
                            

                            //Operaciones disponibles una vez iniciado sesión y habiendo realizado alguna donacion
                            subMenu = 0;
                            volver=false;
                            while ( !volver ) {

                                //Si el cliente ya ha realizado alguna donacion puede acceder a todas las operaciones
                                if( midonacionDefinitiva.getCantidadCliente(cliente) > 0.0  ){
                                    System.out.println("\n\tOperaciones disponibles:");
                                    System.out.println("\t    1:  Donar");
                                    System.out.println("\t    2:  Consultar el dinero total donado por mi");
                                    System.out.println("\t    3:  Consultar el dinero total donado en el sistema");
                                    System.out.println("\t    4:  Consultar el dinero total donado en mi servidor concreto");
                                    System.out.println("\t    5:  Consultar el numero total de donaciones en el sistema");
                                    System.out.println("\t    6:  Consultar el numero total de donaciones en mi servidor concreto");
                                    System.out.println("\t    7:  Consultar el numero de clientes totales del sistema");
                                    System.out.println("\t    8:  Consultar el numero de clientes de mi servidor concreto");
                                    System.out.println("\t    9:  Consultar el nombre del cliente que haya donado más dinero en todo el sistema");
                                    System.out.println("\t    10: Consultar el nombre del cliente que haya donado más dinero en mi servidor concreto");
                                    System.out.println("\t    11: Consultar la mayor cantidad donada en todo el sistema");
                                    System.out.println("\t    12: Consultar la mayor cantidad donada en mi servidor concreto");
                                    System.out.println("\t    13: Darse de baja");
                                    System.out.println("\t    14: Cerrar Sesion / Salir");

                                    System.out.print("\t    --Introducce una opción: ");
                                    subMenu = teclado.nextInt();
                                    teclado.nextLine();


                                    switch (subMenu) {
                                      case 1: //1.Donar
                                          System.out.println(PURPLE+"\n\t    ------DONAR------: "+RESET);
                                          System.out.print("\t       Introduce la cantidad (en decimal con punto): ");
                                          cant = teclado.nextDouble();
                                          result = midonacionDefinitiva.donar(cliente,cant);
                                          if(result){
                                              System.out.println(GREEN+"\t       La donacion se ha realizado de forma correcta. GRACIAS POR SU APORTACIÓN! :) "+RESET);
                                          }
                                          else{
                                              System.out.println(RED+"\t       La donacion NO se ha realizado de forma correcta... :( "+RESET);
                                          }
                                          break;
                                      case 2: //2.Consultar el dinero total donado por mi{
                                          System.out.println(PURPLE+"\n\t    ------CONSULTAR EL DINERO TOTAL DONADO POR MI------: "+RESET);
                                          cant = midonacionDefinitiva.getCantidadCliente(cliente);
                                          System.out.println("\t       El dinero total que has donado ha sido "+cant);
                                          break;
                                      case 3: //3.Consultar el dinero total donado en el sistema
                                          System.out.println(PURPLE+"\n\t    ------CONSULTAR EL DINERO TOTAL DONADO EN EL SISTEMA------: "+RESET);
                                          cant = midonacionDefinitiva.getCantidadSistema();
                                          System.out.println("\t       El dinero total donado en el sistema es "+cant);
                                          break;
                                      case 4: //4.Consultar el dinero total donado en mi servidor concreto
                                          System.out.println(PURPLE+"\n\t    ------CONSULTAR EL DINERO TOTAL DONADO EN MI SERVIDOR CONCRETO------: "+RESET);
                                          cant = midonacionDefinitiva.getCantidadReplica();
                                          System.out.println("\t       El dinero total donado en tu replica es "+cant);
                                          break;
                                      case 5: //5.Consultar el numero total de donaciones en el sistema
                                          System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NUMERO TOTAL DE DONACIONES EN EL SISTEMA------: "+RESET);
                                          num = midonacionDefinitiva.getNumDonacionesSistema();
                                          System.out.println("\t       El numero total de donaciones en el sistema es "+num);
                                          break;
                                      case 6: //6.Consultar el numero total de donaciones en mi servidor concreto
                                          System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NUMERO TOTAL DE DONACIONES EN MI SERVIDOR CONCRETO------: "+RESET);
                                          num = midonacionDefinitiva.getNumDonacionesReplica();
                                          System.out.println("\t       El numero total de donaciones en tu servidor concreto es "+num);
                                          break;
                                      case 7: //7.Consultar el numero de clientes totales del sistema
                                          System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NUMERO DE CLIENTES TOTALES DEL SISTEMA------: "+RESET);
                                          num = midonacionDefinitiva.getNumClientesSistema();
                                          System.out.println("\t       El numero de clientes totales del sistema es "+num);
                                          break;
                                      case 8: //8.Consultar el numero de clientes de mi servidor concreto
                                          System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NUMERO DE CLIENTES DE MI SERVIDOR CONCRETO------: "+RESET);
                                          num = midonacionDefinitiva.getNumClientesReplica();
                                          System.out.println("\t       El numero de clientes de tu servidor concreto es "+num);
                                          break;
                                      case 9: //9.Consultar el nombre del cliente que haya donado más dinero en todo el sistema
                                          System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NOMBRE DEL CLIENTE QUE HAYA DONADO MÁS DINERO EN TODO EL SISTEMA------: "+RESET);
                                          if( midonacionDefinitiva.getNumDonacionesSistema()> 0){
                                              nombreConsultado = midonacionDefinitiva.getNombreMayorDonacionSistema();
                                              System.out.println("\t       El nombre del cliente que haya donado más dinero en todo el sistema es "+nombreConsultado);
                                          }
                                          else{
                                              System.out.println("\t       Aún no hay donaciones en el sistema!");

                                          }
                                          break;
                                      case 10: //10.Consultar el nombre del cliente que haya donado más dinero en mi servidor concreto
                                          System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NOMBRE DEL CLIENTE QUE HAYA DONADO MÁS DINERO EN MI SERVIDOR CONCRETO------: "+RESET);
                                          if( midonacionDefinitiva.getNumDonacionesReplica()> 0){
                                              nombreConsultado = midonacionDefinitiva.getNombreMayorDonacionReplica();
                                              System.out.println("\t       El nombre del cliente que haya donado más dinero esta replica es "+nombreConsultado);
                                          }
                                          else{
                                              System.out.println("\t       Aún no hay donaciones en esta replica!");

                                          }
                                          break;
                                      case 11: //11.Consultar la mayor cantidad donada en todo el sistema
                                          System.out.println(PURPLE+"\n\t    ------CONSULTAR LA MAYOR CANTIDAD DONADA EN TODO EL SISTEMA------: "+RESET);
                                          if( midonacionDefinitiva.getNumDonacionesSistema()> 0){
                                              cant = midonacionDefinitiva.getCantidadMayorDonacionSistema();
                                              System.out.println("\t       La mayor cantidad donada en todo el sistema es "+cant);
                                          }
                                          else{
                                              System.out.println("\t       Aún no hay donaciones en el sistema!");
                                          }
                                          break;
                                      case 12: //12.Consultar la mayor cantidad donada en mi servidor concreto
                                          System.out.println(PURPLE+"\n\t    ------CONSULTAR LA MAYOR CANTIDAD DONADA EN MI SERVIDOR CONCRETO------: "+RESET);
                                          if( midonacionDefinitiva.getNumDonacionesReplica()> 0){
                                              cant = midonacionDefinitiva.getCantidadMayorDonacionReplica();
                                              System.out.println("\t       La mayor cantidad donada en esta replica es "+cant);
                                          }
                                          else{
                                              System.out.println("\t       Aún no hay donaciones en esta replica!");
                                          }
                                          break;
                                      case 13: //13.Darse de baja
                                          System.out.println(PURPLE+"\n\t    ------DARSE DE BAJA------: ");
                                          result = midonacionDefinitiva.darDeBaja(cliente);
                                          if(result){
                                              System.out.println(GREEN+"\t       La operacion se ha realizado de forma correcta. :) "+RESET);
                                              volver = true;
                                          }
                                          else{
                                              System.out.println(RED+"\t       La operacion NO se ha realizado de forma correcta... :( "+RESET);
                                          }
                                          break;
                                      case 14://Salir o Cerrar Sesion
                                          System.out.println(GREEN+"\t       La sesión se ha cerrado de forma correcta. HASTA PRONTO! :) "+RESET);
                                          volver =true;
                                          break;
                                      default:
                                        System.out.println("ERROR: Opcion no valida\n");
                                    }                                    
                                }
                                else{
                                    
                                    int subsubMenu = 0;
                                               
                                    System.out.println("\n\tOperaciones disponibles:");
                                    System.out.println("\t    1:  Donar");
                                    System.out.println("\t    2: Darse de baja");
                                    System.out.println("\t    3: Cerrar Sesion / Salir");

                                    System.out.print("\t    --Introducce una opción: ");
                                    subsubMenu = teclado.nextInt();
                                    teclado.nextLine();


                                    switch (subsubMenu) {
                                      case 1: //1.Donar
                                          System.out.println(PURPLE+"\n\t    ------DONAR------: "+RESET);
                                          System.out.print("\t       Introduce la cantidad (en decimal con punto): ");
                                          cant = teclado.nextDouble();
                                          result = midonacionDefinitiva.donar(cliente,cant);
                                          if(result){
                                              System.out.println(GREEN+"\t       La donacion se ha realizado de forma correcta. GRACIAS POR SU APORTACIÓN! :) "+RESET);
                                          }
                                          else{
                                              System.out.println(RED+"\t       La donacion NO se ha realizado de forma correcta... :( "+RESET);
                                          }
                                          break;
                                     case 2: //2.Darse de baja
                                        System.out.println(PURPLE+"\n\t    ------DARSE DE BAJA------: ");
                                        result = midonacionDefinitiva.darDeBaja(cliente);
                                        if(result){
                                            System.out.println(GREEN+"\t       La operacion se ha realizado de forma correcta. :) "+RESET);
                                            volver = true;
                                        }
                                        else{
                                            System.out.println(RED+"\t       La operacion NO se ha realizado de forma correcta... :( "+RESET);
                                        }
                                        break;
                                    case 3://Salir o Cerrar Sesion
                                        System.out.println(GREEN+"\t       La sesión se ha cerrado de forma correcta. HASTA PRONTO! :) "+RESET);
                                        volver =true;
                                        break;
                                    default:
                                      System.out.println("ERROR: Opcion no valida\n");
                                  }
                                    
                                }
                            }
                        }
                                
                                                
                        break;

                    case 2: //Registrarse
                        System.out.println(YELLOW_BACKGROUND+"\n\t----FORMULARIO DE REGISTRO----"+RESET);

                        System.out.print("\t    Introduce el nombre: ");
                        cliente = teclado.next();
                        
                        
                        //Intento registro
                        boolean registrado=false;
                        
                        replicaDefinitiva = midonacionPrimera.registrar(cliente);
                        
                        nombreReplica = replicaDefinitiva.get("NOMBRE");
                        puertoReplica = Integer.valueOf (replicaDefinitiva.get("PUERTO"));
                        
                        if( !nombreReplica.equals("ERROR") ){
                            miregDefinitivo = LocateRegistry.getRegistry(host, puertoReplica);
                            midonacionDefinitiva = (idonacion)miregDefinitivo.lookup(nombreReplica);
                            registrado = true;
                            System.out.println(GREEN+"\t    Registro correcto..."+RESET);
                        }
                        
                        salir=false;
                        while( !registrado && !salir){
                            System.out.println(RED+"\t    Error!! El nombre introducido no es correcto"+RESET);
                            System.out.println("\t   Opciones disponibles:");
                            System.out.println("\t       1: Reintentar");
                            System.out.println("\t       2: Atrás");

                            System.out.print("\t       --Introducce una opción: ");

                            opcion = teclado.nextInt();
                            teclado.nextLine();
                            
                            switch(opcion) {
                                case 1:
                                    System.out.print("\t    Introduce el nombre: ");
                                    cliente = teclado.next();
                                    
                                    replicaDefinitiva = midonacionPrimera.registrar(cliente);
                                    nombreReplica = replicaDefinitiva.get("NOMBRE");
                                    puertoReplica = Integer.valueOf (replicaDefinitiva.get("PUERTO"));

                                    if( !nombreReplica.equals("ERROR") ){
                                        miregDefinitivo = LocateRegistry.getRegistry(host, puertoReplica);
                                        midonacionDefinitiva = (idonacion)miregDefinitivo.lookup(nombreReplica);
                                        registrado = true;
                                        System.out.println(GREEN+"\t    Registro correcto..."+RESET);
                                    }
                                    break;
                                case 2: 
                                    salir=true;
                                    break;
                                default:
                                  System.out.println("ERROR: Opcion no valida\n");
                            }
                            
                        }
                        
                        if(registrado){
                            
                            //Operaciones disponibles una vez iniciado sesión
                            subMenu = 0;
                            volver=false;
                            while ( !volver ) {

                                //Si el cliente ya ha realizado alguna donacion puede acceder a todas las operaciones
                                if( midonacionDefinitiva.getCantidadCliente(cliente) > 0.0  ){
                                    System.out.println("\n\tOperaciones disponibles:");
                                    System.out.println("\t    1:  Donar");
                                    System.out.println("\t    2:  Consultar el dinero total donado por mi");
                                    System.out.println("\t    3:  Consultar el dinero total donado en el sistema");
                                    System.out.println("\t    4:  Consultar el dinero total donado en mi servidor concreto");
                                    System.out.println("\t    5:  Consultar el numero total de donaciones en el sistema");
                                    System.out.println("\t    6:  Consultar el numero total de donaciones en mi servidor concreto");
                                    System.out.println("\t    7:  Consultar el numero de clientes totales del sistema");
                                    System.out.println("\t    8:  Consultar el numero de clientes de mi servidor concreto");
                                    System.out.println("\t    9:  Consultar el nombre del cliente que haya donado más dinero en todo el sistema");
                                    System.out.println("\t    10: Consultar el nombre del cliente que haya donado más dinero en mi servidor concreto");
                                    System.out.println("\t    11: Consultar la mayor cantidad donada en todo el sistema");
                                    System.out.println("\t    12: Consultar la mayor cantidad donada en mi servidor concreto");
                                    System.out.println("\t    13: Darse de baja");
                                    System.out.println("\t    14: Cerrar Sesion / Salir");

                                    System.out.print("\t    --Introducce una opción: ");
                                    subMenu = teclado.nextInt();
                                    teclado.nextLine();


                                    switch (subMenu) {
                                        case 1: //1.Donar
                                            System.out.println(PURPLE+"\n\t    ------DONAR------: "+RESET);
                                            System.out.print("\t       Introduce la cantidad (en decimal con punto): ");
                                            cant = teclado.nextDouble();
                                            result = midonacionDefinitiva.donar(cliente,cant);
                                            if(result){
                                                System.out.println(GREEN+"\t       La donacion se ha realizado de forma correcta. GRACIAS POR SU APORTACIÓN! :) "+RESET);
                                            }
                                            else{
                                                System.out.println(RED+"\t       La donacion NO se ha realizado de forma correcta... :( "+RESET);
                                            }
                                            break;
                                        case 2: //2.Consultar el dinero total donado por mi{
                                            System.out.println(PURPLE+"\n\t    ------CONSULTAR EL DINERO TOTAL DONADO POR MI------: "+RESET);
                                            cant = midonacionDefinitiva.getCantidadCliente(cliente);
                                            System.out.println("\t       El dinero total que has donado ha sido "+cant);
                                            break;
                                        case 3: //3.Consultar el dinero total donado en el sistema
                                            System.out.println(PURPLE+"\n\t    ------CONSULTAR EL DINERO TOTAL DONADO EN EL SISTEMA------: "+RESET);
                                            cant = midonacionDefinitiva.getCantidadSistema();
                                            System.out.println("\t       El dinero total donado en el sistema es "+cant);
                                            break;
                                        case 4: //4.Consultar el dinero total donado en mi servidor concreto
                                            System.out.println(PURPLE+"\n\t    ------CONSULTAR EL DINERO TOTAL DONADO EN MI SERVIDOR CONCRETO------: "+RESET);
                                            cant = midonacionDefinitiva.getCantidadReplica();
                                            System.out.println("\t       El dinero total donado en tu replica es "+cant);
                                            break;
                                        case 5: //5.Consultar el numero total de donaciones en el sistema
                                            System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NUMERO TOTAL DE DONACIONES EN EL SISTEMA------: "+RESET);
                                            num = midonacionDefinitiva.getNumDonacionesSistema();
                                            System.out.println("\t       El numero total de donaciones en el sistema es "+num);
                                            break;
                                        case 6: //6.Consultar el numero total de donaciones en mi servidor concreto
                                            System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NUMERO TOTAL DE DONACIONES EN MI SERVIDOR CONCRETO------: "+RESET);
                                            num = midonacionDefinitiva.getNumDonacionesReplica();
                                            System.out.println("\t       El numero total de donaciones en tu servidor concreto es "+num);
                                            break;
                                        case 7: //7.Consultar el numero de clientes totales del sistema
                                            System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NUMERO DE CLIENTES TOTALES DEL SISTEMA------: "+RESET);
                                            num = midonacionDefinitiva.getNumClientesSistema();
                                            System.out.println("\t       El numero de clientes totales del sistema es "+num);
                                            break;
                                        case 8: //8.Consultar el numero de clientes de mi servidor concreto
                                            System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NUMERO DE CLIENTES DE MI SERVIDOR CONCRETO------: "+RESET);
                                            num = midonacionDefinitiva.getNumClientesReplica();
                                            System.out.println("\t       El numero de clientes de tu servidor concreto es "+num);
                                            break;
                                        case 9: //9.Consultar el nombre del cliente que haya donado más dinero en todo el sistema
                                            System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NOMBRE DEL CLIENTE QUE HAYA DONADO MÁS DINERO EN TODO EL SISTEMA------: "+RESET);
                                            if( midonacionDefinitiva.getNumDonacionesSistema()> 0){
                                                nombreConsultado = midonacionDefinitiva.getNombreMayorDonacionSistema();
                                                System.out.println("\t       El nombre del cliente que haya donado más dinero en todo el sistema es "+nombreConsultado);
                                            }
                                            else{
                                                System.out.println("\t       Aún no hay donaciones en el sistema!");

                                            }
                                            break;
                                        case 10: //10.Consultar el nombre del cliente que haya donado más dinero en mi servidor concreto
                                            System.out.println(PURPLE+"\n\t    ------CONSULTAR EL NOMBRE DEL CLIENTE QUE HAYA DONADO MÁS DINERO EN MI SERVIDOR CONCRETO------: "+RESET);
                                            if( midonacionDefinitiva.getNumDonacionesReplica()> 0){
                                                nombreConsultado = midonacionDefinitiva.getNombreMayorDonacionReplica();
                                                System.out.println("\t       El nombre del cliente que haya donado más dinero en esta replica es "+nombreConsultado);
                                            }
                                            else{
                                                System.out.println("\t       Aún no hay donaciones en esta replica!");

                                            }
                                            break;
                                        case 11: //11.Consultar la mayor cantidad donada en todo el sistema
                                            System.out.println(PURPLE+"\n\t    ------CONSULTAR LA MAYOR CANTIDAD DONADA EN TODO EL SISTEMA------: "+RESET);
                                            if( midonacionDefinitiva.getNumDonacionesSistema()> 0){
                                                cant = midonacionDefinitiva.getCantidadMayorDonacionSistema();
                                                System.out.println("\t       La mayor cantidad donada en todo el sistema es "+cant);
                                            }
                                            else{
                                                System.out.println("\t       Aún no hay donaciones en el sistema!");
                                            }
                                            break;
                                        case 12: //12.Consultar la mayor cantidad donada en mi servidor concreto
                                            System.out.println(PURPLE+"\n\t    ------CONSULTAR LA MAYOR CANTIDAD DONADA EN MI SERVIDOR CONCRETO------: "+RESET);
                                            if( midonacionDefinitiva.getNumDonacionesReplica()> 0){
                                                cant = midonacionDefinitiva.getCantidadMayorDonacionReplica();
                                                System.out.println("\t       La mayor cantidad donada en mi servidor concreto "+cant);
                                            }
                                            else{
                                                System.out.println("\t       Aún no hay donaciones en esta replica!");
                                            }
                                            break;
                                        case 13: //13.Darse de baja
                                            System.out.println(PURPLE+"\n\t    ------DARSE DE BAJA------: ");
                                            result = midonacionDefinitiva.darDeBaja(cliente);
                                            if(result){
                                                System.out.println(GREEN+"\t       La operacion se ha realizado de forma correcta. :) "+RESET);
                                                volver = true;
                                            }
                                            else{
                                                System.out.println(RED+"\t       La operacion NO se ha realizado de forma correcta... :( "+RESET);
                                            }
                                            break;
                                        case 14://Salir o Cerrar Sesion
                                            System.out.println(GREEN+"\t       La sesión se ha cerrado de forma correcta. HASTA PRONTO! :) "+RESET);
                                            volver =true;
                                            break;
                                        default:
                                          System.out.println("ERROR: Opcion no valida\n");
                                    }
                                }
                                else{

                                    int subsubMenu = 0;
                                               
                                    System.out.println("\n\tOperaciones disponibles:");
                                    System.out.println("\t    1:  Donar");
                                    System.out.println("\t    2: Darse de baja");
                                    System.out.println("\t    3: Cerrar Sesion / Salir");

                                    System.out.print("\t    --Introducce una opción: ");
                                    subsubMenu = teclado.nextInt();
                                    teclado.nextLine();


                                    switch (subsubMenu) {
                                      case 1: //1.Donar
                                          System.out.println(PURPLE+"\n\t    ------DONAR------: "+RESET);
                                          System.out.print("\t       Introduce la cantidad (en decimal con punto): ");
                                          cant = teclado.nextDouble();
                                          result = midonacionDefinitiva.donar(cliente,cant);
                                          if(result){
                                              System.out.println(GREEN+"\t       La donacion se ha realizado de forma correcta. GRACIAS POR SU APORTACIÓN! :) "+RESET);
                                          }
                                          else{
                                              System.out.println(RED+"\t       La donacion NO se ha realizado de forma correcta... :( "+RESET);
                                          }
                                          break;
                                     case 2: //2.Darse de baja
                                        System.out.println(PURPLE+"\n\t    ------DARSE DE BAJA------: ");
                                        result = midonacionDefinitiva.darDeBaja(cliente);
                                        if(result){
                                            System.out.println(GREEN+"\t       La operacion se ha realizado de forma correcta. :) "+RESET);
                                            volver = true;
                                        }
                                        else{
                                            System.out.println(RED+"\t       La operacion NO se ha realizado de forma correcta... :( "+RESET);
                                        }
                                        break;
                                    case 3://Salir o Cerrar Sesion
                                        System.out.println(GREEN+"\t       La sesión se ha cerrado de forma correcta. HASTA PRONTO! :) "+RESET);
                                        volver =true;
                                        break;
                                    default:
                                      System.out.println("ERROR: Opcion no valida\n");
                                  }
                                }
                            }
                            
                        }
                        
                        
                        
                        
                        break;

                    case 3://Salir
                        System.out.println("\t    Saliendo...");
                        break;

                    default:
                        System.out.println("ERROR: Opcion no valida\n");
                }
                 
            }
        }
        catch(NotBoundException | RemoteException e){
            System.err.println("Exception del sistema: "+ e);
        }
        System.exit(0);
    }
}