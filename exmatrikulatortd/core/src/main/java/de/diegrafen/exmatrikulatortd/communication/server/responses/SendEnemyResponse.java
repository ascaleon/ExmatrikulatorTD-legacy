package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.communication.client.requests.SendEnemyRequest;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 16:27
 */
public class SendEnemyResponse extends Response {

    private boolean successful;

    private int enemyType;

    private int playerToSendTo;

    private int sendingPlayer;

    public SendEnemyResponse(boolean successful) {
        this.successful = successful;
    }

    public SendEnemyResponse(boolean successful, int enemyType, int playerToSendTo, int sendingPlayer) {
        this.successful = successful;
        this.enemyType = enemyType;
        this.playerToSendTo = playerToSendTo;
        this.sendingPlayer = sendingPlayer;
    }

    public boolean wasSuccessful() {
        return successful;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public int getPlayerToSendTo() {
        return playerToSendTo;
    }

    public int getSendingPlayer() {
        return sendingPlayer;
    }
}
