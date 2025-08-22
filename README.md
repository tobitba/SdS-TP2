# Simulación de Sistemas TP2

## 👋 Introducción

Trabajo práctico para la materia de Simulación de Sistemas en el ITBA. Se buscó implementar un simulador de movimiento de partículas siguiendo el modelo Autómata Off-Lattice para partículas propuesto por Vicsek y Co.

### ❗ Requisitos

- Java 21+
- Maven

## 🏃 Ejecución
Primero se requiere compilar el paquete
```shell
mvn clean package
```

Finalmente se puede correr el programa por consola con el siguiente comando:
```bash
java {args} -cp {JAR_FILE} {MAIN_CLASS}
```
Con:
- `JAR_FILE` = target/SdS-TP2-1.0-SNAPSHOT.jar
- `MAIN_CLASS` = OffLatticeSimulation
- `args` obligatorios listados en la siguiente sección

### 🛠️ Argumentos
Donde los argumentos son los siguientes:
- `N`: Cantidad de particulas
- `L`: Tamaño del dominio
- `V`: Velocidad de las partículas en la simulación
- `rc`: Radio de búsqueda de vecinos
- `epoch`: Cantidad de épocas de la simulación
- `noise`: Ruido con el que se moverá el ángulo de las partículas
- `rand-dir`: true si se desea tomar una dirección random de los vecinos, false si se desea hacer un promedio de las direcciones vecinas
- `output`: Nombre opcional para el archivo de salida de la simulación

Ejemplo:
```shell
 java -DN=1000 -DL=20 "-DV=0.3" -Drc=1 -Depoch=1000 -Dnoise=0 -Drand-dir=false -cp target/SdS-TP2-1.0-SNAPSHOT.jar OffLatticeSimulation
```
Nota: Los argumentos con punto pueden requerir estar en comillas dobles, como el caso de la velocidad.

## 🔎 Animación y Análisis
Las animaciones y análisis se realizaron en este [colab](https://colab.research.google.com/drive/1JuF0k8cnBpoqezo86U3zJO3Gzo28ERoR?authuser=2#scrollTo=LbGdq9SN06eL).
