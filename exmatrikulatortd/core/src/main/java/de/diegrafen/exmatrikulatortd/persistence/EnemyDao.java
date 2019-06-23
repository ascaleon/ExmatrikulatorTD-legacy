package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

/**
 *
 * DAO-Klasse für Enemy-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:50
 */
public class EnemyDao extends BaseDao<Enemy> {

    /**
     * Gibt die Enemy-Klasse zurück
     * @return Der Klassen-Typ
     */
    @Override
    Class<Enemy> getClazz() {
        return Enemy.class;
    }
}
