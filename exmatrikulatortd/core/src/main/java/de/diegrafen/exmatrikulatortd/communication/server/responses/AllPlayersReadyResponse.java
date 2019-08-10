package de.diegrafen.exmatrikulatortd.communication.server.responses;

public class AllPlayersReadyResponse extends Response {

    private int difficulty;

    private int numberOfPlayers;

    private int allocatedPlayerNumber;

    private int gamemode;

    private String mapPath;

    public AllPlayersReadyResponse() {
        super();
    }

    public AllPlayersReadyResponse(int difficulty, int numberOfPlayers, int allocatedPlayerNumber, int gamemode, String mapPath) {
        this();
        this.difficulty = difficulty;
        this.numberOfPlayers = numberOfPlayers;
        this.allocatedPlayerNumber = allocatedPlayerNumber;
        this.gamemode = gamemode;
        this.mapPath = mapPath;
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
}
