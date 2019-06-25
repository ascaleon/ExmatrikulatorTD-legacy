package de.diegrafen.exmatrikulatortd.communication.client.requests;

import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.*;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:33
 */
public class SendEnemyRequest extends Request {

    private EnemyType enemyType;

    public SendEnemyRequest(EnemyType enemyType) {
        super();
        this.enemyType = enemyType;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }
}
