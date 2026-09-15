# Analisis y mejoras del proyecto

## 1. Resumen

El proyecto funciona como un juego sencillo de Missile Command y tiene una estructura comprensible. Las mejoras principales se concentran en `MissileComand`, que reúne demasiadas responsabilidades, y en la duplicacion de logica entre `Misil` y `PlayerMisil`.

No se detecta el uso de patrones de diseno complejos. Por lo tanto, la complejidad observada no proviene de patrones como Factory, Observer o Strategy, sino de algunas responsabilidades agrupadas, abstracciones pequenas y elementos sin uso.

## 2. God Class y Long Class

### Problema

`MissileComand` concentra varias responsabilidades:

- Construccion de la pantalla.
- Bucle principal del juego.
- Generacion de misiles.
- Actualizacion de objetos.
- Deteccion de colisiones.
- Creacion y limpieza de explosiones.
- Dano a las ciudades.
- Control del puntaje.
- Reproduccion de sonidos.
- Reinicio de la partida.
- Dialogos de Game Over y navegacion al menu.

Por esta razon puede considerarse una God Class y tambien una Long Class.

### Mejora propuesta

Extraer responsabilidades en clases o metodos separados:

```text
MissileComand
├── GameController       -> coordina el ciclo del juego
├── CollisionManager     -> gestiona las colisiones
├── GameRenderer         -> dibuja los objetos
├── AudioManager         -> carga y reproduce sonidos
├── ScoreManager         -> ya existe y gestiona puntajes
└── City / Misil / Explosion
```

La extraccion debe hacerse de forma gradual para no alterar el comportamiento del juego.

## 3. Long Method

### Problema

El metodo `actionPerformed(...)` de `MissileComand` tiene aproximadamente 90 lineas y realiza demasiadas tareas en una sola operacion:

- Calcula el tiempo transcurrido.
- Genera misiles enemigos.
- Mueve todos los objetos.
- Detecta colisiones entre misiles.
- Crea explosiones.
- Procesa impactos de explosiones.
- Destroza ciudades.
- Elimina objetos terminados.
- Comprueba el fin de la partida.

### Mejora propuesta

Convertirlo en un metodo coordinador pequeno:

```java
public void actionPerformed(ActionEvent evento) {
    long delta = calcularDelta();
    generarMisiles();
    actualizarObjetos(delta);
    procesarColisiones();
    procesarImpactosEnCiudades();
    limpiarObjetosTerminados();
    repaint();
    verificarFinDelJuego();
}
```

Los metodos nuevos deberian ser privados y tener una sola responsabilidad.

## 4. Duplicacion de codigo

### Problema

`Misil` y `PlayerMisil` repiten logica en:

- El calculo de la distancia inicial.
- El calculo de `dx` y `dy`.
- El metodo `update(long deltaMs)`.
- La comprobacion de llegada al objetivo.
- La implementacion de `IUpdatable` e `IReachTarget`.

La diferencia entre ambas clases esta principalmente en la velocidad, el dibujo y el limite de llegada.

### Mejora propuesta

Mover la logica comun a `GameObjectTarget` o a una nueva clase base para proyectiles. Esa clase podria encargarse de:

- Guardar posicion y objetivo.
- Calcular la velocidad en cada eje.
- Actualizar la posicion.
- Exponer la posicion y el objetivo mediante getters.

Cada misil conservaria solamente su comportamiento especifico, como color, forma, velocidad y tolerancia de llegada.

## 5. Ruptura de encapsulamiento

### Problemas

En `GameObjectTarget` los campos son `protected`:

```java
protected double x, y;
protected int targetX, targetY;
```

Esto permite que las subclases modifiquen directamente el estado interno.

En `MissileComand`, las listas tambien tienen acceso de paquete:

```java
ArrayList<Misil> enemyMissiles;
ArrayList<PlayerMisil> counterMissiles;
ArrayList<City> cities;
ArrayList<Explosion> explosions;
```

### Mejora propuesta

- Cambiar los atributos internos a `private`.
- Exponer solo getters o metodos de comportamiento, como `move(deltaMs)`.
- Declarar las listas como `private`.
- Usar `List` como tipo de declaracion y `ArrayList` como implementacion.
- Evitar devolver directamente listas modificables desde getters.

Ejemplo:

```java
private final List<Misil> enemyMissiles = new ArrayList<>();
```

## 6. Long Parameter List

