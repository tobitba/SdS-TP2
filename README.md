# Simulación de Sistemas TP2

## 👋 Introducción

Trabajo práctico para la materia de Simulación de Sistemas en el ITBA. Se buscó implementar un simulador de movimiento de partículas siguiendo el modelo Autómata Off-Lattice para partículas propuesto por Vicsek y Co.

### ❗ Requisitos

- Java 21+
- Maven

## 🏃 Ejecución

Se puede correr el programa por consola con el siguiente comando:
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

## 🔎 Animación y Análisis
Las animaciones y análisis se realizaron en este [colab](https://colab.research.google.com/drive/1JuF0k8cnBpoqezo86U3zJO3Gzo28ERoR?authuser=2#scrollTo=LbGdq9SN06eL).
