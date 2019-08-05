package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.persistence.TowerDao;

import java.util.LinkedList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.*;
import static de.diegrafen.exmatrikulatortd.controller.factories.WaveFactory.*;
import static de.diegrafen.exmatrikulatortd.model.Difficulty.EASY;
import static de.diegrafen.exmatrikulatortd.util.Constants.NUMBER_OF_TOWERS;

public final class NewGameFactory {

    private static final int MAX_PLAYERS = 2;

    public static final int STANDARD_SINGLE_PLAYER_GAME = 0;

    public static final int ENDLESS_SINGLE_PLAYER_GAME = 1;

    public static final int MULTIPLAYER_DUEL = 2;

    public static final int MULTIPLAYER_STANDARD_GAME = 3;

    public static final int MULTIPLAYER_ENDLESS_GAME = 4;

    private NewGameFactory() {

    }

    // TODO: Irgendwie muss hier noch der Schwierigkeitsgrad berücksichtigt werden
    public static Gamestate createNewGame(int gameMode, int numberOfPlayers) {

        Gamestate gamestate = null;

        if (numberOfPlayers == 1) {
            switch (gameMode) {
                case STANDARD_SINGLE_PLAYER_GAME:
                    gamestate = createStandardSinglePlayerGame();
                    break;
                case ENDLESS_SINGLE_PLAYER_GAME:
                    gamestate = createEndlessSinglePlayerGame();
                    break;
            }
        } else if (numberOfPlayers > 1 & numberOfPlayers <= MAX_PLAYERS) {
            switch (gameMode) {
                case MULTIPLAYER_DUEL:
                    gamestate = createMultiPlayerDuell(numberOfPlayers);
                    break;
                case MULTIPLAYER_STANDARD_GAME:
                    gamestate = createMultiStandardGame(numberOfPlayers);
                    break;
                case MULTIPLAYER_ENDLESS_GAME:
                    gamestate = createMultiEndlessGame(numberOfPlayers);
                    break;
            }
        }

        if (gamestate != null) {
            gamestate.setGameMode(gameMode);
        }

        return gamestate;
    }

    private static Gamestate createStandardSinglePlayerGame() {
        List<Player> players = createPlayers(1);
        List<Wave> waves = createWaves();
        //List<Tower> buildableTowers = createBuildableTowers();
        return new Gamestate(players, waves);
    }

    private static Gamestate createEndlessSinglePlayerGame() {
        Gamestate gamestate = createStandardSinglePlayerGame();
        gamestate.setEndlessGame(true);
        return gamestate;
    }

    private static Gamestate createMultiPlayerDuell(int numberOfPlayers) {
        Gamestate gamestate = createMultiStandardGame(numberOfPlayers);
        //gamestate.setDuel();

        return gamestate;
    }

    private static Gamestate createMultiStandardGame(int numberOfPlayers) {
        List<Player> players = createPlayers(numberOfPlayers);
        List<Wave> waves = createWaves();
        //List<Tower> buildableTowers = createBuildableTowers();
        return new Gamestate(players, waves);
    }

    private static Gamestate createMultiEndlessGame(int numberOfPlayers) {
        Gamestate gamestate = createMultiStandardGame(numberOfPlayers);
        gamestate.setEndlessGame(true);
        return gamestate;
    }

    private static List<Wave> createWaves() {
        List<Wave> waves = new LinkedList<>();
        int i = 0;
        while (i < 4) {
            waves.add(createWave(REGULAR_WAVE));
            waves.add(createWave(HEAVY_WAVE));
            waves.add(createWave(REGULAR_AND_HEAVY_WAVE));
            //waves.add(createWave(BOSS_WAVE));
            i++;
        }
        return waves;
    }

    private static List<Player> createPlayers(int numberOfPlayers) {
        List<Player> players = new LinkedList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player(i);
            player.setCurrentLives(25);
            player.setMaxLives(25);
            player.setResources(1000);
            player.setScore(0);
            player.setDifficulty(EASY);
            players.add(player);
        }
        return players;
    }
}
