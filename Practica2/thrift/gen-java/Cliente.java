// Generated code

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Cliente {

  static final String AZUL = "\u001B[34m";
  static final String AMARILLO = "\u001B[33m";
  static final String MAGENTA = "\u001B[35m";
  static final String RESET_COLOR = "\u001B[0m";

  public static void main(String [] args) {

    try {
      TTransport transport;

      transport = new TSocket("localhost", 9090);
      transport.open();

      TProtocol protocol = new  TBinaryProtocol(transport);
      Calculadora.Client client = new Calculadora.Client(protocol);

      calcular(client);

      transport.close();
    } catch (TException x) {
      x.printStackTrace();
    } 
  }

  private static void calcular(Calculadora.Client client) throws TException
  {
    //Declaracion de variables
    String operation;
    String peticion = "";

    //Operaciones basicas
    double a = 0 , b = 0, resultadoBasicas = 0;
    int op1 = 0, op2=0;

    //Operaciones con Vectores
    List<Double> v1 = new ArrayList<>();
    List<Double> v2 = new ArrayList<>();
    List<Double> resultadoVectores = new ArrayList<>();
    v1.clear();v2.clear();resultadoVectores.clear();
    int dim;

    //Operaciones con Vectores3D
    Vector3D v3D1 = new Vector3D();
    Vector3D v3D2 = new Vector3D();

    //Operaciones con Matrices Cuadradas
    Matriz  m1 = new Matriz();
    Matriz  m2 = new Matriz();
    m1.m = new ArrayList<>();
    m2.m = new ArrayList<>();
    int tam;

    //Scanner
    Scanner scanner = new Scanner(System.in);

    //-------------------MENU---------------
    boolean peticionValida=true;

    int subMenu=0;

    int menuPrincipal=0;
    while( menuPrincipal != 5){
      System.out.println("\nOpciones disponibles:");
      System.out.println("  1: Operaciones Básicas");
      System.out.println("  2: Operaciones con vectores");
      System.out.println("  3: Operaciones con vectores 3D");
      System.out.println("  4: Operaciones con matrices cuadradas");
      System.out.println("  5: Salir");

      System.out.print("  --Introducce una opción: ");

      menuPrincipal = scanner.nextInt();
      scanner.nextLine();

      switch(menuPrincipal){

        case 1: //Menu Operaciones básicas
          System.out.println("\n\t----OPERACIONES BÁSICAS----");

          //Obtener los operandos y la operacion
          int menuOpBasicas = 0;
          while (menuOpBasicas != 7) {

            System.out.println("\n\tOperaciones disponibles:");
            System.out.println("\t    1: Suma");
            System.out.println("\t    2: Resta");
            System.out.println("\t    3: Multiplicacion");
            System.out.println("\t    4: Division");
            System.out.println("\t    5: Logaritmo");
            System.out.println("\t    6: Potencia");
            System.out.println("\t    7: Salir");

            System.out.print("\t    --Introducce una opción: ");
            menuOpBasicas = scanner.nextInt();
            scanner.nextLine();
            peticionValida = false;

            switch(menuOpBasicas){
              case 1: //Suma
                peticion = "+";
                peticionValida = true;

                System.out.print("\n\t    Introduce el primer operando (entero): ");
                op1 = scanner.nextInt();
                System.out.print("\t    Introduce el segundo operando (entero): ");
                op2 = scanner.nextInt();

                //Realizamos llamada a la peticion de calculo para el servidor
                resultadoBasicas = client.suma(op1, op2);
                break;

              case 2: //Resta
                peticion = "-";
                peticionValida = true;

                System.out.print("\t    Introduce el primer operando (entero): ");
                op1 = scanner.nextInt();
                System.out.print("\t    Introduce el segundo operando (entero): ");
                op2 = scanner.nextInt();

                //Realizamos llamada a la peticion de calculo para el servidor;
                resultadoBasicas = client.resta(op1, op2);
                break;

              case 3://Multiplicacion
                peticion = "*";
                peticionValida = true;
                System.out.print("\t    Introduce el primer operando (decimal con coma): ");
                a = scanner.nextDouble();
                System.out.print("\t    Introduce el segundo operando (decimal con coma): ");
                b = scanner.nextDouble();

                //Realizamos llamada a la peticion de calculo para el servidor
                resultadoBasicas = client.multiplicacion(a, b);
                break;

              case 4://#Division
                peticion = "/";
                peticionValida = true;

                System.out.print("\t    Introduce el primer operando (decimal con coma): ");
                a = scanner.nextDouble();
                System.out.print("\t    Introduce el segundo operando (decimal con coma): ");
                b = scanner.nextDouble();

                //Realizamos llamada a la peticion de calculo para el servidor
                resultadoBasicas = client.division(a, b);
                break;

              case 5://Logaritmo
                peticion = "log";
                peticionValida = true;

                System.out.print("\t    Introduce el argumento (decimal con coma): ");
                a = scanner.nextDouble();
                System.out.print("\t    Introduce la base (decimal con coma): ");
                b = scanner.nextDouble();

                //Realizamos llamada a la peticion de calculo para el servidor
                resultadoBasicas = client.logaritmo(a, b);
                break;

              case 6://Potencia
                peticion = "^";
                peticionValida = true;

                System.out.print("\t    Introduce la base (decimal con coma): ");
                a = scanner.nextDouble();
                System.out.print("\t    Introduce el exponente (entero): ");
                op1 = scanner.nextInt();

                //Realizamos llamada a la peticion de calculo para el servidor
                resultadoBasicas = client.potencia(a, op1);
                break;

              case 7: //Exit
                System.out.println("\t    Saliendo...");
                break;

              default:
                System.out.println("ERROR: Opcion no valida\n");
            }

            //SE IMPRIME EL RESULTADO DE LA OPERACION
            if (peticionValida){
              if(peticion == "+" || peticion== "-"){
                System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
                System.out.println(AZUL+"  El resultado de la operación "+AMARILLO+op1+" "+AZUL+peticion+AMARILLO+" "+op2+AZUL+" = "+MAGENTA+resultadoBasicas+RESET_COLOR);
                System.out.println(AZUL+"--------------------------------------------------------------------------"+RESET_COLOR);
              }
              else if(peticion == "^"){
                System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
                System.out.println(AZUL+"  El resultado de la operación "+AMARILLO+a+" "+AZUL+peticion+AMARILLO+" "+op1+AZUL+" = "+MAGENTA+resultadoBasicas+RESET_COLOR);
                System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
              }
              else {
                System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
                System.out.println(AZUL+"  El resultado de la operación "+AMARILLO+a+" "+AZUL+peticion+AMARILLO+" "+b+AZUL+" = "+MAGENTA+resultadoBasicas+RESET_COLOR);
                System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
              }
            }

          }
          break;

        case 2://Menu vectores
          //Menu de operaciones con vectores
          System.out.println("\n\t----OPERACIONES CON VECTORES----");

          //Obtener los operandos y la operacion

          //1. Obtener el tamaño que tendran los vectores
          dim=0;
          do{
            System.out.print("\tIntroduce un tamaño positivo mayor que 0: ");
            dim = scanner.nextInt();
          }while(dim <= 0);

          //2. Obtener el contenido del primer vector
          System.out.println("\tContenido del primer vector (v1) (decimal con coma):");
          for(int i=0; i < dim; i++){
            System.out.print("\tv1["+i+"]: ");
            double valor = scanner.nextDouble();
            v1.add(valor);
          }

          //3. Obtener el contenido del segundo vector
          System.out.println("\n\tContenido del segundo vector (v2) (decimal con coma):");
          for(int i=0; i < dim; i++){
            System.out.print("\tv2["+i+"]: ");
            double valor = scanner.nextDouble();
            v2.add(valor);
          }

          //4. Operaciones disponibles con esos vectores
          subMenu = 0;
          while (subMenu != 3) {

            System.out.println("\tOperaciones disponibles:");
            System.out.println("\t    1: Suma");
            System.out.println("\t    2: Resta");
            System.out.println("\t    3: Salir");

            System.out.print("\t    --Introducce una opción: ");
            subMenu = scanner.nextInt();
            scanner.nextLine();

            peticionValida = false;

            switch (subMenu) {
              case 1: //Suma de vectores
                peticion = "+";
                peticionValida = true;

                //Realizamos llamada a la peticion de calculo para el servidor
                resultadoVectores = client.sumavectores(v1, v2);
                break;

              case 2://Resta de vectores
                peticion = "-";
                peticionValida = true;

                //Realizamos llamada a la peticion de calculo para el servidor
                resultadoVectores = client.restavectores(v1, v2);
                break;

              case 3: //Exit
                System.out.println("Saliendo...");
                break;

              default:
                System.out.println("ERROR: Opcion no valida\n");
            }

            //SE IMPRIME EL RESULTADO DE LA OPERACION
            if (peticionValida) {
              System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
              //System.out.println("{AZUL}  El resultado de la operación {AMARILLO} {a} {AZUL} {peticion} {AMARILLO} {b} {AZUL} = {MAGENTA} {resultado} {RESET_COLOR}");
              System.out.println(AZUL+"  El resultado de la operación : "+AMARILLO);

              //Se imprime el primer vector
              System.out.print(v1);

              //Se impime la operacion
              System.out.println(AZUL+"\n\t\t" + peticion + "\t"+AMARILLO);

              //Se imprime el segundo vector
              System.out.print(v2);

              //Se imprime el vector resultado
              System.out.println("\n\t\t"+AZUL+" = "+MAGENTA);
              System.out.print(resultadoVectores);

              System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);

            }
          }
          break;

        case 3://Menu vectores 3D
          //Menu de operaciones con vectores 3D
          System.out.println("\n\t----OPERACIONES CON  VECTORES 3D----");

          //Obtener los operandos y la operacion

          //1. Obtener el contenido del primer vector
          System.out.println("\tContenido del primer vector (v1) (decimal con coma):");
          System.out.print("\t      x:");
          v3D1.x = scanner.nextDouble();
          System.out.print("\t      y:");
          v3D1.y = scanner.nextDouble();
          System.out.print("\t      z:");
          v3D1.z = scanner.nextDouble();

          //2. Obtener el contenido del segundo vector
          System.out.println("\tContenido del segundo vector (v1) (decimal con coma):");
          System.out.print("\t      x:");
          v3D2.x = scanner.nextDouble();
          System.out.print("\t      y:");
          v3D2.y = scanner.nextDouble();
          System.out.print("\t      z:");
          v3D2.z = scanner.nextDouble();

          //3. Operaciones disponibles con esos vectores
          subMenu = 0;
          Vector3D resultado3D = new Vector3D();
          double resultadoEscalar;

          while (subMenu != 3) {

            System.out.println("\tOperaciones disponibles:");
            System.out.println("\t    1: Producto escalar");
            System.out.println("\t    2: Producto vectorial");
            System.out.println("\t    3: Salir");

            System.out.print("\t    --Introducce una opción: ");
            subMenu = scanner.nextInt();
            scanner.nextLine();

            peticionValida = false;

            switch (subMenu) {
              case 1: //Producto escalar
                //Realizamos llamada a la peticion de calculo para el servidor
                resultadoEscalar = client.productoescalar3d(v3D1,v3D2);

                //SE IMPRIME EL RESULTADO DE LA OPERACION
                System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
                System.out.println(AZUL+"  El resultado de la operación:");
                //Se imprime la operacion y el resultado que en este caso es un numero
                System.out.println("\n\t"+AMARILLO+"("+v3D1.x+","+v3D1.y+", "+v3D1.z+")"+AZUL+" * "+AMARILLO+"("+v3D2.x+","+v3D2.y+", "+v3D2.z+")"+AZUL+" = "+MAGENTA+" "+resultadoEscalar);
                System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
                break;

              case 2://Producto vectorial
                //Realizamos llamada a la peticion de calculo para el servidor
                resultado3D = client.productovectorial3d(v3D1,v3D2);

                //SE IMPRIME EL RESULTADO DE LA OPERACION
                System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
                System.out.println("{AZUL}  El resultado de la operación:");
                //Se imprime la operacion
                System.out.println("\n\t"+AMARILLO+"("+v3D1.x+", "+v3D1.y+", "+v3D1.z+")"+AZUL+" · "+AMARILLO+"("+v3D2.x+", "+v3D2.y+", "+v3D2.z+")");
                //Se imprime el vector resultado que en este caso en otro vector 3D
                System.out.println(AZUL+" = "+MAGENTA+"("+resultado3D.x+", "+resultado3D.y+", "+resultado3D.z+")");
                System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
                break;

              case 3: //Exit
                System.out.println("Saliendo...");
                break;

              default:
                System.out.println("ERROR: Opcion no valida\n");
            }

          }
          break;

        case 4://Menu matrices
          //Menu de operaciones con matrices cuadradas
          System.out.println("\n\t----OPERACIONES CON MATRICES CUADRADAS----");

          //Obtener los operandos y la operacion
          Matriz resultadoMatrices = new Matriz();
          resultadoMatrices.m = new ArrayList<>();

          //1. Obtener el tamaño que tendran las matrices
          tam=0;
          do{
            System.out.print("\tIntroduce un tamaño positivo mayor que 0: ");
            tam = scanner.nextInt();
          }while(tam <= 0);

          //2. Obtener el contenido de la primera matriz
          m1.c = tam;
          m1.f = tam;

          System.out.println("\tContenido de la primera matriz (m1) (decimal con coma):");
          for(int i=0; i < tam; i++){
            for(int j=0; j < tam; j++){
              System.out.print("\tm1["+i+"]["+j+"]: ");
              double valor = scanner.nextDouble();
              m1.m.add(valor);
            }
          }

          //3. Obtener el contenido de la segunda matriz
          m2.c = tam;
          m2.f = tam;
          System.out.println("\tContenido del segunda matriz (m2) (decimal con coma):");
          for(int i=0; i < tam; i++){
            for(int j=0; j < tam; j++){
              System.out.print("\tm2["+i+"]["+j+"]: ");
              double valor = scanner.nextDouble();
              m2.m.add(valor);
            }
          }
          //4. Operaciones disponibles con esas matrices
          subMenu = 0;
          while (subMenu != 4) {

            System.out.println("\tOperaciones disponibles:");
            System.out.println("\t    1: Suma");
            System.out.println("\t    2: Resta");
            System.out.println("\t    3: Producto");
            System.out.println("\t    4: Salir");

            System.out.print("\t    --Introducce una opción: ");
            subMenu = scanner.nextInt();
            scanner.nextLine();

            peticionValida = false;

            switch (subMenu) {
              case 1: //Suma de matrices
                peticion="+";
                peticionValida = true;

                //Realizamos llamada a la peticion de calculo para el servidor
                resultadoMatrices = client.sumamatrices( m1, m2);
                break;

              case 2://Resta de matrices
                peticion="-";
                peticionValida = true;

                //Realizamos llamada a la peticion de calculo para el servidor
                resultadoMatrices = client.restamatrices( m1, m2);
                break;

              case 3: //Producto de matrices
                peticion="*";
                peticionValida = true;

                //Realizamos llamada a la peticion de calculo para el servidor
                resultadoMatrices = client.productomatrices( m1, m2);
                break;

              case 4: //Exit
                System.out.println("Saliendo...");
                break;

              default:
                System.out.println("ERROR: Opcion no valida\n");
            }

            //SE IMPRIME EL RESULTADO DE LA OPERACION
            if (peticionValida) {
              System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
              System.out.println(AZUL+"  El resultado de la operación: "+AMARILLO);

              //Se imprime la primera matriz
              for(int i=0; i < tam; i++){
                System.out.print("\t\t");
                for(int j=0; j < tam; j++){
                  System.out.print( m1.m.get((m1.c * i) + j) + " ");
                }
                System.out.println("");
              }
              //Se impime la operacion
              System.out.println(AZUL+"\t\t{peticion}\t"+AMARILLO);

              //Se imprime la segunda matriz
              for(int i=0; i < tam; i++){
                System.out.print("\t\t");
                for(int j=0; j < tam; j++){
                  System.out.print( m2.m.get((m2.c * i) + j) + " ");
                }
                System.out.println("");
              }

              //Se imprime la matriz resultado
              System.out.println("\t\t"+AZUL+" = "+MAGENTA);
              for(int i=0; i < tam; i++){
                System.out.print("\t\t");
                for(int j=0; j < tam; j++){
                  System.out.print( resultadoMatrices.m.get((resultadoMatrices.c * i) + j) + " ");
                }
                System.out.println("");
              }
              System.out.println("\n"+AZUL+"-------------------------------------------------------------------------"+RESET_COLOR);
            }

          }
          break;

        case 5: //Exit
          System.out.println("Saliendo...");
          break;

        default:
          System.out.println("ERROR: Opcion no valida\n");
      }

    }
    System.out.println("Fin");

  }

}