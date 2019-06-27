package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Aura;
import de.diegrafen.exmatrikulatortd.model.tower.Buff;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import static de.diegrafen.exmatrikulatortd.model.tower.AttackType.*;
import static de.diegrafen.exmatrikulatortd.model.tower.Aura.CORRUPTION_AURA;
import static de.diegrafen.exmatrikulatortd.model.tower.Aura.HASTE_AURA;
import static de.diegrafen.exmatrikulatortd.model.tower.Aura.SLOW_AURA;
import static de.diegrafen.exmatrikulatortd.util.Assets.*;
import static de.diegrafen.exmatrikulatortd.util.Constants.TILE_SIZE;

/**
 * Factory für Turm-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 26.06.2019 22:07
 */
public final class TowerFactory {

    public static final int REGULAR_TOWER = 0;

    public static final String REGULAR_TOWER_DESCRIPTION = "Your friendly neighbourhood Standardturm.";

    public static final int SLOW_TOWER = 1;

    public static final String SLOW_TOWER_DESCRIPTION = "Verlangsamt Gegner durch extrem lange Ladezeiten und Abstürze.";

    public static final int CORRUPTION_TOWER = 2;

    public static final String CORRUPTION_TOWER_DESCRIPTION = "Lädt keine Vorlesungsfolien hoch und nimmt Studierenden so die Deckung.";

    public static final int EXPLOSIVE_TOWER = 3;

    public static final String EXPLOSIVE_TOWER_DESCRIPTION = "Zwingt mehrere Studierende zur Gruppenarbeit.";

    public static final int AURA_TOWER = 4;

    public static final String AURA_TOWER_DESCRIPTION = "Motiviert andere Lehrende in der Umgebung, mehr Stoff in der Prüfung dranzunehmen.";

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
        return new Tower("Regular Tower", REGULAR_TOWER_DESCRIPTION, REGULAR_TOWER, 100, 2 * TILE_SIZE, 3, NORMAL, null, 0, 300, 150, 600, 1, 10, REGULAR_TOWER_ASSETS);
    }

    /**
     * Erzeugt einen Verlangsamungsturm
     *
     * @return Der erzeugte Verlangsamungsturm
     */
    private static Tower createSlowTower() {
        return new Tower("Slowtower", SLOW_TOWER_DESCRIPTION, SLOW_TOWER, 100, 2 * TILE_SIZE, 3, FROST, SLOW_AURA, 0, 300, 150, 600, 1, 3, SLOW_TOWER_ASSETS);
    }


    /**
     * Erzeugt einen Corruption-Turm
     *
     * @return Der erzeugte Corruption-Turm
     */
    private static Tower createCorruptionTower() {
        return new Tower("Corruption Tower", CORRUPTION_TOWER_DESCRIPTION, CORRUPTION_TOWER, 100, 2 * TILE_SIZE, 3, UNHOLY, CORRUPTION_AURA, 0, 300, 150, 600, 1, 5, CORRUPTION_TOWER_ASSETS);
    }

    /**
     * Erzeugt einen Explosiv-Turm
     *
     * @return Der erzeugte Explosiv-Turm
     */
    private static Tower createExplosiveTower() {
        return new Tower("Explosive Tower", EXPLOSIVE_TOWER_DESCRIPTION, EXPLOSIVE_TOWER, 100, 2 * TILE_SIZE, 3, EXPLOSIVE, null, 0, 300, 150, 600, 1, 5, EXPLOSIVE_TOWER_ASSETS);
    }

    /**
     * Erzeugt einen Aura-Turm
     *
     * @return Der erzeugte Aura-Turm
     */
    private static Tower createAuraTower() {
        return new Tower("Aura Tower", AURA_TOWER_DESCRIPTION, AURA_TOWER, 100, 2 * TILE_SIZE, 3, PIERCING, HASTE_AURA, 4 * TILE_SIZE, 300, 150, 600, 1, 3, AURA_TOWER_ASSETS);
    }
}
