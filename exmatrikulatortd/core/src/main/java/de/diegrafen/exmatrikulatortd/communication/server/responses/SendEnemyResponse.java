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

    private int enemyType;

    private int playerToSendTo;

    private int sendingPlayer;

    public SendEnemyResponse() {
    }

    public SendEnemyResponse(int enemyType, int playerToSendTo, int sendingPlayer) {
        this.enemyType = enemyType;
        this.playerToSendTo = playerToSendTo;
        this.sendingPlayer = sendingPlayer;
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
