package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.Gamestate;

public final class NewGameFactory {

    public static final int MAX_PLAYERS = 2;

    public static final int STANDARD_SINGLE_PLAYER_GAME = 0;

    public static final int ENDLESS_SINGLE_PLAYER_GAME = 1;

    public static final int MULTIPLAYER_DUELL = 2;

    public static final int MULTIPLAYER_STANDARD_GAME = 3;

    public static final int MULTIPLAYER_ENDLESS_GAME = 4;

    private NewGameFactory() {

    }

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
                case MULTIPLAYER_DUELL:
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
        return null;
    }

    private static Gamestate createEndlessSinglePlayerGame() {
        return null;
    }

    private static Gamestate createMultiPlayerDuell(int numberOfPlayers) {
        return null;
    }

    private static Gamestate createMultiStandardGame(int numberOfPlayers) {
        return null;
    }

    private static Gamestate createMultiEndlessGame(int numberOfPlayers) {
        return null;
    }
}
