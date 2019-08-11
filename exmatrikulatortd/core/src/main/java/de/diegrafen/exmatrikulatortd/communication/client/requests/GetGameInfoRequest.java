package de.diegrafen.exmatrikulatortd.communication.client.requests;

public class GetGameInfoRequest extends Request {

    private String playerName;

    private int difficulty;

    private String profilePicturePath;

    public GetGameInfoRequest() {
        super();
    }

    public GetGameInfoRequest(String playerName, int difficulty, String profilePicturePath) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.profilePicturePath = profilePicturePath;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }
}
