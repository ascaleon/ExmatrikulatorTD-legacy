package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.tower.Aura;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 *
 * Factory für Turm-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 04:57
 */
public final class TowerFactory {

    /**
     * Versteckter Konstruktor
     */
    private TowerFactory () {

    }

    /**
     * Die verschiedenen Turm-Typen
     */
    public enum TowerType {

        SLOW_TOWER, REGULAR_TOWER, CORRUPTION_TOWER, EXPLOSIVE_TOWER, AURA_TOWER
    }


    /**
     * Erzeugt einen neuen Gegner
     * @param towerType Der Typ des Turms
     * @return Der erzeugte Turm
     */
    public static Tower createNewTower (TowerType towerType) {
        return null;
    }

    /**
     * Erzeugt einen Verlangsamungsturm
     * @return Der erzeugte Verlangsamungsturm
     */
    private Tower createSlowTower () {
        return null;
    }

    /**
     * Erzeugt einen normalen Turm
     * @return Der erzeugte normalen Turm
     */
    private Tower createRegularTower () {
        return null;
    }

    /**
     * Erzeugt einen Corruption-Turm
     * @return Der erzeugte Corruption-Turm
     */
    private Tower createCorruptionTower () {
        return null;
    }

    /**
     * Erzeugt einen Explosiv-Turm
     * @return Der erzeugte Explosiv-Turm
     */
    private Tower createExplosiveTower () {
        return null;
    }

    /**
     * Erzeugt einen Aura-Turm
     * @return Der erzeugte Aura-Turm
     */
    private Tower createAuraTower () {
        return null;
    }
}
