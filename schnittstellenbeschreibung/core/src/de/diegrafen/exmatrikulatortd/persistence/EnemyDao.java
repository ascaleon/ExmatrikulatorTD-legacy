package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:50
 */
public class EnemyDao extends BaseDao<Enemy> {
    @Override
    Class<Enemy> getClazz() {
        return null;
    }
}
