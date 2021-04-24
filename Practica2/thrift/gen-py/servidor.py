# ----------------------------------------------------------------------------
#   Desarrollo de sistemas distribuidos 2020-2021
#   Páctica 2: Apache Thrift
#   Autor: Raquel Molina Reche
#
#   Ejecutar: $python3 servidor.py
# ----------------------------------------------------------------------------

import glob
import sys

from calculadora import Calculadora

#Para importar las estruccturas de datos definidas en el .thrift
from calculadora.ttypes import *


#Instalar el paquete thrift de python

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

#Bibliotecas necesarias para realizar las operaciones
from math import *

import logging

#Para facilitar la depuracion
logging.basicConfig(level=logging.DEBUG)


#Crear una clase handler e implementar los métodos del servicio 
class CalculadoraHandler:
    def __init__(self):
        self.log = {}

    def ping(self):
        print("me han hecho ping()")

    #-------Operaciones básicas-----------
    #Suma
    def suma(self, n1, n2):
        print("sumando " + str(n1) + " con " + str(n2))
        return n1 + n2

    #Resta
    def resta(self, n1, n2):
        print("restando " + str(n1) + " con " + str(n2))
        return n1 - n2

    #Mmultiplicacion
    def multiplicacion(self, n1, n2):
        print("multiplicando " + str(n1) + " * " + str(n2))
        return n1 * n2

    #Division
    def division(self, n1, n2):
        print("calculando division " + str(n1) + " / " + str(n2))
        return n1 / n2

    #Logaritmo
    def logaritmo(self, arg, base):
        print("Calculando logaritmo de " + str(arg) + " en base " + str(base))
        return log(arg,base)
    
    #Potencia
    def potencia(self, base, expon):
        print("Calculando potencia de " + str(base) + " elevado a " + str(expon))
        return pow(base, expon)
    
    #-------Operaciones con vectores-----------
    #Suma de vectores
    def sumavectores(self,  v1,  v2):

        print("Suma de vectores...")

        #print("Suma de ", end=" ")
        #for element in v1:
        #    print (element, end=" ")
        #print(" + ", end=" ")
        #for element in v2:
        #    print (element,  end=" ")
        #print ("\n")

        #Realización del calculo
        resultado = list()
        n = len(v1)
        for i in range(0, n):
            resultado.append( v1[i] + v2[i])

        return resultado

    #Resta de vectores
    def restavectores(self,  v1,  v2):
        
        print("Resta de vectores...")

        #print("Resta de vectores",  end=" ")
        #for element in v1:
        #    print (element, end=" ")
        #print(" + ", end=" ")
        #for element in v2:
        #    print (element, end=" "
        #print ("\n")
        
        #Realización del calculo
        resultado = list()
        n = len(v1)
        for i in range(0, n):
            resultado.append( v1[i] - v2[i])

        return resultado


    #-------Operaciones con vectores 3D-----------
    #Producto escalar de un vector 3D
    def productoescalar3d(self,  v1,  v2):
        print("Calculando producto escalar...")

        #Realización del calculo
        productoEscalar = (v1.x*v2.x) + (v1.y*v2.y) + (v1.z*v2.z)

        return productoEscalar

    #Producto vectorial de un vector 3D
    def productovectorial3d(self,  v1,  v2):
        print("Calculando producto vectorial...")

        #Realización del calculo
        resultado = Vector3D()
        resultado.x = (v1.y*v2.z) - (v1.z*v2.y)
        resultado.y = (v1.x*v2.z) - (v1.z*v2.x)
        resultado.z = (v1.x*v2.y) - (v1.y*v2.x)

        return resultado

    #-------Operaciones con matrices Cuadradas-----------
    #Suma de matrices Cuadradas
    def sumamatrices(self, m1, m2):
        print("Calculando suma de matrices... ")

        resultado = Matriz()

        cols = m1.c
        resultado.c = cols

        fils = m1.f
        resultado.f = fils
        resultado.m = list()

        indice = 0

        #Realización del calculo
        for i in range(0, fils):
            for j in range(0, cols):
                indice = (i*cols)+j
                resultado.m.insert(indice, m1.m[indice] + m2.m[indice])

        return resultado


    #Resta de matrices Cuadradas.#Producto vectorial de un vector 3D
    def restamatrices(self, m1, m2):
        print("Calculando resta de matrices... ")

        resultado = Matriz()

        cols = m1.c
        resultado.c = cols

        fils = m1.f
        resultado.f = fils

        resultado.m = list()
        
        indice = 0

        #Realización del calculo
        for i in range(0, fils):
            for j in range(0, cols):
                indice = (i*cols)+j
                resultado.m.insert(indice, m1.m[indice] - m2.m[indice])


        return resultado


    #Producto de matrices Cuadradas#Producto vectorial de un vector 3D
    def productomatrices(self, m1, m2):
        print("Calculando producto de matrices... ")

        resultado = Matriz()

        cols = m1.c
        resultado.c = cols

        fils = m1.f
        resultado.f = fils

        resultado.m = list()
        
        indice = 0

        #Realización del calculo
        n = fils
        for i in range(0, n):
            for j in range(0, n):
                sumaProd = 0.0
                for k in range(0, n):
                    indice = (i*cols)+j
                    ind_a = (i*cols)+k
                    ind_b = (k*cols)+j
                    sumaProd += m1.m[ind_a] * m2.m[ind_b]
                    resultado.m.insert(indice,sumaProd)

        return resultado


#Crear el objeto server
if __name__ == "__main__":
    handler = CalculadoraHandler()
    processor = Calculadora.Processor(handler)
    transport = TSocket.TServerSocket(host="127.0.0.1", port=9090)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

    print("iniciando servidor...")
    server.serve()
    print("fin")
