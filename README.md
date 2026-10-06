# MissileCOOMand

Integrantes:    
 ~ Joaquin Olmedo  
 ~  Denis Lautaro

## Patron Strategy

El movimiento de los misiles se delega a `IMovementStrategy`, que permite intercambiar
algoritmos sin modificar `Misil` ni `PlayerMisil`. `LinearMovementStrategy` conserva
el movimiento lineal actual por defecto; `DirectMovementStrategy` recalcula el rumbo
y ajusta el ultimo paso para no sobrepasar el objetivo.

Se puede elegir una estrategia al crear un misil mediante el constructor que recibe
`IMovementStrategy`, o cambiarla durante la ejecucion con `setMovementStrategy`.
