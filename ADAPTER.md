# Patron Adapter en el proyecto

## Objetivo

El patron Adapter permite que dos componentes con interfaces diferentes
colaboren. El adaptador implementa la interfaz que espera el cliente y traduce
sus llamadas al metodo disponible en el componente existente, sin tener que
modificar este ultimo.

## Cambios realizados

### `interfaces/IDamageable.java`

Se agrego la interfaz `IDamageable`, que define la operacion
`receiveDamage(int amount)`. Esta es la interfaz que utiliza el cliente para
aplicar dano a un objeto.

### `modelo/CityDamageAdapter.java`

Se agrego `CityDamageAdapter`, que implementa `IDamageable` y recibe una
instancia de `City`. Cuando se invoca `receiveDamage(amount)`, el adaptador
delega la llamada en el metodo que ya existia en `City`: `takeDamage(amount)`.

### `modelo/MissileComand.java`

Se conecto el adaptador al flujo del juego:

- Las ciudades se agregan mediante `addCity`, que registra tanto la ciudad
  como su adaptador.
- Cuando un misil enemigo impacta una ciudad, `MissileComand` aplica el dano
  mediante `cityDamageAdapters` e `IDamageable`, en lugar de llamar
  directamente a `City.takeDamage`.

## Roles del patron

| Rol | Implementacion |
|---|---|
| Target (interfaz esperada por el cliente) | `IDamageable` |
| Adapter | `CityDamageAdapter` |
| Adaptee (clase existente cuya interfaz se adapta) | `City` |
| Cliente | `MissileComand` |

## Por que se hizo

Antes del cambio, `MissileComand` dependia directamente del metodo
`City.takeDamage`. Ahora el cliente utiliza la interfaz `IDamageable` y el
adaptador convierte esa operacion a la API existente de `City`.

Esto muestra el patron Adapter en un caso real del juego, separa al cliente de
la interfaz concreta de `City` y evita modificar la logica interna de dano,
salud y destruccion de la ciudad.

## Comportamiento y verificacion

El adaptador no cambia la cantidad de dano ni el comportamiento de `City`:
simplemente delega la llamada. Las fuentes Java del proyecto compilaron
correctamente despues del cambio.
