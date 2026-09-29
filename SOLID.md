# Aplicacion de SOLID

## Objetivo

Aplicar principios SOLID al juego Missile Command manteniendo el comportamiento existente. La refactorizacion se realiza de forma incremental para reducir el riesgo de introducir errores en la logica del juego.

## Cambios realizados

### 1. Encapsulamiento de `GameObjectTarget`

Los atributos de posicion, objetivo y direccion pasaron de `protected` a `private`.

Antes, `Misil` y `PlayerMisil` podian modificar directamente el estado interno de la clase base. Ahora utilizan los metodos definidos por la clase base:

- `initializeDirection(double speed)` para establecer la direccion.
- `move(long deltaMs)` para actualizar la posicion.
- `getX()`, `getY()`, `getTargetX()` y `getTargetY()` para consultar datos.

Esto reduce el acoplamiento y hace que `GameObjectTarget` sea la unica clase responsable de modificar su movimiento.

### 2. Proteccion de las colecciones del controlador

`GameController` mantiene sus listas internamente como `ArrayList`, pero sus getters ahora devuelven `Collections.unmodifiableList(...)`.

De esta forma, `MissileComand` puede recorrer los objetos del juego sin poder agregar, eliminar o reemplazar elementos directamente. Las modificaciones se realizan mediante operaciones del controlador como `addEnemyMissile`, `addPlayerMissile`, `addExplosion` y `reset`.

### 3. Centralizacion del puntaje

Las colisiones ya no modifican el atributo `score` directamente. Utilizan `addScore(int value)`, que concentra la regla de incremento en un unico metodo.

Esto evita duplicar la forma de actualizar el puntaje y facilita cambiar la regla en el futuro.

### 4. Dibujo mediante la interfaz publica

`Misil` y `PlayerMisil` dejaron de leer directamente los campos de la clase base para dibujar. Ahora usan `getX()` y `getY()`.

El comportamiento visual se conserva, pero las subclases dependen de una interfaz de consulta y no de la representacion interna.

## Relacion con los principios SOLID

### S - Single Responsibility Principle

Cada clase debe tener un motivo principal para cambiar.

- `GameObjectTarget` administra posicion, objetivo y movimiento.
- `Misil` y `PlayerMisil` definen las diferencias de cada proyectil.
- `GameController` coordina el estado y las reglas principales del juego.
- `ScoreManager` administra la persistencia de los puntajes.
- `GameFactory` centraliza la creacion de objetos del modelo.

`GameController` todavia contiene varias reglas de interaccion. Como mejora futura conviene extraer `CollisionManager` y, si el renderizado sigue creciendo, `GameRenderer`.

### O - Open/Closed Principle

El uso de `GameObjectTarget`, `IUpdatable` e `IReachTarget` permite agregar nuevos objetos moviles o nuevos tipos de proyectiles sin modificar la logica comun de movimiento.

La fabrica tambien deja centralizado el punto de creacion, aunque podria evolucionar a fabricas especializadas si aparecen mas tipos de objetos.

### L - Liskov Substitution Principle

`Misil` y `PlayerMisil` pueden utilizarse como objetos actualizables y con objetivo porque cumplen los contratos de `IUpdatable` e `IReachTarget`.

Ambas clases respetan el contrato de la clase base: tienen una posicion, un objetivo, pueden actualizarse y pueden informar si llegaron a destino.

### I - Interface Segregation Principle

Las interfaces son pequenas y especificas:

- `IUpdatable` solo exige `update(long deltaMs)`.
- `IReachTarget` solo exige `hasReachedTarget()`.

Una clase no necesita implementar metodos que no utiliza.

### D - Dependency Inversion Principle

El controlador trabaja con listas declaradas como `List`, no depende de operaciones especificas de `ArrayList` para su contrato publico. Ademas, las clases de dominio dependen de contratos pequenos para actualizarse y detectar llegada al objetivo.

La aplicacion todavia tiene dependencias concretas pendientes de mejorar, especialmente la creacion directa de objetos y la persistencia estatica de `ScoreManager`. Una siguiente etapa podria inyectar una interfaz como `ScoreRepository` y una fabrica de objetos.

## Archivos modificados

- `modelo/GameObjectTarget.java`: atributos internos privados.
- `modelo/Misil.java`: dibujo mediante getters.
- `modelo/PlayerMisil.java`: dibujo mediante getters.
- `modelo/GameController.java`: listas de solo lectura y actualizacion centralizada del puntaje.
- `SOLID.md`: documentacion de principios, cambios y mejoras futuras.

## Verificacion

Se compilaron todas las clases Java con `javac` despues de los cambios. La compilacion finalizo correctamente y no se modifico el comportamiento de movimiento, colisiones, explosiones o puntaje.

## Proximas mejoras recomendadas

1. Extraer las colisiones de `GameController` a `CollisionManager`.
2. Inyectar un repositorio de puntajes para eliminar la dependencia estatica de `ScoreManager`.
3. Separar la actualizacion del modelo del dibujo de Swing.
4. Agregar pruebas unitarias para movimiento, colisiones, puntajes y reinicio.
