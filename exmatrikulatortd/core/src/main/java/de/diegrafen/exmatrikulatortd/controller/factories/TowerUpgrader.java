package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import static de.diegrafen.exmatrikulatortd.util.Assets.EXPLOSIVE_TOWER_ASSETS;
import static de.diegrafen.exmatrikulatortd.util.Assets.UPGRADED_REGULAR_TOWER_ASSETS;
import static de.diegrafen.exmatrikulatortd.util.Constants.*;

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

    public static void upgradeTower(Tower tower) {

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

    }

    //TODO: Upgrade-System muss unbedingt verbessert werden.

    private static boolean upgradeRegularTower(Tower tower) {

        boolean successful = true;

        if (tower.getMaxUpgradeLevel() <= tower.getUpgradeLevel()) {
            successful = false;
        } else {
            tower.setUpgradeLevel(tower.getUpgradeLevel() + 1);
            tower.setBaseAttackDamage(tower.getBaseAttackDamage() + 25); // tower.getAttackDamageUpgradeBonus()
            //tower.setUpgradePrice(tower.getUpgradePrice() * 2);  // tower.getAttackSpeedUpgradeBonus()
            tower.setSellPrice(tower.getSellPrice() + 100);        // tower.getAttackRangeUpgradeBonus()
            tower.setAssetsName(UPGRADED_REGULAR_TOWER_ASSETS);
            tower.notifyObserver();
            System.out.println("Upgraded!");
        }

        return successful;
    }

    private static boolean upgradeSlowTower(Tower tower) {
        boolean successful = true;

        if (tower.getMaxUpgradeLevel() <= tower.getUpgradeLevel()) {
            successful = false;
        } else {
            tower.setUpgradeLevel(tower.getUpgradeLevel() + 1);
            tower.setBaseAttackDamage(tower.getBaseAttackDamage() + 10);
            //tower.setUpgradePrice(tower.getUpgradePrice() * 2);
            tower.setSellPrice(tower.getSellPrice() + 250);
            tower.notifyObserver();
            System.out.println("Upgraded!");
        }

        return successful;
    }

    private static boolean upgradeAuraTower(Tower tower) {
        boolean successful = true;

        if (tower.getMaxUpgradeLevel() <= tower.getUpgradeLevel()) {
            successful = false;
        } else {
            tower.setUpgradeLevel(tower.getUpgradeLevel() + 1);
            tower.setBaseAttackDamage(tower.getBaseAttackDamage() + 10);
            //tower.setUpgradePrice(tower.getUpgradePrice() * 2);
            tower.setSellPrice(tower.getSellPrice() + 500);
            tower.notifyObserver();
            System.out.println("Upgraded!");
        }

        return successful;
    }

    private static boolean upgradeExplosiveTower(Tower tower) {
        boolean successful = true;

        if (tower.getMaxUpgradeLevel() <= tower.getUpgradeLevel()) {
            successful = false;
        } else {
            tower.setUpgradeLevel(tower.getUpgradeLevel() + 1);
            tower.setBaseAttackDamage(tower.getBaseAttackDamage() + 200);
            //tower.setUpgradePrice(tower.getUpgradePrice() * 2);
            tower.setSellPrice(tower.getSellPrice() + 250);
            tower.notifyObserver();
            System.out.println("Upgraded!");
        }

        return successful;
    }

    private static boolean upgradeCorruptionTower(Tower tower) {
        boolean successful = true;

        if (tower.getMaxUpgradeLevel() <= tower.getUpgradeLevel()) {
            successful = false;
        } else {
            tower.setUpgradeLevel(tower.getUpgradeLevel() + 1);
            tower.setBaseAttackDamage(tower.getBaseAttackDamage() + 100);
            //tower.setUpgradePrice(tower.getUpgradePrice() * 2);
            tower.setSellPrice(tower.getSellPrice() + 150);
            tower.notifyObserver();
            System.out.println("Upgraded!");
        }

        return successful;
    }

}
