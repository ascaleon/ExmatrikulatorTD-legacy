package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.persistence.TowerDao;

import java.util.LinkedList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.WaveFactory.*;
import static de.diegrafen.exmatrikulatortd.util.Constants.*;

public final class NewGameFactory {

    private NewGameFactory() {

    }

    public static Gamestate createNewGame(int gameMode, int numberOfPlayers, int difficulty, String[] names) {

        Gamestate gamestate = null;

        if (numberOfPlayers == 1) {
            switch (gameMode) {
                case STANDARD_SINGLE_PLAYER_GAME:
                    gamestate = createStandardSinglePlayerGame(difficulty, names);
                    break;
                case ENDLESS_SINGLE_PLAYER_GAME:
                    gamestate = createEndlessSinglePlayerGame(difficulty, names);
                    break;
            }
        } else if (numberOfPlayers > 1) {
            switch (gameMode) {
                case MULTIPLAYER_DUEL:
                    gamestate = createMultiPlayerDuell(numberOfPlayers, difficulty, names);
                    break;
                case MULTIPLAYER_STANDARD_GAME:
                    gamestate = createMultiStandardGame(numberOfPlayers, difficulty, names);
                    break;
                case MULTIPLAYER_ENDLESS_GAME:
                    gamestate = createMultiEndlessGame(numberOfPlayers, difficulty, names);
                    break;
            }
        }

        if (gamestate != null) {
            gamestate.setGameMode(gameMode);
        }

        return gamestate;
    }

    private static Gamestate createStandardSinglePlayerGame(int difficulty, String[] names) {
        List<Player> players = createPlayers(1, difficulty, names);
        List<Wave> waves = createWaves();
        return new Gamestate(players, waves);
    }

    private static Gamestate createEndlessSinglePlayerGame(int difficulty, String[] names) {
        Gamestate gamestate = createStandardSinglePlayerGame(difficulty, names);
        gamestate.setEndlessGame(true);
        return gamestate;
    }

    private static Gamestate createMultiPlayerDuell(int numberOfPlayers, int difficulty, String[] names) {
        Gamestate gamestate = createMultiStandardGame(numberOfPlayers, difficulty, names);
        //gamestate.setDuel();
        return gamestate;
    }

    private static Gamestate createMultiStandardGame(int numberOfPlayers, int difficulty, String[] names) {
        List<Player> players = createPlayers(numberOfPlayers, difficulty, names);
        List<Wave> waves = createWaves();
        return new Gamestate(players, waves);
    }

    private static Gamestate createMultiEndlessGame(int numberOfPlayers, int difficulty, String[] names) {
        Gamestate gamestate = createMultiStandardGame(numberOfPlayers, difficulty, names);
        gamestate.setEndlessGame(true);
        return gamestate;
    }

    private static List<Wave> createWaves() {
        List<Wave> waves = new LinkedList<>();
        int i = 0;
        while (i < 3) {
            waves.add(createWave(REGULAR_WAVE));
            waves.add(createWave(HEAVY_WAVE));
            waves.add(createWave(FAST_WAVE));
            waves.add(createWave(REGULAR_AND_HEAVY_WAVE));
            waves.add(createWave(HEAVY_AND_FAST_WAVE));
            waves.add(createWave(BOSS_WAVE));
            i++;
        }
        return waves;
    }

    private static List<Player> createPlayers(int numberOfPlayers, int difficulty, String[] names) {
        List<Player> players = new LinkedList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player(i);
            if (names[i] != null) {
                player.setPlayerName(names[i]);
            } else {
                player.setPlayerName("Name unbekannt.");
            }
            player.setCurrentLives(25);
            player.setMaxLives(25);
            player.setResources(1000);
            player.setScore(0);
            player.setDifficulty(difficulty);
            players.add(player);
        }
        return players;
    }
}
