/**
 *	Autor: Raquel Molina Reche
 * 
 *	Compilación:	gcc calculadora_client.c calculadora_clnt.c calculadora_xdr.c -o cliente -lnsl
 *	Ejecución:		./cliente localhost
 */

#include "calculadora.h"

#include <stdio.h>

//------Colores para la salida por la terminal-----
#define RESET_COLOR	"\x1b[0m"
#define NEGRO_T		"\x1b[30m"
#define NEGRO_F		"\x1b[40m"
#define ROJO_T		"\x1b[31m"
#define ROJO_F		"\x1b[41m"
#define VERDE_T		"\x1b[32m"
#define VERDE_F 	"\x1b[42m"
#define AMARILLO_T	"\x1b[33m"
#define AMARILLO_F	"\x1b[43m"
#define AZUL_T		"\x1b[34m"
#define AZUL_F		"\x1b[44m"
#define MAGENTA_T	"\x1b[35m"
#define MAGENTA_F	"\x1b[45m"
#define CYAN_T		"\x1b[36m"
#define CYAN_F		"\x1b[46m"
#define BLANCO_T	"\x1b[37m"
#define BLANCO_F	"\x1b[47m"


/*-------------------OPERACIONES BASICAS----------------------------*/
void
calculadoraprog_basicas(char *host, double a, char operation, double b)
{
	CLIENT *clnt;
	responseBasic  *result;

	//Se define la estructura operation que contiene los operandos a y b 
	// que será el argumento de las operaciones
	struct operationBasic operands;
	operands.first = a;
	operands.second = b;


	#ifndef	DEBUG
		clnt = clnt_create (host, CALCULADORAPROG, CALCULADORAVERS, "udp");
		if (clnt == NULL) {
			clnt_pcreateerror (host);
			exit (1);
		}
	#endif	/* DEBUG */

	//Se realiza la operacion según el tipo de operando
	switch (operation)
    {
        case '+': //Suma
            result = suma_1(operands, clnt);
			if (result == (responseBasic *) NULL) {
				clnt_perror (clnt, "call failed");
			}
            break;

        case '-'://Resta
           	result = resta_1(operands, clnt);
			if (result == (responseBasic *) NULL) {
				clnt_perror (clnt, "call failed");
			}
            break;

        case '*': //Multiplicacion
            result = multiplicacion_1(operands, clnt);
			if (result == (responseBasic *) NULL) {
				clnt_perror (clnt, "call failed");
			}
            break;

        case '/':  //Division
            result = division_1(operands, clnt);
			if (result == (responseBasic *) NULL) {
				clnt_perror (clnt, "call failed");
			}
            break;
		case 'l':
			result = logaritmo_1(operands, clnt);
			if (result == (responseBasic *) NULL) {
				clnt_perror (clnt, "call failed");
			}
			break;
		case '^':
			result = potencia_1(operands, clnt);
			if (result == (responseBasic *) NULL) {
				clnt_perror (clnt, "call failed");
			}
			break;
        
        default:
            break;
    }

	//Se muestra el resultado
	printf(MAGENTA_T"%f"RESET_COLOR, result->responseBasic_u.result);
	

	//Se libera la memoria asignada por la llamada RPC
	xdr_free (xdr_responseBasic, result);

	#ifndef	DEBUG
		clnt_destroy (clnt);
	#endif	 /* DEBUG */
}

