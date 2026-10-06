# Diagrama de clases del sistema Missile Command

El siguiente diagrama representa las clases y relaciones actuales del proyecto.

```mermaid
classDiagram
    direction TB

    class IUpdatable {
        <<interface>>
        +update(deltaMs: long)
    }

    class IReachTarget {
        <<interface>>
        +hasReachedTarget() boolean
    }

    class GameObjectStart {
        <<abstract>>
        #x: int
        #y: int
        +GameObjectStart(x: int, y: int)
        +getX() int
        +getY() int
    }

    class GameObjectTarget {
        <<abstract>>
        -x: double
        -y: double
        -targetX: int {readOnly}
        -targetY: int {readOnly}
        -dx: double
        -dy: double
        +GameObjectTarget(x: int, y: int, targetX: int, targetY: int)
        #initializeDirection(speed: double)
        #move(deltaMs: long)
        +getDistanceToTarget() double
        +hasReachedTarget(threshold: double) boolean
        +getX() int
        +getY() int
        +getTargetX() int
        +getTargetY() int
    }

    class City {
        -name: String
        -health: int
        -destroyed: boolean
        -explosionGif: Image
        -explosionTimerMs: long
        -explosionClips: Clip[]
        -soundPlayed: boolean
        -urlImagen: URL
        +City(name: String, x: int, y: int)
        +takeDamage(amount: int)
        +isHitBy(projectileX: double, projectileY: double, radius: double) boolean
        +update(deltaMs: long)
        +isDestroyed() boolean
        +reset()
        +draw(graphics: Graphics)
    }

    class Misil {
        -SPEED: double
        -markedForRemoval: boolean
        +Misil(x: int, y: int, targetX: int, targetY: int)
        +update(deltaMs: long)
        +hasReachedTarget() boolean
        +draw(graphics: Graphics2D)
        +markForRemoval()
        +isMarkedForRemoval() boolean
    }

    class PlayerMisil {
        -SPEED: double
        +PlayerMisil(x: int, y: int, targetX: int, targetY: int)
        +update(deltaMs: long)
        +hasReachedTarget() boolean
        +draw(graphics: Graphics2D, baseX: int, baseY: int)
    }

    class Explosion {
        -x: int
        -y: int
        -radius: int
        -elapsedTime: int
        +Explosion(x: int, y: int)
        +update(deltaMs: long)
        +isFinished() boolean
        +hits(misil: Misil) boolean
        +draw(graphics: Graphics2D)
    }

    class GameFactory {
        <<utility>>
        +createCity(name: String, x: int, y: int) City
        +createEnemyMissile(x: int, y: int, targetX: int, targetY: int) Misil
        +createPlayerMissile(x: int, y: int, targetX: int, targetY: int) PlayerMisil
        +createExplosion(x: int, y: int) Explosion
    }

    class GameController {
        -enemyMissiles: List~Misil~
        -playerMissiles: List~PlayerMisil~
        -explosions: List~Explosion~
        -cities: List~City~
        -score: int
        -missileCount: int
        +GameController(cities: List~City~)
        +getEnemyMissiles() List~Misil~
        +getPlayerMissiles() List~PlayerMisil~
        +getExplosions() List~Explosion~
        +getScore() int
        +addScore(value: int)
        +addEnemyMissile(startX: int, endY: int, targetX: int, targetY: int)
        +addPlayerMissile(x: int, y: int, targetX: int, targetY: int)
        +addExplosion(x: int, y: int)
        +updateEnemyMissiles(deltaMs: long)
        +updatePlayerMissiles(deltaMs: long)
        +updateExplosions(deltaMs: long)
        +updateCities(deltaMs: long)
        +handlePlayerEnemyCollision()
        +handleTargetReachedPlayerShots()
        +handleBlastCollisions()
        +handleEnemyCityImpact()
        +cleanupFinishedObjects()
        +allCitiesDestroyed() boolean
        +getMissileCount() int
        +setMissileCount(value: int)
        +setScore(value: int)
        +reset(scoreValue: int)
    }

    class MissileComand {
        -cities: List~City~
        -gameController: GameController
        -missileSpawnClock: int
        -maxMisil: int
        -misilCount: int
        -timer: Timer
        -score: int
        -lastShotTime: long
        -bgmClip: Clip
        -shootClip: Clip
        +MissileComand()
        +paintComponent(graphics: Graphics)
        +actionPerformed(event: ActionEvent)
        +mousePressed(event: MouseEvent)
        -allCitiesDestroyed() boolean
        -resetGame()
        -showGameOverDialog()
    }

    class GameFrame {
        +GameFrame()
        +main(args: String[])
    }

    class MainMenu {
        +MainMenu()
        +main(args: String[])
    }

    class ScoreEntry {
        -name: String
        -score: int
        +ScoreEntry(name: String, score: int)
        +getName() String
        +getScore() int
        +compareTo(other: ScoreEntry) int
        +toString() String
    }

    class ScoreManager {
        <<utility>>
        -FILE_NAME: String
        +loadScores() List~ScoreEntry~
        +saveScores(scores: List~ScoreEntry~)
    }

    GameObjectStart <|-- City
    GameObjectTarget <|-- Misil
    GameObjectTarget <|-- PlayerMisil
    IUpdatable <|.. City
    IUpdatable <|.. Misil
    IUpdatable <|.. PlayerMisil
    IReachTarget <|.. Misil
    IReachTarget <|.. PlayerMisil

    MissileComand ..|> ActionListener
    MissileComand ..|> MouseListener
    GameFrame --> MissileComand : crea
    MainMenu --> GameFrame : inicia
    MainMenu --> ScoreManager : consulta
    MissileComand ..> GameFactory : crea ciudades
    MissileComand ..> ScoreManager : persiste puntajes
    MissileComand *-- GameController : utiliza
    MissileComand o-- City : muestra
    GameController o-- City : administra
    GameController o-- Misil : administra
    GameController o-- PlayerMisil : administra
    GameController o-- Explosion : administra
    GameController ..> GameFactory : crea objetos
    GameFactory ..> City : crea
    GameFactory ..> Misil : crea
    GameFactory ..> PlayerMisil : crea
    GameFactory ..> Explosion : crea
    Explosion ..> Misil : detecta impacto
    ScoreManager ..> ScoreEntry : carga y guarda
    ScoreEntry ..|> Comparable~ScoreEntry~
```

## Resumen de relaciones

- `City` hereda de `GameObjectStart` e implementa `IUpdatable`.
- `Misil` y `PlayerMisil` heredan de `GameObjectTarget` e implementan `IUpdatable` e `IReachTarget`.
- `GameController` administra las listas de ciudades, misiles y explosiones.
- `GameController` expone vistas de solo lectura de sus listas; las modificaciones se realizan mediante sus operaciones de dominio.
- `GameFactory` crea las entidades del juego y aplica el principio GRASP Creador.
- `MissileComand` recibe eventos de Swing y delega la lógica a `GameController`.
- `GameFrame` inicia la ventana del juego y `MainMenu` permite iniciar partidas o consultar puntajes.
- `GameFactory` y `ScoreManager` son clases utilitarias con métodos estáticos; por eso el código depende directamente de implementaciones concretas para crear entidades y guardar puntajes.
- `ScoreManager` gestiona los objetos `ScoreEntry` y la persistencia en `scores.txt`.
