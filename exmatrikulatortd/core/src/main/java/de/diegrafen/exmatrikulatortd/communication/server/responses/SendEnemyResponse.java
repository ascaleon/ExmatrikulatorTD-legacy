package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.communication.client.requests.SendEnemyRequest;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.EnemyType;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 16:27
 */
public class SendEnemyResponse extends Response {

    private boolean successful;

    private EnemyType enemyType;

    public SendEnemyResponse(boolean successful, EnemyType enemyType) {
        super();
        this.successful = successful;
        this.enemyType = enemyType;
    }

    public SendEnemyResponse(boolean successful) {
        this.successful = successful;
    }

    public boolean wasSuccessful() {
        return successful;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }
}
