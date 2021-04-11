/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

/**
 *	Autor: Raquel Molina Reche
 * 
 *	Compilación:  gcc calculadora_server.c calculadora_svc.c calculadora_xdr.c -o servidor -lnsl -lm
 */

#include "calculadora.h"
#include <stdio.h>
#include <math.h>

responseBasic *
suma_1_svc(operationBasic operands,  struct svc_req *rqstp)
{
	static responseBasic  result;

	double *calculationp;
	double calculation;

	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseBasic, &result);

	//El puntero calculationp apunta a la direccion de memoria de result.response_u.result
	calculationp = &result.responseBasic_u.result;

	//Se calcula el resultado de la operacion
	calculation = operands.first + operands.second;


	//Se cambia el contenido hacia donde apunta calculationp por el resultado de la operacion
	//Por lo que se cambia el result.response_u.result
	(*calculationp) = calculation;

	return &result;
}

responseBasic *
resta_1_svc(operationBasic operands,  struct svc_req *rqstp)
{
	static responseBasic  result;

	double *calculationp;
	double calculation;

	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseBasic, &result);

	//El puntero calculationp apunta a la direccion de memoria de result.response_u.result
	calculationp = &result.responseBasic_u.result;

	//Se calcula el resultado de la operacion
	calculation = operands.first - operands.second;


	//Se cambia el contenido hacia donde apunta calculationp por el resultado de la operacion
	//Por lo que se cambia el result.response_u.result
	(*calculationp) = calculation;

	return &result;
}

responseBasic *
multiplicacion_1_svc(operationBasic operands,  struct svc_req *rqstp)
{
	static responseBasic  result;

	double *calculationp;
	double calculation;

	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseBasic, &result);

	//El puntero calculationp apunta a la direccion de memoria de result.response_u.result
	calculationp = &result.responseBasic_u.result;

	//Se calcula el resultado de la operacion
	calculation = operands.first * operands.second;


	//Se cambia el contenido hacia donde apunta calculationp por el resultado de la operacion
	//Por lo que se cambia el result.response_u.result
	(*calculationp) = calculation;

	return &result;
}

responseBasic *
division_1_svc(operationBasic operands,  struct svc_req *rqstp)
{
	static responseBasic  result;

	double *calculationp;
	double calculation;

	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseBasic, &result);

	//El puntero calculationp apunta a la direccion de memoria de result.response_u.result
	calculationp = &result.responseBasic_u.result;

	//Se calcula el resultado de la operacion
	calculation = operands.first / operands.second;


	//Se cambia el contenido hacia donde apunta calculationp por el resultado de la operacion
	//Por lo que se cambia el result.response_u.result
	(*calculationp) = calculation;

	return &result;
}

responseBasic *
logaritmo_1_svc(operationBasic operands,  struct svc_req *rqstp)
{
	static responseBasic  result;

	double *calculationp;
	double calculation;

	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseBasic, &result);

	//El puntero calculationp apunta a la direccion de memoria de result.response_u.result
	calculationp = &result.responseBasic_u.result;

	//Se calcula el resultado de la operacion
	calculation = log(operands.first) / log(operands.second); 


	//Se cambia el contenido hacia donde apunta calculationp por el resultado de la operacion
	//Por lo que se cambia el result.response_u.result
	(*calculationp) = calculation;

	return &result;
}

responseBasic *
potencia_1_svc(operationBasic operands,  struct svc_req *rqstp)
{
	static responseBasic  result;

	double *calculationp;
	double calculation;

	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseBasic, &result);

	//El puntero calculationp apunta a la direccion de memoria de result.response_u.result
	calculationp = &result.responseBasic_u.result;

	//Se calcula el resultado de la operacion
	calculation = pow(operands.first, operands.second);


	//Se cambia el contenido hacia donde apunta calculationp por el resultado de la operacion
	//Por lo que se cambia el result.response_u.result
	(*calculationp) = calculation;

	return &result;
}