/*-------------------OPERACIONES CON VECTORES----------------------------*/
void
calculadoraprog_vectores(char *host, vectorData v1, char operation, vectorData v2)
{
	CLIENT *clnt;
	responseVectores  *result;
	responseBasic  *resultProdEscalar;

	//Se define la estructura operation que contiene los operandos v1 y v2 
	// que será el argumento de las operaciones
	struct operationVectores operands;
	operands.first = v1;
	operands.second = v2;

	#ifndef	DEBUG
		clnt = clnt_create (host, CALCULADORAPROG, CALCULADORAVERS, "udp");
		if (clnt == NULL) {
			clnt_pcreateerror (host);
			exit (1);
		}
	#endif	/* DEBUG */

	switch (operation)
    {
        case '+': //Suma
            result = sumavectores_1(operands, clnt);
			if (result == (responseVectores *) NULL) {
				clnt_perror (clnt, "call failed");
			}
            break;

        case '-'://Resta
           	result = restavectores_1(operands, clnt);
			if (result == (responseVectores *) NULL) {
				clnt_perror (clnt, "call failed");
			}
            break;
		default:
            break;
    }
	
	//Se muestra el resultado
	switch (operation)
    {
        case '+': //Suma
			for(int i = 0; i < result->responseVectores_u.vResult.vectorData_len; i++){
				printf(MAGENTA_T"%f  "RESET_COLOR, result->responseVectores_u.vResult.vectorData_val[i]);
			}
            break;

        case '-'://Resta
			for(int i = 0; i < result->responseVectores_u.vResult.vectorData_len; i++){
				printf(MAGENTA_T"%f  "RESET_COLOR, result->responseVectores_u.vResult.vectorData_val[i]);
			}
            break;
		default:
            break;
    }
		
	printf("\n");

	//Se libera la memoria asignada por la llamada RPC
	xdr_free (xdr_responseVectores, result);

	#ifndef	DEBUG
		clnt_destroy (clnt);
	#endif	 /* DEBUG */
}

/*-------------------OPERACIONES CON VECTORES 3D----------------------------*/
void
calculadoraprog_vectores3D(char *host, vector3D v1,  char operation, vector3D v2)
{
	CLIENT *clnt;
	responseVectores  *resultProdVectorial;
	responseBasic  *resultProdEscalar;


	//Se define la estructura operation que contiene los operandos v1, v2 y v3 
	// que será el argumento de las operaciones
	struct operationVectores3D operands;
	operands.first = v1;
	operands.second = v2;

	#ifndef	DEBUG
		clnt = clnt_create (host, CALCULADORAPROG, CALCULADORAVERS, "udp");
		if (clnt == NULL) {
			clnt_pcreateerror (host);
			exit (1);
		}
	#endif	/* DEBUG */

	switch (operation)
    {
        case '*': //Producto escalar
            resultProdEscalar = prodescalar3d_1(operands, clnt);
			if (resultProdEscalar == (responseBasic *) NULL) {
				clnt_perror (clnt, "call failed");
			}

            break;

        case 'x': //Producto vectorial
           	resultProdVectorial = prodvectorial3d_1(operands, clnt);
			if (resultProdVectorial == (responseVectores *) NULL) {
				clnt_perror (clnt, "call failed");
			}
            break;
		default:
            break;
    }
	
	//Se muestra el resultado y se libera la memoria asiganda por la llamada RPC
	switch (operation)
    {
        case '*': //Producto escalar
			printf(MAGENTA_T"%f"RESET_COLOR, resultProdEscalar->responseBasic_u.result);

			//Se libera la memoria asignada por la llamada RPC
			xdr_free (xdr_responseVectores, resultProdEscalar);
            break;

        case 'x': //Producto vectorial
			printf(MAGENTA_T"(%f,"RESET_COLOR, resultProdVectorial->responseVectores_u.vResult.vectorData_val[0]);
			printf(MAGENTA_T" %f,"RESET_COLOR, resultProdVectorial->responseVectores_u.vResult.vectorData_val[1]);
			printf(MAGENTA_T" %f) "RESET_COLOR, resultProdVectorial->responseVectores_u.vResult.vectorData_val[2]);
			//for(int i = 0; i < resultProdVectorial->responseVectores_u.vResult.vectorData_len; i++){
			//	printf(MAGENTA_T"%f  "RESET_COLOR, resultProdVectorial->responseVectores_u.vResult.vectorData_val[i]);
			//}
			//Se libera la memoria asignada por la llamada RPC
			xdr_free (xdr_responseVectores, resultProdVectorial);
            break;
		default:
            break;
    }

	printf("\n");

	#ifndef	DEBUG
		clnt_destroy (clnt);
	#endif	 /* DEBUG */
}

