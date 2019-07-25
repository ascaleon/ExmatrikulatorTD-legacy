package de.diegrafen.exmatrikulatortd.communication.client.requests;

import de.diegrafen.exmatrikulatortd.model.Difficulty;

public class GetGameInfoRequest extends Request {

    private String playerName;

    private Difficulty difficulty;

    private String profilePicturePath;

    public GetGameInfoRequest() {
        super();
    }

    public GetGameInfoRequest(String playerName, Difficulty difficulty, String profilePicturePath) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.profilePicturePath = profilePicturePath;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }
}
