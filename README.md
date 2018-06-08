# CABS: compilador a ABS

CABS es un lenguaje con una sintaxis similar a C que pretende acercar el uso de algunas herramientas implementadas para ABS, un lenguaje de modelado, al lenguaje C.

## Contenido

Este repositorio contiene el código del compilador desarrollado para mi TFG. Se acompaña de una carpeta con ejemplos con extensión ``.cabs`` y sus compilados en extensión ``.abs``.

## Instrucciones

Para hacer uso del compilador es necesario compilar el proyecto usando un compilador de Java 1.8. La clase que contiene el método ``main`` es ``Manager``. Los parámetros que se han de pasar al programa son el path del archivo de origen seguido del path del archivo de destino de la compilación separados por un espacio.

Los archivos ``com/raincoatmoon/Parser/Jlex.java`` y ``java-cup-11b-runtime.jar`` solo son necesarios si se desea modificar la sintaxis del lenguaje. Contienen el código de JLex y de CUP, librerías usadas para crear el lexer y el parser de CABS. Los archivos ``com/raincoatmoon/Parser/parser.lex`` y ``com/raincoatmoon/Parser/synt.cup`` contienen la declaración del léxico y de la gramática de CABS, siendo las clases ``Yylex.java`` y ``parser.java`` las generadas por dichas librerías.

## Referencias
Para el desarrollo de este compilador tomé como referencia un proyecto anterior desarrollado para la asignatura de Procesadores de Lenguaje de la Facultad de Informática de la UCM. El lenguaje original, CeMAGRplus puede encontrarse en el repositorio homónimo de esta cuenta de GitHub bajo licencia MIT. La similitud que hay entre uno y otro se reduce a la estructura del proyecto y al nombre de las clases. El código de traducción, así como la sintaxis y el target language son completamente distintos.

## Licencia
Este proyecto en su totalidad, incluyendo los ejemplos, están compartidos bajo licencia MIT.