/*-------------------OPERACIONES CON MATRICES CUADRADAS----------------------------*/
void
calculadoraprog_matrices(char *host, matrizData a, char operation, matrizData b)
{
	CLIENT *clnt;
	responseMatrices  *result;

	struct operationMatrices operands;
	operands.first = a;
	operands.second = b;

	#ifndef	DEBUG
		clnt = clnt_create (host, CALCULADORAPROG, CALCULADORAVERS, "udp");
		if (clnt == NULL) {
			clnt_pcreateerror (host);
			exit (1);
		}
	#endif	/* DEBUG */

	//Se realiza la operacion según el tipo de operando
	switch (operation)
    {
        case '+': //Suma
            result = sumamatrices_1(operands, clnt);
			if (result == (responseMatrices *) NULL) {
				clnt_perror (clnt, "call failed");
			}
            break;

        case '-'://Resta
			result = restamatrices_1(operands, clnt);
			if (result == (responseMatrices *) NULL) {
				clnt_perror (clnt, "call failed");
			}
			break;

		case '*'://Producto
           	result = productomatrices_1(operands, clnt);
			if (result == (responseMatrices *) NULL) {
				clnt_perror (clnt, "call failed");
			}
            break;

        default:
            break;
    }
	
	//Se muestra el resultado
	int indice, fil, col;
	fil = result->responseMatrices_u.mResult.f;
	col = result->responseMatrices_u.mResult.c;
	switch (operation)
    {
        case '+': //Suma
			for(int i = 0; i < fil; i++){
				printf("\n\t");
				for(int j = 0; j < col; j++){
					indice = (i*col)+j;
					printf(MAGENTA_T"%f  "RESET_COLOR, result->responseMatrices_u.mResult.m.vectorData_val[indice]);
				}
			}
			printf("\n");
            break;

        case '-'://Resta
			for(int i = 0; i < fil; i++){
				printf("\n\t");
				for(int j = 0; j < col; j++){
					indice = (i*col)+j;
					printf(MAGENTA_T"%f  "RESET_COLOR, result->responseMatrices_u.mResult.m.vectorData_val[indice]);
				}
			}
			printf("\n");
            break;
		case '*': //PRODUCTO
			for(int i = 0; i < fil; i++){
				printf("\n\t");
				for(int j = 0; j < col; j++){
					indice = (i*col)+j;
					printf(MAGENTA_T"%f  "RESET_COLOR, result->responseMatrices_u.mResult.m.vectorData_val[indice]);
				}
			}
			printf("\n");
            break;
		default:
            break;
    }

	//Se libera la memoria asignada por la llamada RPC
	xdr_free (xdr_responseMatrices, result);
	
	
	#ifndef	DEBUG
		clnt_destroy (clnt);
	#endif	 /* DEBUG */
}


#define MAXIMA_LONGITUD_PETICION 50

