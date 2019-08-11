package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.enemy.Debuff;
import de.diegrafen.exmatrikulatortd.model.tower.Aura;
import de.diegrafen.exmatrikulatortd.model.tower.Buff;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import java.util.LinkedList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.util.Assets.*;
import static de.diegrafen.exmatrikulatortd.util.Constants.*;

/**
 * Factory für Turm-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 26.06.2019 22:07
 */
public final class TowerFactory {

    private static final String REGULAR_TOWER_DESCRIPTION = "Standardturm" + "\n" + "Kosten: 200";

    private static final String SLOW_TOWER_DESCRIPTION = "Verlangsamungsturm\n" + "Verlangsamt Gegner" + "\n" + "Kosten: 500";

    private static final String CORRUPTION_TOWER_DESCRIPTION = "Verderbnisturm\n" + "Verringert die Ruestung von Gegnern" + "\n" + "Kosten: 300";

    private static final String EXPLOSIVE_TOWER_DESCRIPTION = "Explosivturm\n" + "Verursacht Flaechenschaden" + "\n" + "Kosten: 500";

    private static final String AURA_TOWER_DESCRIPTION = "Auraturm\n" + "Beschleunigt eigene Türme in der Naehe" + "\n" + "Kosten: 1000";

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
    public static Tower createNewTower(final int towerType) {

        Tower tower;

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
            default:
                tower = createRegularTower();
        }

        return tower;
    }

    /**
     * Erzeugt einen normalen Turm
     *
     * @return Der erzeugte normalen Turm
     */
    private static Tower createRegularTower() {
        return new Tower("Regular Tower", REGULAR_TOWER_DESCRIPTION, REGULAR_TOWER, 25, 4, 0.5f,
                NORMAL, 0.45f, new LinkedList<>(), 0, 200, 100, 1, 10,
                REGULAR_TOWER_ASSETS, REGULAR_TOWER_PORTRAIT, REGULAR_TOWER_PORTRAIT_SELECTED, 0, 0, new LinkedList<>(),
                true, 25, 1.05f, 0.125f, 0);
    }

    /**
     * Erzeugt einen Verlangsamungsturm
     *
     * @return Der erzeugte Verlangsamungsturm
     */
    private static Tower createSlowTower() {
        LinkedList<Aura> auras = new LinkedList<>();
        LinkedList<Debuff> debuffs = new LinkedList<>();

        Debuff slowDebuff = new Debuff("Slow-Effekt", AURA_REFRESH_RATE, -0.5f, 0.5f, 0, false);
        debuffs.add(slowDebuff);
        Aura slowAura = new Aura(debuffs, new LinkedList<>());
        auras.add(slowAura);

        return new Tower("Slowtower", SLOW_TOWER_DESCRIPTION, SLOW_TOWER, 10,
                4, 3, LOGIC, 0.15f, auras, 4, 500, 500,
                1, 3, SLOW_TOWER_ASSETS, SLOW_TOWER_PORTRAIT, SLOW_TOWER_PORTRAIT_SELECTED, 0, 0, debuffs, true,
                5, 1.05f, 0.25f, 0.25f);
    }


    /**
     * Erzeugt einen Corruption-Turm
     *
     * @return Der erzeugte Corruption-Turm
     */
    private static Tower createCorruptionTower() {
        List<Debuff> attackDebuffs = new LinkedList<>();
        Debuff corruptionDebuff = new Debuff("Corruption-Debuff", 3, -2, 1, -25, false);
        attackDebuffs.add(corruptionDebuff);

        return new Tower("Corruption Tower", CORRUPTION_TOWER_DESCRIPTION, CORRUPTION_TOWER, 100, 4, 1,
                PIERCING, 0.4f, new LinkedList<>(), 0, 300, 300, 1, 10,
                CORRUPTION_TOWER_ASSETS, CORRUPTION_TOWER_PORTRAIT, CORRUPTION_TOWER_PORTRAIT_SELECTED,
                0, 0, attackDebuffs, true, 50, 1.05f, 0.33f, 0);
    }

    /**
     * Erzeugt einen Explosiv-Turm
     *
     * @return Der erzeugte Explosiv-Turm
     */
    private static Tower createExplosiveTower() {
        return new Tower("Explosive Tower", EXPLOSIVE_TOWER_DESCRIPTION, EXPLOSIVE_TOWER, 200, 4, 3,
                EXPLOSIVE, 0.4f, new LinkedList<>(), 0, 500, 250, 500, 1, 10, EXPLOSIVE_TOWER_ASSETS, EXPLOSIVE_TOWER_PORTRAIT, EXPLOSIVE_TOWER_PORTRAIT_SELECTED,
                0.5f, 2f, new LinkedList<>(), "fireball", FIREBALL_ASSETS, 400, true, 100,
                1.03f, 0.25f,0);
    }

    /**
     * Erzeugt einen Aura-Turm
     *
     * @return Der erzeugte Aura-Turm
     */
    private static Tower createAuraTower() {
        LinkedList<Aura> auras = new LinkedList<>();
        List<Debuff> attackDebuffs = new LinkedList<>();
        LinkedList<Debuff> debuffs = new LinkedList<>();
        LinkedList<Buff> buffs = new LinkedList<>();

        Buff hasteBuff = new Buff("Haste-Buff", AURA_REFRESH_RATE, 1.2f, 1, false);
        buffs.add(hasteBuff);
        Aura hasteAura = new Aura(debuffs, buffs);

        auras.add(hasteAura);

        return new Tower("Aura Tower", AURA_TOWER_DESCRIPTION, AURA_TOWER, 10,
                4, 3, NORMAL, 0, auras,
                4, 1000, 1000, 1,
                3, AURA_TOWER_ASSETS, AURA_TOWER_PORTRAIT, AURA_TOWER_PORTRAIT_SELECTED, 0, 0, attackDebuffs, true,
                10, 1.05f,0.5f, 1.5f);
    }
}
