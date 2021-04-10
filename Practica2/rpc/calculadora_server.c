/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "calculadora.h"

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

responseVectores *
sumavectores_1_svc(vectorData v1, vectorData v2,  struct svc_req *rqstp)
{
	static responseVectores  result;

	//Puntero a la dimension del vector
	int *dim;
	dim = &result.responseVectores_u.v.vectorData_len;

	vectorData *values;
	values = &result.responseVectores_u.v.vectorData_val;


	//Se libera la memoria que se asigno en una ejecucion previa del servidor para el resultado
	xdr_free(xdr_responseVectores, &result);
	
	//Se asigna la dimension
	(*dim) = v1.vectorData_len;
	//Se redimensiona
	values->vectorData_val = malloc(v1.vectorData_len*sizeof(double));

	printf("1: hola\n");

	//Se calcula el resultado de la operacion
	for(int i = 0; i < v1.vectorData_len;i++){
		values->vectorData_val[i] =  v1.vectorData_val[i] + v2.vectorData_val[i];
		//result.responseVectores_u.v.vectorData_val[i]  =  v1.vectorData_val[i] + v2.vectorData_val[i];
		printf("%f + %f = %f\n", v1.vectorData_val[i],v2.vectorData_val[i],result.responseVectores_u.v.vectorData_val[i]);
	}

	printf("2: hola\n");

	return &result;
}