int
main (int argc, char *argv[])
{
	if (argc  != 2) {
		printf ("usage: %s server_host\n", argv[0]);
		exit (1);
	}

	//Declaracion de variables
	char *host;
	host = argv[1];

	char peticion[MAXIMA_LONGITUD_PETICION];
	char operation;
	
	//Operaciones basicas
	double a, b;

	//Operaciones con Vectores
	vectorData v1, v2;

	//Operaciones con Vectores3D
	vector3D v3D1, v3D2;

	//Operaciones con Matrices Cuadradas
	matrizData m1, m2;
	int tam;
	
	//-------------------MENU---------------

	int peticionValida=1;

	int subMenu=0;

	int menuPrincipal=0;
	while( menuPrincipal != 5){
		printf("\nOpciones disponibles:\n");
		printf("  1: Operaciones Básicas\n");
		printf("  2: Operaciones con vectores\n");
		printf("  3: Operaciones con vectores 3D\n");
		printf("  4: Operaciones con matrices cuadradas\n");
		printf("  5: Salir \n");

		printf("\n--Introducce una opción: ");
		scanf("%d",&menuPrincipal);

		switch(menuPrincipal){
			case 1: //Menu Operaciones basicas
				printf("\n\t----OPERACIÓN BÁSICA----\n");

				//Obtener los operandos y la operacion
				printf("\tIntroduce la operacion  +  -  *  /  log  ^ : ");
				scanf("%s",peticion);

				if( strcmp(peticion, "+") == 0 ){
					operation='+';
					peticionValida=1;
				}
				else if(strcmp(peticion, "-") == 0){
					operation='-';
					peticionValida=1;
				}
				else if(strcmp(peticion, "*") == 0){
					operation='*';
					peticionValida=1;
				}
				else if(strcmp(peticion, "/") == 0){
					operation='/';
					peticionValida=1;
				}
				else if(strcmp(peticion, "log") == 0){
					operation='l';
					peticionValida=1;
				}
				else if(strcmp(peticion, "^") == 0){
					operation='^';
					peticionValida=1;
				}
				else{
					peticionValida=0;
				}

				//Si la peticon es valida entonces se obtienen los operandos
				if(peticionValida){

					if( operation != 'l' && operation != '^'){
						printf("\tIntroduce el primer operando: ");
						scanf("%lf",&a);

						printf("\tIntroduce el segundo operando: ");
						scanf("%lf",&b);
					}
					else if(operation == 'l')  {
						printf("\tIntroduce el argumento: ");
						scanf("%lf",&a);

						printf("\tIntroduce la base: ");
						scanf("%lf",&b);
					}
					else if(operation == '^')  {
						printf("\tIntroduce la base: ");
						scanf("%lf",&a);

						printf("\tIntroduce el exponente: ");
						scanf("%lf",&b);
					}

					printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
					printf( AZUL_T"  El resultado de la operación "AMARILLO_T"%f" AZUL_T" %s "AMARILLO_T"%f"AZUL_T" = "RESET_COLOR, a , peticion, b);
					
					//Realizar peticion al servidor y Mostrar resultado
					calculadoraprog_basicas (host,a,operation,b);
					printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");


				}
				else{
					printf("\tNO ES CORRECTA LA PETICION\n");
				}	

				break;
				
			case 2: //Menu vectores
				printf("\n\t----OPERACIÓN CON VECTORES----\n");

				//Obtener los operandos y la operacion
				printf("\tIntroduce el tamaño de los vectores: ");
				int tam;
				do{
					scanf("%d",&tam);
				}while(tam <= 0);

				printf("\tContenido del primer vector (v1):\n");
				v1.vectorData_len = tam;
				v1.vectorData_val = malloc(tam*sizeof(double));

				for(int i = 0; i < v1.vectorData_len; i++){
					printf("\tv1[%d]: ",i);
					scanf("%lf",&v1.vectorData_val[i]);
				}

				printf("\tContenido del segundo vector (v2):\n");
				v2.vectorData_len = tam;
				v2.vectorData_val = malloc(tam*sizeof(double));
				
				for(int i = 0; i < v2.vectorData_len; i++){
					printf("\tv2[%d]: ",i);
					scanf("%lf",&v2.vectorData_val[i]);
				}

				subMenu = 0;
				while( subMenu != 3){
					printf("\n\tOpciones disponibles con v1 y v2:\n");
					printf("\t    1: Suma\n");
					printf("\t    2: Resta\n");
					printf("\t    3: Salir\n");
					printf("\n\t    --Introducce una opción: ");
					scanf("%d",&subMenu);

					switch(subMenu){
						case 1: //suma

							operation='+';

							//Resultado
							printf(AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							printf( ""AZUL_T" El resultado de la operación:\n\t"AMARILLO_T);
							for(int i = 0; i < v1.vectorData_len; i++){
								printf("%lf ",v1.vectorData_val[i]);
							}
							printf(AZUL_T"\n\t\t%c\n\t"AMARILLO_T,operation);
							for(int i = 0; i < v2.vectorData_len; i++){
								printf("%lf ",v2.vectorData_val[i]);
							}
							printf( "\n\t"AZUL_T"= "RESET_COLOR);
										
							//Realizar peticion al servidor y Mostrar resultado
							calculadoraprog_vectores(host, v1, operation, v2);

							printf(AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							break;
							
						case 2: //Resta	

							operation='-';

							//Resultado
							printf(AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							printf( ""AZUL_T" El resultado de la operación:\n\t"AMARILLO_T);
							for(int i = 0; i < v1.vectorData_len; i++){
								printf("%lf ",v1.vectorData_val[i]);
							}
							printf(AZUL_T"\n\t\t%c\n\t"AMARILLO_T,operation);
							for(int i = 0; i < v2.vectorData_len; i++){
								printf("%lf ",v2.vectorData_val[i]);
							}
							printf( "\n\t"AZUL_T"= "RESET_COLOR);
										
							//Realizar peticion al servidor y Mostrar resultado
							calculadoraprog_vectores(host, v1, operation, v2);

							printf(AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							break;

						case 3: //Salir
							// Exit
							break;
						default:
							printf("\t    ERROR: Opcion no valida\n");
					}
				}
			break;

			case 3: //Menu Vectores3D
				printf("\n\t----OPERACIÓN CON VECTORES 3D----\n");

				//Obtener los operandos y la operacion

				printf("\tContenido del primer vector (v1):\n");
				printf("\t      x:");
				scanf("%lf",&v3D1.x);
				printf("\t      y:");
				scanf("%lf",&v3D1.y);
				printf("\t      z:");
				scanf("%lf",&v3D1.z);
				
				printf("\tContenido del segundo vector (v2):\n");
				printf("\t      x:");
				scanf("%lf",&v3D2.x);
				printf("\t      y:");
				scanf("%lf",&v3D2.y);
				printf("\t      z:");
				scanf("%lf",&v3D2.z);

				subMenu = 0;
				while( subMenu != 3){
					printf("\n\t  Opciones disponibles con v1 y v2:\n");
					printf("\t    1: Producto escalar\n");
					printf("\t    2: Producto vectorial\n");
					printf("\t    3: Salir\n");
					printf("\n\t    --Introducce una opción: ");
					scanf("%d",&subMenu);

					switch(subMenu){
						case 1: //Producto escalar

						operation = '*';
						
						//Resultado
						printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
						printf(AZUL_T" El resultado de la operación "AMARILLO_T);

						printf("\n\t(%f, %f, %f,)"AZUL_T" · "AMARILLO_T" (%f, %f, %f,)",v3D1.x,v3D1.y,v3D1.z,v3D2.x,v3D2.y,v3D2.z);
						printf( AZUL_T" = "RESET_COLOR);
						
						//Realizar peticion al servidor y Mostrar resultado
						calculadoraprog_vectores3D(host, v3D1, operation, v3D2);
						printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							break;
							
						case 2: //Producto vectorial
							
							operation = 'x';

							//Resultado
							printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							printf(AZUL_T" El resultado de la operación "AMARILLO_T);

							printf("\n\t(%f, %f, %f,)"AZUL_T" %c "AMARILLO_T"(%f, %f, %f,)",v3D1.x,v3D1.y,v3D1.z,operation,v3D2.x,v3D2.y,v3D2.z);
							printf( AZUL_T" = "RESET_COLOR);
							
							//Realizar peticion al servidor y Mostrar resultado
							calculadoraprog_vectores3D(host, v3D1, operation, v3D2);
							printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							break;

						case 3: //Salir
							// Exit
							break;

						default:
							printf("\t    ERROR: Opcion no valida\n");
					}

				}				
			break;

			case 4: //Menu Matrices
			printf("\n\t----OPERACIONES CON MATRICES CUADRADAS----\n");

				//Obtener los operandos y la operacion

				printf("\tIntroduce el numero de filas y columnas: ");
				
				do{
					scanf("%d",&tam);
				}while(tam <= 0);

				printf("\tContenido de la primera matriz (m1):\n");
				m1.c=tam;
				m1.f=tam;
				m1.m.vectorData_len=tam*tam;
				m1.m.vectorData_val=malloc(tam*tam*sizeof(double));
				
				for(int i = 0; i < tam; i++){
					//printf("\tFila %d: ",i);
					for(int j = 0; j < tam; j++){
						printf("\t\tm1[%d][%d]: ",i,j);
						scanf("%lf",&m1.m.vectorData_val[(i*m1.c) + j]);
					}
				}

				printf("\tContenido de la segunda matriz (m2):\n");
				m2.c=tam;
				m2.f=tam;
				m2.m.vectorData_len=tam*tam;
				m2.m.vectorData_val=malloc(tam*tam*sizeof(double));
				
				for(int i = 0; i < tam; i++){
					//printf("\tFila %d: ",i);
					for(int j = 0; j < tam; j++){
						printf("\t\tm2[%d][%d]: ",i,j);
						scanf("%lf",&m2.m.vectorData_val[ (i*m1.c) + j]);
					}
				}
			
				subMenu = 0;
				while( subMenu != 4){
					printf("\n\t  Opciones disponibles con m1 y m2:\n");
					printf("\t    1: Suma de matrices\n");
					printf("\t    2: Resta de matrices\n");
					printf("\t    3: Producto de matrices\n");
					printf("\t    4: Salir\n");
					printf("\n\t    --Introducce una opción: ");
					scanf("%d",&subMenu);

					switch(subMenu){
						case 1: //Suma

							operation = '+';
							//Resultado
							printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							printf( ""AZUL_T" El resultado de la operación:\n\t"AMARILLO_T);

							for(int i = 0; i < m1.f; i++){
								printf("\n\t");
								for(int j = 0; j < m1.c; j++){
									printf("%f ", m1.m.vectorData_val[(i*m1.c)+j]);
								}
							}

							printf(AZUL_T"\n\t\t%c"AMARILLO_T,operation);

							for(int i = 0; i < m2.f; i++){
								printf("\n\t");
								for(int j = 0; j < m2.c; j++){
									printf("%f ", m2.m.vectorData_val[(i*m2.c)+j]);
								}
							}

							printf( "\n\t"AZUL_T"= "RESET_COLOR);
										
							//Realizar peticion al servidor y Mostrar resultado
							calculadoraprog_matrices(host, m1, operation, m2);
							printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							break;
							
						case 2: //Resta
							
							operation = '-';

							//Resultado
							printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							printf( ""AZUL_T" El resultado de la operación:\n\t"AMARILLO_T);

							for(int i = 0; i < m1.f; i++){
								printf("\n\t");
								for(int j = 0; j < m1.c; j++){
									printf("%f ", m1.m.vectorData_val[(i*m1.c)+j]);
								}
							}

							printf(AZUL_T"\n\t\t%c"AMARILLO_T,operation);

							for(int i = 0; i < m2.f; i++){
								printf("\n\t");
								for(int j = 0; j < m2.c; j++){
									printf("%f ", m2.m.vectorData_val[(i*m2.c)+j]);
								}
							}

							printf( "\n\t"AZUL_T"= "RESET_COLOR);
										
							//Realizar peticion al servidor y Mostrar resultado
							calculadoraprog_matrices(host, m1, operation, m2);
							printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							break;

						case 3: //Producto
							operation = '*';

							//Resultado
							printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							printf( ""AZUL_T" El resultado de la operación:\n\t"AMARILLO_T);

							for(int i = 0; i < m1.f; i++){
								printf("\n\t");
								for(int j = 0; j < m1.c; j++){
									printf("%f ", m1.m.vectorData_val[(i*m1.c)+j]);
								}
							}

							printf(AZUL_T"\n\t\t%c"AMARILLO_T,operation);

							for(int i = 0; i < m2.f; i++){
								printf("\n\t");
								for(int j = 0; j < m2.c; j++){
									printf("%f ", m2.m.vectorData_val[(i*m2.c)+j]);
								}
							}

							printf( "\n\t"AZUL_T"= "RESET_COLOR);
										
							//Realizar peticion al servidor y Mostrar resultado
							calculadoraprog_matrices(host, m1, operation, m2);
							printf("\n"AZUL_T"--------------------------------------------------------------------"RESET_COLOR"\n");
							break;

						case 4: //Salir
							// Exit
							break;

						default:
							printf("\t    ERROR: Opcion no valida\n");
					}
				}

				break;

			case 5:
				printf("Saliendo...\n");
				// Exit
				break;

			default:
				printf("ERROR: Opcion no valida\n");
		}
	}
	
exit (0);
}