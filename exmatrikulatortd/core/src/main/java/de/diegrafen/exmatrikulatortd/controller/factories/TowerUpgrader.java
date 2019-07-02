package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.*;

/**
 * Stellt statische Methoden für das Aufrüsten von Türmen zur Verfügung
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 26.06.2019 22:07
 */
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

        boolean successful = true;

        if (tower.getMaxUpgradeLevel() >= tower.getUpgradeLevel()) {
            successful = false;
        } else {
            tower.setUpgradeLevel(tower.getUpgradeLevel() + 1);
            tower.setBaseAttackDamage(tower.getBaseAttackDamage()* 2);
            tower.setUpgradePrice(tower.getUpgradePrice() * 2);
            tower.setSellPrice(tower.getSellPrice() * 2);
        }

        return successful;
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
