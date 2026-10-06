package interfaces;

import java.util.List;

import modelo.ScoreEntry;

public interface ScoreRepository {
    List<ScoreEntry> loadScores();

    void saveScores(List<ScoreEntry> scores);
}