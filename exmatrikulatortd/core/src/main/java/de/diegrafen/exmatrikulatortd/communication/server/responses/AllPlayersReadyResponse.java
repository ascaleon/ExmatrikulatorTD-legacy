package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.model.Gamestate;

public class AllPlayersReadyResponse extends Response {

    private Gamestate gamestate;

    private int difficulty;

    private int numberOfPlayers;

    private int allocatedPlayerNumber;

    private int gamemode;

    private String mapPath;

    private String[] playerNames;

    public AllPlayersReadyResponse() {
        super();
    }

    public AllPlayersReadyResponse(Gamestate gamestate, int numberOfPlayers, String mapPath) {
        this();
        this.gamestate = gamestate;
        this.numberOfPlayers = numberOfPlayers;
        this.mapPath = mapPath;
    }

    public AllPlayersReadyResponse(int difficulty, int numberOfPlayers, int allocatedPlayerNumber, int gamemode, String mapPath, String[] playerNames) {
        this();
        this.difficulty = difficulty;
        this.numberOfPlayers = numberOfPlayers;
        this.allocatedPlayerNumber = allocatedPlayerNumber;
        this.gamemode = gamemode;
        this.mapPath = mapPath;
        this.playerNames = playerNames;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getAllocatedPlayerNumber() {
        return allocatedPlayerNumber;
    }

    public int getGamemode() {
        return gamemode;
    }

    public String getMapPath() {
        return mapPath;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }
}
