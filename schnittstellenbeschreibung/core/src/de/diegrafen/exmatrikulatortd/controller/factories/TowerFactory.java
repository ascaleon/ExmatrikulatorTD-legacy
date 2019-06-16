package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.tower.Aura;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 04:57
 */
public final class TowerFactory {

    private TowerFactory () {

    }

    public enum TowerType {

        SLOW_TOWER, REGULAR_TOWER, CORRUPTION_TOWER, EXPLOSIVE_TOWER, AURA_TOWER
    }

    public static Tower createNewTower (TowerType towerType) {
        return null;
    }

    private Tower createSlowTower () {
        return null;
    }

    private Tower createRegularTower () {
        return null;
    }

    private Tower createCorruptionTower () {
        return null;
    }

    private Tower createExplosiveTower () {
        return null;
    }

    private Tower createAuraTower () {
        return null;
    }
}
