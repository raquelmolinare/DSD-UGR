# ----------------------------------------------------------------------------
#   Desarrollo de sistemas distribuidos 2020-2021
#   Páctica 2: Apache Thrift
#   Autor: Raquel Molina Reche
#
#   Ejecutar: $python3 cliente.py
# ----------------------------------------------------------------------------

from calculadora import Calculadora

#Para importar las estruccturas de datos definidas en el .thrift
from calculadora.ttypes import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

transport = TSocket.TSocket("localhost", 9090)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)

#Se crea el cliente
client = Calculadora.Client(protocol)

transport.open()

#Variables para el uso de colores en la salida
AZUL = "\x1b[34m"
MAGENTA = "\x1b[35m"
AMARILLO = "\x1b[33m"
RESET_COLOR	 = "\x1b[0m"

#print("hacemos ping al server")
#client.ping()

#Declaracion de variables necesarias

#Operaciones con vectores
v1 = list()
v2 = list()
dim = int()

#Operaciones con vectores 3D
v3D1 = Vector3D()
v3D2 = Vector3D()

#Operaciones con matrices Cuadradas
m1 = Matriz()
m2 = Matriz()

tam = int()
m1.m = list() 
m2.m = list() 

#-------------------MENU---------------

def pedirOpcion():
    correcto=False
    num=0
    while(not correcto):
        try:
            num = int(input("Introduce un numero entero: "))
            correcto=True
        except ValueError:
            print('Error, introduce un numero entero')
    return num
 
subMenu = 0
menuPrincipal = 0

