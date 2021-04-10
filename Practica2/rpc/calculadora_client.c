/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "calculadora.h"


void
calculadoraprog_1(char *host, double a, char operation, double b)
{
	CLIENT *clnt;
	double  *result;

#ifndef	DEBUG
	clnt = clnt_create (host, CALCULADORAPROG, CALCULADORAVERS, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */

	result = suma_1(a, b, clnt);
	printf("El resultado de la operación %d %c %d =%d", a, operation, b, result);
	if (result == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result = resta_1(a, b, clnt);
	if (result == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result = multiplicacion_1(a, b, clnt);
	if (result == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result = division_1(a, b, clnt);
	if (result == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	 /* DEBUG */
}


int
main (int argc, char *argv[])
{
	//Declaracion de variables
	char *host;
	double a, b;
	char operation;

	if (argc != 5) {
		printf ("usage: %s server_host\n", argv[0]);
		exit (1);
	}

	//Se almacena la peticion
	host = argv[1];
	a = atof(argv[2]);
	b = atof(argv[4]);
	operation = argv[3];


	calculadoraprog_1 (host);


exit (0);
}
