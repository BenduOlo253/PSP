# Programa 1 PSP0.1: Analizador LOC para código fuente Java

## 1. Descripción del programa

Este proyecto implementa una herramienta de consola en Java para medir métricas
PSP0.1 sobre uno o varios archivos fuente `.java`. Para cada archivo recibido en
la línea de comandos, el programa reporta:

- LOC lógicas totales del programa.
- Total de líneas físicas del archivo fuente.
- Número total de clases detectadas.
- Nombre de cada clase.
- Tamaño LOC de cada clase.
- Número de métodos y constructores por clase.
- Variables declaradas e inicializadas en la misma línea física.

La herramienta se ejecuta desde la carpeta `Programa2/PSP1` con comandos como:

```bash
javac *.java
java App ProgramaPrueba1.java ProgramaPrueba2.java
```

## 2. Uso de PSP0.1

PSP0.1 requiere registrar datos de tamaño para mejorar la planeación y el control
personal del desarrollo. En este programa, el tamaño se mide con LOC lógicas bajo
un estándar explícito. Las métricas por clase ayudan a separar el tamaño total del
producto en componentes que pueden compararse, revisarse y reutilizarse en
estimaciones posteriores.

## 3. Estándar de conteo aplicado

Se aplicaron las siguientes reglas de conteo:

1. Una línea física cuenta como una LOC lógica cuando contiene código funcional.
2. Se cuentan `import`, declaraciones de clase, declaraciones de método,
   constructores, variables, asignaciones, llamadas, estructuras de control,
   `try`, `catch`, `break`, `continue` y `return`.
3. No se cuentan comentarios de línea, comentarios de bloque, JavaDoc, banners,
   líneas en blanco ni llaves solas.
4. Una llave de apertura cuenta solo si está en la misma línea de una declaración
   o instrucción funcional.
5. Una llave de cierre sola no cuenta como LOC.
6. Si una línea mezcla código y comentario, solo se evalúa el código.
7. Si una línea contiene varias declaraciones de variables, cada variable cuenta
   como una LOC lógica. Por ejemplo, `int x = 1, y = 2;` cuenta como 2 LOC.
8. Los marcadores `//`, `/*` y `*/` dentro de cadenas o caracteres no se tratan
   como comentarios.

## 4. Estándar de codificación aplicado

El código nuevo y modificado sigue estas reglas:

- Archivos Java con encabezado JavaDoc inicial.
- Clases y métodos públicos documentados con JavaDoc.
- Clases en UpperCamelCase.
- Métodos y variables en lowerCamelCase.
- Constantes en SCREAMING_SNAKE_CASE.
- Indentación de 4 espacios.
- Estilo K&R para llaves.
- Nombres descriptivos.
- Uso exclusivo de Java estándar, sin librerías externas.
- Constantes para evitar números mágicos en formato de salida y reglas léxicas.

## 5. Adaptación del código existente

Se revisó la estructura del repositorio antes de modificar. En el estado recibido
no existía la carpeta `Programa2/PSP1`; la única implementación Java previa estaba
en `Programa1/PSP1`. Esa implementación corresponde a otro ejercicio PSP: lectura
de puntos, escritura de archivos y cálculo de media/desviación estándar. Sus
clases (`App`, `LectorArchivo`, `EscritorArchivos`, `Calculadora` y `Punto`) no
implementaban análisis LOC ni detección de clases, métodos o variables.

Por esa razón se creó la carpeta `Programa2/PSP1` y se ubicó ahí el Programa 1
solicitado. Se conservó la idea útil de separar responsabilidades de lectura y
procesamiento: el acceso a archivos quedó en `ProcesadorArchivo`, mientras que el
análisis LOC quedó separado en clases especializadas. No se modificó el programa
existente de `Programa1/PSP1` porque cambiarlo habría roto su propósito original.

## 6. Estructura final del proyecto

| Archivo | Responsabilidad |
| --- | --- |
| `App.java` | Punto de entrada ejecutable con `java App`; delega en `AnalizadorLOC`. |
| `AnalizadorLOC.java` | Orquestador del análisis; valida argumentos, coordina el análisis e imprime resultados. |
| `ProcesadorArchivo.java` | Lee archivos fuente y cuenta líneas físicas. |
| `LimpiadorCodigo.java` | Elimina comentarios sin alterar cadenas ni caracteres. |
| `ContadorLOC.java` | Aplica el estándar de conteo LOC total y por rango. |
| `AnalizadorClases.java` | Detecta clases, línea inicial, línea final y métodos asociados. |
| `AnalizadorMetodos.java` | Detecta métodos y constructores evitando estructuras de control. |
| `AnalizadorVariables.java` | Detecta variables declaradas e inicializadas en la misma línea. |
| `ResultadoPrograma.java` | Modelo de métricas generales por archivo analizado. |
| `ResultadoClase.java` | Modelo de métricas por clase. |
| `ProgramaPrueba1.java` | Caso de prueba con dos clases, comentarios y declaración múltiple. |
| `ProgramaPrueba2.java` | Caso de prueba con imports, constructor y estructuras de control. |
| `ReportePrograma1.md` | Reporte técnico académico del programa. |

