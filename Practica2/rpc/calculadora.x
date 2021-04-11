/* Archivo calculadora.x: Realización de operaciones aritméticas de forma remota */

/*-------Estructuras de datos para las operaciones básicas----------*/
struct operationBasic{
    double first;
    double second;
};

union responseBasic switch (int error){
    case 0:
        double result;
    default:
        void;
};

/*-------Estructuras de datos para las operaciones con vectores----------*/
typedef double vectorData<>;

struct operationVectores{
    vectorData first;
    vectorData second;
};

union responseVectores switch (int error){
    case 0:
       vectorData vResult;
    default:
        void;
};

struct vector3D{
    double x;
    double y;
    double z;
};

typedef struct vector3D vector3D;

struct operationVectores3D{
    vector3D first;
    vector3D second;
};


/*-------Estructuras de datos para las operaciones con matrices----------*/
typedef struct matriz matrizData;

struct matriz{
    int f;
    int c;
    vectorData m;
};

struct operationMatrices{
    matrizData first;
    matrizData second;
};

union responseMatrices switch (int error){
    case 0:
       matrizData mResult;
    default:
        void;
};

program CALCULADORAPROG {

    version CALCULADORAVERS {
        responseBasic SUMA (operationBasic) = 1;
        responseBasic RESTA (operationBasic) = 2;
        responseBasic MULTIPLICACION (operationBasic) = 3;
        responseBasic DIVISION (operationBasic) = 4;
        responseBasic LOGARITMO (operationBasic) = 5;
        responseBasic POTENCIA (operationBasic) = 6;
        responseVectores SUMAVECTORES(operationVectores) = 7;
        responseVectores RESTAVECTORES(operationVectores) = 8;
        responseBasic PRODESCALAR3D(operationVectores3D) = 9;
        responseVectores PRODVECTORIAL3D(operationVectores3D) = 10;
        responseMatrices SUMAMATRICES(operationMatrices) = 11;
    } = 1;

} = 0x20000001;
