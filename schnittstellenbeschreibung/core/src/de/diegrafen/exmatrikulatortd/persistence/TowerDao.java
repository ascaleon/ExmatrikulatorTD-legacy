package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:50
 */
public class TowerDao extends BaseDao<Tower> {

    @Override
    Class<Tower> getClazz() {
        return null;
    }
}
