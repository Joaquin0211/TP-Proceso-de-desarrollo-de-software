# GRASP aplicado al proyecto

## 1. Creador

La clase que debe crear los objetos del juego es `GameFactory`, ya que es la responsable de instanciar las ciudades, los misiles y las explosiones. Esto evita que la clase principal del juego tenga que crear todas las entidades internamente.

`GameFactory` se encarga de:
- crear ciudades
- crear misiles enemigos
- crear misiles del jugador
- crear explosiones

Esto cumple con el patrón GRASP de Creador, porque la clase que conoce más acerca de la creación de cada entidad es la que debe encargarse de instanciarlas.

## 2. Controlador

La clase `GameController` funciona como controlador del juego, porque organiza el flujo principal del sistema. Se encarga de:
- actualizar el estado del juego
- manejar colisiones
- controlar puntajes
- eliminar objetos terminados
- verificar si todas las ciudades fueron destruidas

La clase `MissileComand` queda como una capa de interfaz y eventos, mientras que la lógica del juego se delega a `GameController`.

Esto corresponde al GRASP de Controlador, porque una clase que recibe eventos del usuario y coordina la ejecución del caso de uso del juego es el punto natural para gestionar la secuencia del flujo.

## 3. Bajo acoplamiento

Antes, la clase `MissileComand` realizaba demasiadas tareas: creaba ciudades, disparos, explosiones, controlaba puntajes, sonidos, colisiones y fin del juego. Eso generaba un alto acoplamiento entre la lógica del juego y la interfaz.

Ahora, la responsabilidad está distribuida:
- `GameFactory` crea los objetos
- `GameController` controla la lógica del juego
- `MissileComand` se encarga de la UI y de los eventos del mouse
- `City`, `Misil`, `PlayerMisil` y `Explosion` responden a su propia lógica

Esto reduce el acoplamiento y hace que los cambios en una clase no afecten muchas otras.

## 4. Alta cohesión

Cada clase tiene una responsabilidad más clara:
- `City`: representa y destruye una ciudad
- `Misil`: representa el misil enemigo
- `PlayerMisil`: representa el misil disparado por el jugador
- `Explosion`: representa el efecto visual y de daño
- `GameController`: coordina la lógica del juego
- `GameFactory`: crea las instancias

La cohesión mejora porque cada clase tiene un propósito específico y no mezcla varias responsabilidades diferentes.

## 5. Experto en Información

La lógica relacionada con el movimiento de los proyectiles se movió a `GameObjectTarget`, porque esa clase conoce la posición, el objetivo y la dirección del objeto. Es la clase que tiene la información necesaria para calcular el movimiento y la distancia al objetivo.

Por eso, `GameObjectTarget` es el experto en información para:
- guardar la posición
- guardar el objetivo
- calcular la dirección
- mover el objeto
- saber si llegó al objetivo

## 6. Conclusión

Con estos cambios, el proyecto cumple mejor los principios GRASP aplicables a este caso:
- `GameFactory` como Creador
- `GameController` como Controlador
- `GameObjectTarget` como Experto en Información
- menor acoplamiento y mayor cohesión entre clases

La solución queda mejor estructurada, más mantenible y más fácil de extender en el futuro.

## 7. Cambios realizados

Se realizaron los siguientes cambios en el proyecto:

- Se creó `GameFactory.java` para centralizar la creación de ciudades, misiles y explosiones.
- Se creó `GameController.java` para controlar el estado del juego, los movimientos, las colisiones, el puntaje y la limpieza de objetos.
- Se modificó `MissileComand.java` para delegar la lógica del juego a `GameController` y la creación de objetos a `GameFactory`.
- Se modificó `GameObjectTarget.java` para concentrar la lógica común de los proyectiles: dirección, movimiento, distancia al objetivo y llegada al destino.
- Se modificó `Misil.java` para utilizar la lógica de movimiento heredada de `GameObjectTarget`.
- Se modificó `PlayerMisil.java` para reutilizar la misma lógica común y conservar su velocidad y dibujo específicos.
- Se modificó `City.java` para que la ciudad determine si un misil la impactó mediante el método `isHitBy(...)`.

Estos cambios no modifican el objetivo del juego. Reorganizan las responsabilidades para aplicar Creador, Controlador, Experto en Información, Bajo Acoplamiento y Alta Cohesión.
