package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;

import java.util.LinkedList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.WaveFactory.*;

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

        return gamestate;
    }

    private static Gamestate createStandardSinglePlayerGame() {
        Player player = new Player(0);
        List<Wave> waves = new LinkedList<>();

        Gamestate gamestate = new Gamestate();

        waves.add(createWave(REGULAR_AND_HEAVY_WAVE));
        waves.add(createWave(REGULAR_WAVE));
        waves.add(createWave(REGULAR_AND_HEAVY_WAVE));
        waves.add(createWave(BOSS_WAVE));

        player.setWaves(waves);
        player.setCurrentLives(25);
        player.setMaxLives(25);
        player.setResources(1000);
        player.setScore(0);

        gamestate.addPlayer(player);

        return gamestate;
    }

    private static Gamestate createEndlessSinglePlayerGame() {
        return null;
    }

    private static Gamestate createMultiPlayerDuell(int numberOfPlayers) {
        List<Player> players = new LinkedList<>();
        Gamestate gamestate = new Gamestate();

        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player(i);

            player.setCurrentLives(25);
            player.setMaxLives(25);
            player.setResources(1000);
            player.setScore(0);

            players.add(player);
        }

        gamestate.setPlayers(players);

        return gamestate;
    }

    private static Gamestate createMultiStandardGame(int numberOfPlayers) {
        List<Player> players = new LinkedList<>();
        Gamestate gamestate = new Gamestate();

        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player(i);
            List<Wave> waves = new LinkedList<>();

            waves.add(createWave(REGULAR_AND_HEAVY_WAVE));
            waves.add(createWave(REGULAR_WAVE));
            waves.add(createWave(REGULAR_AND_HEAVY_WAVE));
            waves.add(createWave(BOSS_WAVE));

            player.setWaves(waves);
            player.setCurrentLives(25);
            player.setMaxLives(25);
            player.setResources(1000);
            player.setScore(0);

            players.add(player);
        }

        gamestate.setPlayers(players);

        return gamestate;
    }

    private static Gamestate createMultiEndlessGame(int numberOfPlayers) {
        return null;
    }
}
