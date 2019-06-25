package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.communication.client.requests.SendEnemyRequest;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 16:27
 */
public class SendEnemyResponse extends Response {

    private boolean successful;

    private Enemy enemy;

    public SendEnemyResponse(boolean successful, Enemy enemy) {
        super();
        this.successful = successful;
        this.enemy = enemy;
    }

    public boolean wasSuccessful() {
        return successful;
    }

    public Enemy getEnemy() {
        return enemy;
    }
}