No hay un caso grave de lista larga de parametros. Los constructores de `GameObjectTarget`, `Misil` y `PlayerMisil` reciben cuatro parametros:

```java
(int x, int y, int targetX, int targetY)
```

Estos parametros representan claramente la posicion inicial y el objetivo, por lo que la cantidad es razonable.

Si el proyecto creciera, se podria agrupar la informacion en objetos como `Position` o `Target`.

## 7. Herencia rechazada

No se observa una herencia rechazada clara.

La herencia de `Misil` y `PlayerMisil` desde `GameObjectTarget` esta justificada porque comparten posicion y objetivo.

`GameObjectStart` es mas discutible: solo tiene una subclase, `City`, y aporta coordenadas y dos getters. Podria eliminarse y mover esos datos directamente a `City`, o reemplazarse por una clase `Position` si se necesitara reutilizarla en el futuro.

## 8. Lazy Class

### Posible caso

`GameObjectStart` es una posible Lazy Class porque tiene muy poca funcionalidad y solo es utilizada por `City`.

### Caso que no necesariamente es un problema

`GameFrame` tambien es pequena, pero cumple una responsabilidad concreta: crear y configurar la ventana principal. Por eso no es necesario eliminarla.

Las interfaces `IUpdatable` e `IReachTarget` son pequenas por naturaleza. No deben considerarse Lazy Classes automaticamente, ya que definen contratos para aplicar polimorfismo.

## 9. Complejidad artificial

No se utilizan patrones de diseno que generen complejidad artificial. Los elementos mejorables son decisiones puntuales:

- `GameObjectStart` aporta muy poco y tiene una sola subclase.
- Hay imports sin utilizar en `GameObjectTarget`.
- La llamada `super()` del constructor de `GameObjectTarget` es innecesaria.
- `collisionThresh` en `MissileComand` se declara pero no se utiliza.
- `pmToRemove` se llena durante las colisiones, pero no se elimina de `counterMissiles` despues.
- En `City` hay un atributo `urlImagen` y una variable local con el mismo nombre.
- Los comentarios descriptivos al final de varias clases son muy extensos en relacion con el codigo, aunque no afectan la ejecucion.

Se recomienda eliminar primero las variables, imports y llamadas sin uso antes de introducir nuevas abstracciones.

## 10. Nombres de variables y metodos

### Nombres demasiado cortos

Algunos nombres dificultan la lectura:

- `x`, `y`: coordenadas.
- `dx`, `dy`: velocidad o desplazamiento en cada eje.
- `e`: evento.
- `g`, `g2`: objetos graficos.
- `ex`, `em`, `pm`, `cm`: variables de bucles.
- `ais`: flujo de entrada de audio.

Podrian reemplazarse por nombres como:

```text
posicionX, posicionY
velocidadX, velocidadY
evento
graphics, graphics2D
explosion, enemyMissile, playerMissile
audioInputStream
```

### Nombres largos pero correctos

Estos nombres son descriptivos y conviene mantenerlos:

- `missileSpawnClock`
- `explosionTimerMs`
- `SHOT_COOLDOWN_MS`
- `hasReachedTarget`
- `isMarkedForRemoval`

Un nombre largo no es un problema si explica claramente el proposito de la variable o el metodo.

## 11. Orden recomendado de mejoras

1. Eliminar variables, imports y llamadas sin uso.
2. Dividir `actionPerformed` en metodos privados pequenos.
3. Corregir la logica de eliminacion de misiles y revisar `pmToRemove`.
4. Encapsular los atributos y listas que tienen acceso directo.
5. Extraer la logica comun de `Misil` y `PlayerMisil`.
6. Separar colisiones, audio y renderizado si `MissileComand` continua creciendo.
7. Evaluar si `GameObjectStart` sigue siendo necesario.
8. Mejorar nombres de variables locales demasiado cortos.
9. Agregar pruebas para movimiento, colisiones, puntajes y reinicio de partida.

## 12. Conclusion

El problema estructural mas importante es `MissileComand`, porque combina muchas responsabilidades. El segundo problema es la duplicacion entre los dos tipos de misil.

No hay una lista larga de parametros critica ni una herencia claramente rechazada. `GameObjectStart` es la abstraccion mas cuestionable, mientras que `GameObjectTarget` y las interfaces tienen una justificacion razonable, aunque pueden simplificarse y encapsularse mejor.
