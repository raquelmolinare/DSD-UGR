/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
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
prodescalar3d_1_svc(operationVectores3D operands,  struct svc_req *rqstp)
{
	static responseBasic  result;

	double *calculationp;
	double productoEscalar;


	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseBasic, &result);
	
	//El puntero calculationp apunta a la direccion de memoria de result.response_u.result
	calculationp = &result.responseBasic_u.result;

	//Se calcula el resultado de la operacion
	productoEscalar = (operands.first.x*operands.second.x) + (operands.first.y*operands.second.y) + (operands.first.z*operands.second.z);

	//Se cambia el contenido hacia donde apunta calculationp por el resultado de la operacion
	//Por lo que se cambia el result.response_u.result
	(*calculationp) = productoEscalar;

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

	//Producto vectorial v1 x v2 = resultado
	result.responseVectores_u.vResult.vectorData_val[0] = (operands.first.y * operands.second.z) - (operands.first.z * operands.second.y); //x
	result.responseVectores_u.vResult.vectorData_val[1] = (operands.first.x * operands.second.z) - (operands.first.z * operands.second.x); //y
	result.responseVectores_u.vResult.vectorData_val[2] = (operands.first.x * operands.second.y) - (operands.first.y * operands.second.x); //z

	return &result;
}

responseMatrices *
sumamatrices_1_svc(operationMatrices operands,  struct svc_req *rqstp)
{
	static responseMatrices  result;

	/*
	 * insert server code here
	 */

	return &result;
}
