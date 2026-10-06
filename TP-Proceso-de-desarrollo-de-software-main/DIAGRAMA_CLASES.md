# Diagrama de clases

El diagrama muestra las relaciones principales de la estructura actual. Las flechas punteadas representan dependencias o implementaciones de interfaces; los rombos representan objetos contenidos por otro componente.

```mermaid
classDiagram
    direction LR

    class MainMenu {
        -ScoreRepository scoreRepository
        -GameObjectFactory objectFactory
        -GameAudio gameAudio
    }

    class GameFrame

    class MissileComand {
        -ScoreRepository scoreRepository
        -GameObjectFactory objectFactory
        -GameAudio gameAudio
        -GameSession session
        -GameRenderer renderer
        -GameOverDialog gameOverDialog
        +paintComponent(Graphics)
        +actionPerformed(ActionEvent)
        +mousePressed(MouseEvent)
    }

    class GameSession {
        -GameObjectFactory objectFactory
        -GameAudio gameAudio
        -List~Misil~ enemyMissiles
        -List~PlayerMisil~ counterMissiles
        -List~City~ cities
        -List~Explosion~ explosions
        -int score
        +update(long, int, int)
        +fireAt(int, int)
        +reset()
        +isGameOver() boolean
    }

    class GameRenderer {
        +draw(Graphics2D, GameSession)
    }

    class GameOverDialog {
        -ScoreRepository scoreRepository
        +show(Component, int) Action
    }

    class GameObjectFactory {
        <<interface>>
        +createCity(String, int, int) City
        +createEnemyMissile(int, int, int, int) Misil
        +createPlayerMissile(int, int, int, int) PlayerMisil
        +createExplosion(int, int) Explosion
    }

    class DefaultGameObjectFactory

    class GameAudio {
        <<interface>>
        +startBackgroundMusic()
        +stopBackgroundMusic()
        +playShot()
        +playCityExplosion()
    }

    class SwingGameAudio

    class ScoreRepository {
        <<interface>>
        +loadScores() List~ScoreEntry~
        +saveScores(List~ScoreEntry~)
    }

    class ScoreManager

    class ScoreEntry {
        -String name
        -int score
        +getName() String
        +getScore() int
    }

    class GameObjectStart {
        <<abstract>>
        #int x
        #int y
        +getX() int
        +getY() int
    }

    class GameObjectTarget {
        <<abstract>>
        #double x
        #double y
        #int targetX
        #int targetY
        +getX() int
        +getY() int
    }

    class City {
        -String name
        -int health
        -boolean destroyed
        -long explosionTimerMs
        +takeDamage(int)
        +update(long)
        +reset()
        +isDestroyed() boolean
    }

    class Misil {
        -double dx
        -double dy
        -boolean markedForRemoval
        +update(long)
        +hasReachedTarget() boolean
        +markForRemoval()
    }

    class PlayerMisil {
        -double dx
        -double dy
        +update(long)
        +hasReachedTarget() boolean
        +getTargetX() int
        +getTargetY() int
    }

    class Explosion {
        -int radius
        -int elapsedTime
        +update(long)
        +hits(Misil) boolean
        +isFinished() boolean
    }

    class IUpdatable {
        <<interface>>
        +update(long)
    }

    class IReachTarget {
        <<interface>>
        +hasReachedTarget() boolean
    }

    JFrame <|-- MainMenu
    JFrame <|-- GameFrame
    JPanel <|-- MissileComand
    ActionListener <|.. MissileComand
    MouseListener <|.. MissileComand

    GameFrame *-- MissileComand : contiene
    MissileComand *-- GameSession : delega reglas
    MissileComand *-- GameRenderer : delega dibujo
    MissileComand *-- GameOverDialog : delega fin de juego
    GameSession o-- City : administra
    GameSession o-- Misil : administra
    GameSession o-- PlayerMisil : administra
    GameSession o-- Explosion : administra

    MainMenu ..> GameFrame : inicia
    MainMenu ..> ScoreRepository
    MainMenu ..> GameObjectFactory
    MainMenu ..> GameAudio
    GameFrame ..> ScoreRepository
    GameFrame ..> GameObjectFactory
    GameFrame ..> GameAudio
    GameRenderer ..> GameSession : consulta estado
    GameOverDialog ..> ScoreRepository
    GameSession ..> GameObjectFactory : crea entidades
    GameSession ..> GameAudio : reproduce evento

    GameObjectFactory <|.. DefaultGameObjectFactory
    GameAudio <|.. SwingGameAudio
    ScoreRepository <|.. ScoreManager
    ScoreRepository ..> ScoreEntry

    GameObjectStart <|-- City
    GameObjectTarget <|-- Misil
    GameObjectTarget <|-- PlayerMisil
    IUpdatable <|.. City
    IUpdatable <|.. Misil
    IUpdatable <|.. PlayerMisil
    IUpdatable <|.. Explosion
    IReachTarget <|.. Misil
    IReachTarget <|.. PlayerMisil
```