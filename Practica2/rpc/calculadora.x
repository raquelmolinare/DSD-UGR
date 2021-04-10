/* Archivo calculadora.x: Realización de operaciones aritméticas de forma remota */

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
 
typedef double vectorData<>;

union responseVectores switch (int error){
    case 0:
       vectorData v;
    default:
        void;
};

program CALCULADORAPROG {

    version CALCULADORAVERS {
        responseBasic SUMA (operationBasic) = 1;
        responseBasic RESTA (operationBasic) = 2;
        responseBasic MULTIPLICACION (operationBasic) = 3;
        responseBasic DIVISION (operationBasic) = 4;
        responseVectores SUMAVECTORES(vectorData v1, vectorData v2) = 5;
    } = 1;

} = 0x20000001;
