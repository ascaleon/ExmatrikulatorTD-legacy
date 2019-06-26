package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.*;

public final class TowerUpgrader {

    /**
     * Versteckter Konstruktor
     */
    private TowerUpgrader() {

    }

    public static boolean upgradeTower(Tower tower) {

        int towerType = tower.getTowerType();

        boolean successful = false;

        switch (towerType) {
            case REGULAR_TOWER:
                successful = upgradeRegularTower(tower);
                break;
            case SLOW_TOWER:
                successful = upgradeSlowTower(tower);
                break;
            case AURA_TOWER:
                successful = upgradeAuraTower(tower);
                break;
            case EXPLOSIVE_TOWER:
                successful = upgradeExplosiveTower(tower);
                break;
            case CORRUPTION_TOWER:
                successful = upgradeCorruptionTower(tower);
                break;
        }

        if (successful) {
            tower.notifyObserver();
        }

        return successful;
    }

    private static boolean upgradeRegularTower(Tower tower) {
        return false;
    }

    private static boolean upgradeSlowTower(Tower tower) {
        return false;
    }

    private static boolean upgradeAuraTower(Tower tower) {
        return false;
    }

    private static boolean upgradeExplosiveTower(Tower tower) {
        return false;
    }

    private static boolean upgradeCorruptionTower(Tower tower) {
        return false;
    }

}
