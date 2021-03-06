package de.diegrafen.exmatrikulatortd.communication.client.requests;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:33
 */
public class SendEnemyRequest extends Request {

    private int enemyType;

    private int playerToSendTo;

    private int sendingPlayer;

    public SendEnemyRequest() {
        super();
    }

    public SendEnemyRequest(int enemyType, int playerToSendTo, int sendingPlayer) {
        this.enemyType = enemyType;
        this.playerToSendTo = playerToSendTo;
        this.sendingPlayer = sendingPlayer;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public int getPlayerToSendTo() {
        return this.playerToSendTo;
    }

    public int getSendingPlayer() {
        return this.sendingPlayer;
    }
}
