package de.diegrafen.exmatrikulatortd.communication.server.responses;

import java.util.List;

public class GetGameInfoResponse extends Response {

    private boolean update;

    private int allocatedPlayerNumber;

    private List<String> playerNames;

    private List<String> playerProfilePicturePaths;

    private String mapPath;

    public GetGameInfoResponse() {
        super();
    }

    public GetGameInfoResponse(boolean update, int allocatedPlayerNumber, List<String> playerNames, List<String> playerProfilePicturePaths, String mapPath) {
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

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public List<String> getPlayerProfilePicturePaths() {
        return playerProfilePicturePaths;
    }

    public String getMapPath() {
        return mapPath;
    }
}