while menuPrincipal != 5:

    print ("\nOpciones disponibles:")
    print ("  1: Operaciones Básicas")
    print ("  2: Operaciones con vectores")
    print ("  3: Operaciones con vectores 3D")
    print ("  4: Operaciones con matrices cuadradas")
    print ("  5: Salir")
     
    menuPrincipal = int(input("\n--Introducce una opción: "))

    if menuPrincipal == 1: #Menu Operaciones básicas
        #Menu de operacione basicas
        print ("\n\t----OPERACIONES BÁSICAS----")

        #Obtener los operandos y la operacion
        peticion = ""        
        menuOpBasicas = 0
        while menuOpBasicas != 7:

            print ("\tOperaciones disponibles:")
            print ("\t    1: Suma")
            print ("\t    2: Resta")
            print ("\t    3: Multiplicacion")
            print ("\t    4: Division")
            print ("\t    5: Logaritmo")
            print ("\t    6: Potencia")
            print ("\t    7: Salir")
            
            menuOpBasicas = int(input("\n\t    --Introducce una opción: "))
            peticionValida = False

            if menuOpBasicas == 1: #Suma
                peticion="+"
                peticionValida = True
                a = int(input("\t    Introduce el primer operando (entero): "))
                b = int(input("\t    Introduce el segundo operando (entero): "))

                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.suma(a, b)

            elif menuOpBasicas == 2: #Resta
                peticion="-"
                peticionValida = True
                a = int(input("\t    Introduce el primer operando (entero): "))
                b = int(input("\t    Introduce el segundo operando (entero): "))

                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.resta(a, b)

            elif menuOpBasicas == 3: #Multiplicacion
                peticion="*"
                peticionValida = True
                a = float(input("\t    Introduce el primer operando: "))
                b = float(input("\t    Introduce el segundo operando: "))

                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.multiplicacion(a, b)

            elif menuOpBasicas == 4: #Division
                peticion="/"
                peticionValida = True
                a = float(input("\t    Introduce el primer operando: "))
                b = float(input("\t    Introduce el segundo operando: "))

                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.division(a, b)

            elif menuOpBasicas == 5: #Logaritmo
                peticion="log"
                peticionValida = True
                a = float(input("\t    Introduce el argumento: "))
                b = float(input("\t    Introduce la base: "))

                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.logaritmo(a, b)

            elif menuOpBasicas == 6: #Potencia
                peticion="^"
                peticionValida = True
                a = float(input("\t    Introduce la base: "))
                b = int(input("\t    Introduce el exponente (entero): "))

                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.potencia(a, b)

            elif menuOpBasicas == 7: #Salir
                print("\t    Saliendo...")
            
            else: #Opcion no valida
                print("\t    ERROR: Opcion no valida\n")
            
            #SE IMPRIME EL RESULTADO DE LA OPERACION
            if peticionValida:
                print (f"\n{AZUL}-------------------------------------------------------------------------{RESET_COLOR}\n")
                print ( f"{AZUL}  El resultado de la operación {AMARILLO} {a} {AZUL} {peticion} {AMARILLO} {b} {AZUL} = {MAGENTA} {resultado} {RESET_COLOR}")
                print(f"\n{AZUL}--------------------------------------------------------------------------{RESET_COLOR}\n")

    elif menuPrincipal == 2: #Menu vectores
        #Menu de operaciones con vectores
        print ("\n\t----OPERACIONES CON VECTORES----")
        
        #Obtener los operandos y la operacion

        #1. Obtener el tamaño que tendran los vectores
        dim = int(input("\tIntroduce el tamaño de los vectores: "))
        while dim <= 0:
            dim = int(input("\tIntroduce un tamaño positivo mayor que 0: "))

        #2. Obtener el contenido del primer vector
        print ("\tContenido del primer vector (v1):")
        for i in range(0,dim):
            v1.append(float(input(f"\tv1[{i}]: ")))

        #3. Obtener el contenido del segundo vector
        print ("\tContenido del segundo vector (v2):")
        for i in range(0,dim):
            v2.append(float(input(f"\tv2[{i}]: ")))

        #4. Operaciones disponibles con esos vectores
        subMenu = 0
        while subMenu != 3:
            print ("\tOperaciones disponibles:")
            print ("\t    1: Suma")
            print ("\t    2: Resta")
            print ("\t    3: Salir")
            
            subMenu = int(input("\n\t    --Introducce una opción: "))
            peticionValida = False

            if subMenu == 1: #Suma de vectores
                peticion="+"
                peticionValida = True

                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.sumavectores(v1,v2)

            elif subMenu == 2: #Resta de vectores
                peticion="-"
                peticionValida = True

                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.restavectores(v1,v2)
            
            elif subMenu == 3: #Salir
                print("\t    Saliendo...")
            
            else: #Opcion no valida
                print("\t    ERROR: Opcion no valida\n")
            
            #SE IMPRIME EL RESULTADO DE LA OPERACION
            if peticionValida:
                print (f"\n{AZUL}-------------------------------------------------------------------------{RESET_COLOR}")
                print ( f"{AZUL}  El resultado de la operación: {AMARILLO}", end="\n\t")
                
                #Se imprime el primer vector
                for element in v1:
                    print (element, end="  ")

                #Se impime la operacion
                print ( f"{AZUL}\n\t\t{peticion}\t{AMARILLO}", end="\n\t")

                #Se imprimie el segundo vector
                for element in v2:
                    print (element, end="  ")

                #Se imprime el vector resultado
                print ( f"\n\t\t{AZUL} = {MAGENTA}", end="\n\t")
                for element in resultado:
                    print (element, end="  ")

                print(f"\n{AZUL}--------------------------------------------------------------------------{RESET_COLOR}\n")

    elif menuPrincipal == 3: #Menu vectores 3D
        #Menu de operaciones con vectores 3D
        print ("\n\t----OPERACIONES CON  VECTORES 3D----")
        
        #Obtener los operandos y la operacion

        #1. Obtener el contenido del primer vector
        print ("\tContenido del primer vector (v1):")
        v3D1.x = float(input("\t      x:"))
        v3D1.y = float(input("\t      y:"))
        v3D1.z = float(input("\t      z:"))

        #2. Obtener el contenido del segundo vector
        print ("\tContenido del segundo vector (v2):")
        v3D2.x = float(input("\t      x:"))
        v3D2.y = float(input("\t      y:"))
        v3D2.z = float(input("\t      z:"))

        #3. Operaciones disponibles con esos vectores
        subMenu = 0
        while subMenu != 3:
            print ("\tOperaciones disponibles:")
            print ("\t    1: Producto escalar")
            print ("\t    2: Producto vectorial")
            print ("\t    3: Salir")
            
            subMenu = int(input("\n\t    --Introducce una opción: "))
            peticionValida = False

            if subMenu == 1: #Producto escalar
                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.productoescalar3d(v3D1,v3D2)

                #SE IMPRIME EL RESULTADO DE LA OPERACION
                print (f"\n{AZUL}-------------------------------------------------------------------------{RESET_COLOR}")
                print ( f"{AZUL}  El resultado de la operación:")
                #Se imprime la operacion y el resultado que en este caso es un numero
                print ( f"\n\t{AMARILLO}({v3D1.x},{v3D1.y}, {v3D1.z}){AZUL} * {AMARILLO}({v3D2.x},{v3D2.y}, {v3D2.z}){AZUL} = {MAGENTA} {resultado}")
                print(f"\n{AZUL}--------------------------------------------------------------------------{RESET_COLOR}\n")

            elif subMenu == 2: #Producto vectorial
                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.productovectorial3d(v3D1,v3D2)
                #SE IMPRIME EL RESULTADO DE LA OPERACION
                print (f"\n{AZUL}-------------------------------------------------------------------------{RESET_COLOR}")
                print ( f"{AZUL}  El resultado de la operación:")
                #Se imprime la operacion
                print ( f"\n\t{AMARILLO}({v3D1.x}, {v3D1.y}, {v3D1.z}){AZUL} · {AMARILLO}({v3D2.x}, {v3D2.y}, {v3D2.z})", end="")
                #Se imprime el vector resultado que en este caso en otro vector 3D
                print ( f"{AZUL} = {MAGENTA}({resultado.x}, {resultado.y}, {resultado.z})")
                print(f"\n{AZUL}--------------------------------------------------------------------------{RESET_COLOR}\n")
            
            elif subMenu == 3: #Salir
                print("\t    Saliendo...")
            
            else: #Opcion no valida
                print("\t    ERROR: Opcion no valida\n")

    elif menuPrincipal == 4: #Menu matrices
        #Menu de operaciones con matrices cuadradas
        print ("\n\t----OPERACIONES CON MATRICES CUADRADAS----")
        
        #Obtener los operandos y la operacion

        #1. Obtener el tamaño que tendran las matrices
        tam = int(input("\tIntroduce el tamaño de las matrices: "))
        while tam <= 0:
            tam = int(input("\tIntroduce un tamaño positivo mayor que 0: "))

        #2. Obtener el contenido de la primera matriz
        m1.c = tam
        m1.f = tam
        print ("\tContenido de la primera matriz (m1):")
        for i in range(0,tam):
            for j in range(0,tam):
                m1.m.append(float(input(f"\tm1[{i}][{j}]: ")))

        #3. Obtener el contenido de la segunda matriz
        m2.c = tam
        m2.f = tam
        print ("\tContenido del segunda matriz (m2):")
        for i in range(0,tam):
            for j in range(0,tam):
                m2.m.append(float(input(f"\tm2[{i}][{j}]: ")))

        #4. Operaciones disponibles con esas matrices
        subMenu = 0
        while subMenu != 4:
            print ("\tOperaciones disponibles:")
            print ("\t    1: Suma")
            print ("\t    2: Resta")
            print ("\t    3: Producto")
            print ("\t    4: Salir")
            
            subMenu = int(input("\n\t    --Introducce una opción: "))
            peticionValida = False

            if subMenu == 1: #Suma de matrices
                peticion="+"
                peticionValida = True

                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.sumamatrices( m1, m2)

            elif subMenu == 2: #Resta de matrices
                peticion="-"
                peticionValida = True

                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.restamatrices( m1, m2)

            elif subMenu == 3: #Producto de matrices
                peticion="*"
                peticionValida = True

                #Realizamos llamada a la peticion de calculo para el servidor
                resultado = client.productomatrices( m1, m2)
            
            elif subMenu == 4: #Salir
                print("\t    Saliendo...")
            
            else: #Opcion no valida
                print("\t    ERROR: Opcion no valida\n")
            
            #SE IMPRIME EL RESULTADO DE LA OPERACION
            if peticionValida:
                print (f"\n{AZUL}-------------------------------------------------------------------------{RESET_COLOR}")
                print ( f"{AZUL}  El resultado de la operación: {AMARILLO}")
                
                #Se imprime la primera matriz
                for i in range(0,m1.f):
                    print ("\t", end="")
                    for j in range(0,m1.c):
                        indice = (i*m1.c)+j
                        print (m1.m[indice], end="  ")
                    print ("\n", end="")

                #Se impime la operacion
                print ( f"{AZUL}\t\t{peticion}\t{AMARILLO}")

                #Se imprime la segunda matriz
                for i in range(0,m2.f):
                    print ("\t", end="")
                    for j in range(0,m2.c):
                        indice = (i*m2.c)+j
                        print (m2.m[indice], end="  ")
                    print ("\n", end="")

                #Se imprime la matriz resultado
                print ( f"\t\t{AZUL} = {MAGENTA}")
                for i in range(0,resultado.f):
                    print ("\t", end="")
                    for j in range(0,resultado.c):
                        indice = (i*resultado.c)+j
                        print (resultado.m[indice], end="  ")
                    print ("\n", end="")


                print(f"{AZUL}--------------------------------------------------------------------------{RESET_COLOR}\n")

    elif menuPrincipal == 5:
        print("Saliendo...")

    else:
        print("ERROR: Opcion no valida\n")
 
print ("Fin")