responseVectores *
sumavectores_1_svc(operationVectores operands,  struct svc_req *rqstp)
{
	static responseVectores  result;

	//Puntero a la dimension del vector
	int *dim;
	dim = &result.responseVectores_u.vResult.vectorData_len;

	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseVectores, &result);
	
	//Se asigna la dimension
	(*dim) =  operands.first.vectorData_len;
	//Se redimensiona
	result.responseVectores_u.vResult.vectorData_val = malloc(operands.first.vectorData_len*sizeof(double));

	//Se calcula el resultado de la operacion
	for(int i = 0; i < operands.first.vectorData_len;i++){
		result.responseVectores_u.vResult.vectorData_val[i]  = operands.first.vectorData_val[i] + operands.second.vectorData_val[i];
	}

	return &result;
}

responseVectores *
restavectores_1_svc(operationVectores operands,  struct svc_req *rqstp)
{
	static responseVectores  result;

	//Puntero a la dimension del vector
	int *dim;
	dim = &result.responseVectores_u.vResult.vectorData_len;

	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseVectores, &result);
	
	//Se asigna la dimension
	(*dim) =  operands.first.vectorData_len;
	//Se redimensiona
	result.responseVectores_u.vResult.vectorData_val = malloc(operands.first.vectorData_len*sizeof(double));

	//Se calcula el resultado de la operacion
	for(int i = 0; i < operands.first.vectorData_len;i++){
		result.responseVectores_u.vResult.vectorData_val[i]  = operands.first.vectorData_val[i] - operands.second.vectorData_val[i];
	}

	return &result;
}

responseBasic *
prodescalar3d_1_svc(operationVectores operands,  struct svc_req *rqstp)
{
	static responseBasic  result;

	double *calculationp;
	double productoEscalar;


	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseBasic, &result);
	
	//El puntero calculationp apunta a la direccion de memoria de result.response_u.result
	//calculationp = &result.responseBasic_u.result;

	//Se calcula el resultado de la operacion
	productoEscalar = (operands.first.vectorData_val[0]*operands.second.vectorData_val[0]) + (operands.first.vectorData_val[1]*operands.second.vectorData_val[1]) + (operands.first.vectorData_val[2]*operands.second.vectorData_val[2]);
	printf ("producto escalar: %f\n", productoEscalar);


	//Se cambia el contenido hacia donde apunta calculationp por el resultado de la operacion
	//Por lo que se cambia el result.response_u.result
	result.responseBasic_u.result = productoEscalar;

	return &result;
}

responseVectores *
prodvectorial3d_1_svc(operationVectores3D operands,  struct svc_req *rqstp)
{
	static responseVectores  result;

	//Puntero a la dimension del vector
	int *dim;
	dim = &result.responseVectores_u.vResult.vectorData_len;

	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseVectores, &result);
	
	//Se asigna la dimension
	(*dim) =  3;
	//Se redimensiona
	result.responseVectores_u.vResult.vectorData_val = malloc(3*sizeof(double));

	printf ("1: hola\n");


	//Vector a = first - second
	vector3D a;
	a.x= operands.first.x-operands.second.x;
	a.y= operands.first.y-operands.second.y;
	a.z= operands.first.z-operands.second.z;

	printf ("a--> x:%f, y:%f, z:%f\n",a.x, a.y, a.z);


	//Vector b = third - second
	vector3D b;
	b.x= operands.third.x-operands.second.x;
	b.y= operands.third.y-operands.second.y;
	b.z= operands.third.z-operands.second.z;

	printf ("b--> x:%f, y:%f, z:%f\n",b.x, b.y, b.z);


	//Producto vectorial a x b = resultado
	result.responseVectores_u.vResult.vectorData_val[0] = (a.y * b.z) - (a.z * b.y); //x
	result.responseVectores_u.vResult.vectorData_val[1] = (a.x * b.z) - (a.z * b.x); //y
	result.responseVectores_u.vResult.vectorData_val[2] = (a.x * b.y) - (a.y * b.x); //z

	printf ("4: resultado x:%f y:%f z:%f\n", result.responseVectores_u.vResult.vectorData_val[0],result.responseVectores_u.vResult.vectorData_val[1],result.responseVectores_u.vResult.vectorData_val[2] );

	
	return &result;
}

responseMatrices *
sumamatrices_1_svc(operationMatrices arg1,  struct svc_req *rqstp)
{
	static responseMatrices  result;

	/*
	 * insert server code here
	 */

	return &result;
}