## 7. Instrucciones de compilación

Desde la carpeta `Programa2/PSP1`:

```bash
javac *.java
```

## 8. Instrucciones de ejecución

Desde la carpeta `Programa2/PSP1`:

```bash
java App ProgramaPrueba1.java ProgramaPrueba2.java
```

También se pueden analizar otros archivos:

```bash
java App Archivo1.java Archivo2.java Archivo3.java
```

## 9. Casos de prueba

### 9.1 ProgramaPrueba1.java

Este archivo valida:

- Mínimo dos clases.
- JavaDoc.
- Comentarios de bloque.
- Líneas en blanco.
- Métodos.
- Constructor.
- Variables declaradas e inicializadas.
- Declaración múltiple `int x = 1, y = 2;`.
- Texto `//` dentro de una cadena que no debe interpretarse como comentario.

### 9.2 ProgramaPrueba2.java

Este archivo valida:

- Imports.
- Una clase principal.
- Constructor.
- `for`.
- `if`.
- `try/catch`.
- `break` y `continue`.
- Variables inicializadas.
- Asignaciones normales que no deben marcarse como declaraciones.

## 10. Resultados obtenidos

Salida obtenida al ejecutar los casos de prueba incluidos:

```text
Programa 1: ProgramaPrueba1.java
-------------------------------------------------------------------------------------------------------
Número de Programa | Nombre de la clase     | Número de métodos   | Tamaño de la clase   | Tamaño total
-------------------------------------------------------------------------------------------------------
1                  | ProgramaPrueba1        | 2                   | 10                   |
1                  | AuxiliarPrueba1        | 2                   | 6                    | 16
-------------------------------------------------------------------------------------------------------
Total de líneas físicas del archivo: 39
Total de LOC lógicas contadas: 16
Total de clases: 2
Variables declaradas e inicializadas:
Variable contador declarada e inicializada en la misma línea
Variable x declarada e inicializada en la misma línea
Variable y declarada e inicializada en la misma línea
Variable nombre declarada e inicializada en la misma línea
Variable etiqueta declarada e inicializada en la misma línea

Programa 2: ProgramaPrueba2.java
-------------------------------------------------------------------------------------------------------
Número de Programa | Nombre de la clase     | Número de métodos   | Tamaño de la clase   | Tamaño total
-------------------------------------------------------------------------------------------------------
2                  | ProgramaPrueba2        | 2                   | 14                   | 16
-------------------------------------------------------------------------------------------------------
Total de líneas físicas del archivo: 37
Total de LOC lógicas contadas: 16
Total de clases: 1
Variables declaradas e inicializadas:
Variable datos declarada e inicializada en la misma línea
Variable valor declarada e inicializada en la misma línea
```

## 11. Decisiones de diseño tomadas

1. Se usó análisis léxico básico en lugar de un parser completo para cumplir la
   restricción de usar solo Java estándar y mantener el programa apropiado para
   PSP0.1.
2. El limpiador de comentarios conserva la cantidad de líneas para que los rangos
   de clases sigan coincidiendo con las líneas físicas originales.
3. Los constructores se cuentan como métodos porque el enunciado lo solicita para
   el reporte.
4. Las clases se detectan por patrón y su cierre se determina por balanceo de
   llaves, ignorando llaves dentro de cadenas o caracteres.
5. Las variables inicializadas se detectan solo cuando la línea tiene forma de
   declaración; asignaciones como `edad = 20;`, `this.edad = edad;` y
   `contador += 1;` no se reportan.
6. Las líneas con varias variables declaradas se separan respetando paréntesis,
   genéricos, arreglos, llaves, cadenas y caracteres para evitar dividir comas de
   expresiones internas.

## 12. Justificación de refactorización

Se agregó `App.java` como main visible del programa para facilitar la ejecución
con `java App`, manteniendo `AnalizadorLOC` como orquestador reutilizable. La
solución se dividió en clases pequeñas para mejorar claridad, mantenibilidad y
exactitud:

- `LimpiadorCodigo` evita mezclar limpieza de comentarios con conteo LOC.
- `ContadorLOC` centraliza las reglas del estándar de conteo.
- `AnalizadorClases`, `AnalizadorMetodos` y `AnalizadorVariables` separan tres
  responsabilidades distintas del análisis fuente.
- `ResultadoPrograma` y `ResultadoClase` desacoplan el cálculo de la impresión.

La refactorización fue necesaria porque el código Java existente en `Programa1`
resuelve otro problema y no contenía una arquitectura reutilizable directamente
para métricas LOC.

## 13. Limitaciones conocidas

1. El programa realiza análisis léxico básico; no construye un árbol sintáctico de
   Java.
2. Declaraciones de métodos partidas en varias líneas pueden no detectarse como
   una única declaración.
3. Clases anónimas y lambdas cuentan como código funcional, pero no se reportan
   como clases o métodos nombrados.
4. Sintaxis Java muy avanzada puede requerir ajustes en las expresiones regulares.
5. Cuando una regla es ambigua, se prioriza el estándar del enunciado: línea física
   funcional equivale a línea lógica, salvo declaraciones múltiples de variables.
