package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Aura;
import de.diegrafen.exmatrikulatortd.model.tower.Buff;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import static de.diegrafen.exmatrikulatortd.model.tower.AttackType.NORMAL;
import static de.diegrafen.exmatrikulatortd.util.Assets.REGULAR_TOWER_ASSETS;
import static de.diegrafen.exmatrikulatortd.util.Constants.TILE_SIZE;

/**
 * Factory für Turm-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 04:57
 */
public final class TowerFactory {

    public static final int REGULAR_TOWER = 0;

    public static final int SLOW_TOWER = 1;

    public static final int CORRUPTION_TOWER = 2;

    public static final int EXPLOSIVE_TOWER = 3;

    public static final int AURA_TOWER = 4;

    /**
     * Versteckter Konstruktor
     */
    private TowerFactory() {

    }

    /**
     * Erzeugt einen neuen Gegner
     *
     * @param towerType Der Typ des Turms
     * @return Der erzeugte Turm
     */
    public static Tower createNewTower(int towerType) {

        Tower tower = null;

        switch (towerType) {
            case REGULAR_TOWER:
                tower = createRegularTower();
                break;
            case SLOW_TOWER:
                tower = createSlowTower();
                break;
            case AURA_TOWER:
                tower = createAuraTower();
                break;
            case EXPLOSIVE_TOWER:
                tower = createExplosiveTower();
                break;
            case CORRUPTION_TOWER:
                tower = createCorruptionTower();
                break;
        }

        return tower;
    }

    /**
     * Erzeugt einen normalen Turm
     *
     * @return Der erzeugte normalen Turm
     */
    private static Tower createRegularTower() {
        //System.out.println("Ohai!");
        return new Tower("Regular Tower", 100, 2 * TILE_SIZE, 3, NORMAL, null, 0, 300, 150, 600, 1, REGULAR_TOWER_ASSETS);
    }

    /**
     * Erzeugt einen Verlangsamungsturm
     *
     * @return Der erzeugte Verlangsamungsturm
     */
    private static Tower createSlowTower() {
        Tower slowTower = new Tower();
        return null;
    }


    /**
     * Erzeugt einen Corruption-Turm
     *
     * @return Der erzeugte Corruption-Turm
     */
    private static Tower createCorruptionTower() {
        return null;
    }

    /**
     * Erzeugt einen Explosiv-Turm
     *
     * @return Der erzeugte Explosiv-Turm
     */
    private static Tower createExplosiveTower() {
        return null;
    }

    /**
     * Erzeugt einen Aura-Turm
     *
     * @return Der erzeugte Aura-Turm
     */
    private static Tower createAuraTower() {
        return null;
    }
}
