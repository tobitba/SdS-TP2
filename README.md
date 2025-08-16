# Simulación de Sistemas TP2

## 👋 Introducción

Trabajo práctico para la materia de Simulación de Sistemas en el ITBA. Se buscó implementar un simulador de movimiento de partículas siguiendo el modelo Autómata Off-Lattice para partículas propuesto por Vicsek y Co.

### ❗ Requisitos

- Java 21+
- Maven

## 🏃 Ejecución

// _TODO_: Modificar la parte de ejecución

Se puede correr el programa por consola con el siguiente comando:
```bash
./run.sh  -Dargumento=valor
```

### 🛠️ Argumentos
Donde los argumentos posibles son los siguientes:
- `generate`: Si se desea generar o no aleatoriamente las particulas setear en true
- `static`: Ruta al archivo estatico (en caso de no generar)
- `dynamic`: Ruta al archivo dinamico (en caso de no generar)
- `N`: Cantidad de particulas
- `L`: Tamaño del dominio
- `M`: Cantidad de celdas
- `ID`: Particula a resaltar en grafico
- `r`: Radio de las particulas generadas. Si fixed=false las particulas tendran radios aleatorios entre [0,r)
- `rc`: Radio de busqueda de vecinos
- `bound-periodicity`: Setear en true si se quiere usar condiciones periodicas de contorno
- `fixed`: Para usar el mismo r para todas las particulas aleatoriamente generadas setear en true
- `graph`: Para mostrar el grafico setear en true
- `save`: Para guardar el grafico setear en true
- `show-ids`: mostrar ids en gráfico