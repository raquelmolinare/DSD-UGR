/* -------------------------------------------------------------------------
 * Desarrollo de sistemas distribuidos 2020-2021
 * Páctica 2: Apache Thrift
 * Autor: Raquel Molina Reche
 *
 * Compilar el fichero al lenguaje a utilizar Ejemplo para python: 
 *     $ thrift -gen py calculadora.thrift
 * -------------------------------------------------------------------------
 */


//---------Estructura que compone un vector 3D-----------
struct  Vector3D{
   1:  required  double  x;   //Componente x 3D
   2:  required  double  y;   //Componente y 3D
   3:  required  double  z;   //Componente z 3D
}


//---------Estructura que compone una matriz-----------
struct  Matriz{
   1:  required  i32  f;   //filas
   2:  required  i32  c;   //columnas
   3:  required  list<double> m; //lista que contiene los elementos de la matriz
}

service Calculadora{
   void ping(),
   i32 suma(1:i32 num1, 2:i32 num2),
   i32 resta(1:i32 num1, 2:i32 num2),
   double multiplicacion(1:double num1, 2:double num2),
   double division(1:double num1, 2:double num2),
   double logaritmo(1:double num1, 2:double num2),
   double potencia(1:double num1, 2:i32 num2),
   list<double> sumavectores(1:list<double> v1, 2:list<double> v2),
   list<double> restavectores(1:list<double> v1, 2:list<double> v2),
   double productoescalar3d(1:Vector3D v3D1, 2:Vector3D v3D2),
   Vector3D productovectorial3d(1:Vector3D v3D1, 2:Vector3D v3D2),
   Matriz sumamatrices(1:Matriz m1, 2:Matriz m2),
   Matriz restamatrices(1:Matriz m1, 2:Matriz m2),
   Matriz productomatrices(1:Matriz m1, 2:Matriz m2),
}
