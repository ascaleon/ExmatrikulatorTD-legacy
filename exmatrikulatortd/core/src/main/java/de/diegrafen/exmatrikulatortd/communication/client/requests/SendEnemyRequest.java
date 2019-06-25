package de.diegrafen.exmatrikulatortd.communication.client.requests;

import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:33
 */
public class SendEnemyRequest extends Request {

    private EnemyFactory.EnemyType enemyType;

    public SendEnemyRequest(EnemyFactory.EnemyType enemyType) {
        super();
        this.enemyType = enemyType;
    }
}
