package de.diegrafen.exmatrikulatortd.communication.client.requests;

public class GetGameInfoRequest extends Request {

    private String playerName;

    private String difficulty;

    private String profilePicturePath;

    public GetGameInfoRequest() {
    }

    public GetGameInfoRequest(String playerName, String difficulty, String profilePicturePath) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.profilePicturePath = profilePicturePath;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }
}
