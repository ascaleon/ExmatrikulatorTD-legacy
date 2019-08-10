package de.diegrafen.exmatrikulatortd.communication.client.requests;

/**
 * @author janro
 * @version 10.08.2019 19:04
 */
public class SendPlayerNameRequest extends Request {

    private String playerName;

    public SendPlayerNameRequest(String playerName) {
        super();
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
