package de.diegrafen.exmatrikulatortd.communication.client.requests;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:33
 */
public class SendEnemyRequest extends Request {

    private Enemy enemy;

    public SendEnemyRequest(Enemy enemy) {
        this.enemy = enemy;
    }
}
