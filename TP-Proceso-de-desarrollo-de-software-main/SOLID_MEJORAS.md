# Diseño SOLID aplicado

## Organización

- `modelo/GameSession.java` contiene estado y reglas de la partida: actualización, generación de misiles, colisiones, puntaje y reinicio.
- `modelo/MissileComand.java` adapta eventos Swing y el temporizador a la sesión; ya no implementa las reglas ni dibuja los objetos.
- `modelo/GameRenderer.java` se ocupa de dibujar el estado de la partida.
- `modelo/GameOverDialog.java` presenta el flujo de fin de partida y solicita el nombre para el ranking.
- `modelo/City.java`, `modelo/Misil.java`, `modelo/PlayerMisil.java` y `modelo/Explosion.java` mantienen el estado y comportamiento de sus entidades sin cargar sonidos ni dibujarse.

## Principios

- **SRP, responsabilidad única:** se separaron las reglas (`GameSession`), la vista (`GameRenderer`), la integración Swing (`MissileComand`), el audio (`SwingGameAudio`) y el flujo de fin de partida (`GameOverDialog`).
- **OCP, abierto/cerrado:** `GameObjectFactory`, `ScoreRepository` y `GameAudio` permiten sustituir la creación de entidades, el almacenamiento y el audio sin modificar el panel de juego. Las variantes de entidades existentes siguen usando el mismo renderizador.
- **LSP, sustitución de Liskov:** `Misil` y `PlayerMisil` comparten la abstracción `GameObjectTarget` y conservan sus contratos de posición y destino. La sesión también opera sobre contratos comunes `IUpdatable` e `IReachTarget`, sin exigir implementaciones específicas de esos métodos.
- **ISP, segregación de interfaces:** `IUpdatable`, `IReachTarget`, `GameAudio` y `ScoreRepository` exponen contratos acotados a las operaciones que sus consumidores necesitan.
- **DIP, inversión de dependencias:** el panel y la sesión reciben sus colaboradores por constructor. Dependen de `ScoreRepository` y `GameAudio`, no de `ScoreManager` o de clips concretos para ejecutar sus reglas. `GameObjectFactory` separa la construcción de entidades de la sesión.

## Clases modificadas y nuevas

- Nuevas: `modelo/GameSession.java`, `modelo/GameRenderer.java`, `modelo/GameOverDialog.java`, `modelo/GameObjectFactory.java`, `modelo/DefaultGameObjectFactory.java`, `modelo/SwingGameAudio.java` e `interfaces/GameAudio.java`.
- Actualizadas: `modelo/MissileComand.java`, `modelo/GameFrame.java`, `modelo/MainMenu.java`, `modelo/City.java`, `modelo/Misil.java`, `modelo/PlayerMisil.java`, `modelo/Explosion.java` y `modelo/GameObjectTarget.java`.
- El cambio previo de puntajes se mantiene: `interfaces/ScoreRepository.java` y `modelo/ScoreManager.java`.

Los constructores sin parámetros conservan el arranque normal con implementaciones predeterminadas. Para extender el comportamiento, se pueden inyectar otras implementaciones en los constructores.

## Alcance

La lógica de colisiones todavía conoce los tipos actuales de misiles y explosiones, y `GameRenderer` dibuja explícitamente las entidades actuales. Agregar una categoría nueva de entidad puede requerir ampliar esas clases; el diseño facilita sustituir las implementaciones existentes, pero no pretende que cualquier entidad nueva se agregue sin cambios.