package de.diegrafen.exmatrikulatortd.communication.server.responses;

import java.util.List;

public class GetGameInfoResponse extends Response {

    private boolean update;

    private int allocatedPlayerNumber;

    private String[] playerNames;

    private String[] playerProfilePicturePaths;

    private String mapPath;

    public GetGameInfoResponse() {
        super();
    }

    public GetGameInfoResponse(boolean update, int allocatedPlayerNumber, String[] playerNames, String[] playerProfilePicturePaths, String mapPath) {
        this.update = update;
        this.allocatedPlayerNumber = allocatedPlayerNumber;
        this.playerNames = playerNames;
        this.playerProfilePicturePaths = playerProfilePicturePaths;
        this.mapPath = mapPath;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public int getAllocatedPlayerNumber() {
        return allocatedPlayerNumber;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public String[] getPlayerProfilePicturePaths() {
        return playerProfilePicturePaths;
    }

    public String getMapPath() {
        return mapPath;
    }
}
