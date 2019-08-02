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

    public static final int REGULAR_TOWER = 0;

    public static final String REGULAR_TOWER_DESCRIPTION = "Standardturm";

    public static final int SLOW_TOWER = 1;

    public static final String SLOW_TOWER_DESCRIPTION = "Verlangsamt Gegner";

    public static final int CORRUPTION_TOWER = 2;

    public static final String CORRUPTION_TOWER_DESCRIPTION = "Verringert die Ruestung der Gegner";

    public static final int EXPLOSIVE_TOWER = 3;

    public static final String EXPLOSIVE_TOWER_DESCRIPTION = "Verursacht Flaechenschaden";

    public static final int AURA_TOWER = 4;

    public static final String AURA_TOWER_DESCRIPTION = "Aura verbessert Tuerme in der Naehe";
    

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
    public static Tower createNewTower(final int towerType, final int tileWidth, final int tileHeight) {

        Tower tower = null;

        switch (towerType) {
            case REGULAR_TOWER:
                tower = createRegularTower(tileWidth, tileHeight);
                break;
            case SLOW_TOWER:
                tower = createSlowTower(tileWidth, tileHeight);
                break;
            case AURA_TOWER:
                tower = createAuraTower(tileWidth, tileHeight);
                break;
            case EXPLOSIVE_TOWER:
                tower = createExplosiveTower(tileWidth, tileHeight);
                break;
            case CORRUPTION_TOWER:
                tower = createCorruptionTower(tileWidth, tileHeight);
                break;
        }

        return tower;
    }

    /**
     * Erzeugt einen normalen Turm
     *
     * @return Der erzeugte normalen Turm
     */
    private static Tower createRegularTower(int tileWidth, int tileHeight) {
        LinkedList<Aura> auras = new LinkedList<>();
        List<Debuff> attackDebuffs = new LinkedList<>();
        LinkedList<Debuff> debuffs = new LinkedList<>();

        Debuff slowDebuff = new Debuff("Slow-Effekt", AURA_REFRESH_RATE, -0.5f, 0.5f, 0, false);
        Aura slowAura = new Aura(200, debuffs, new LinkedList<>());
        Debuff frostDebuff = new Debuff("Frost-Debuff", 3, -5, 0.5f, -50, false);
        
        debuffs.add(slowDebuff);
        auras.add(slowAura);
        attackDebuffs.add(frostDebuff);

        int averageTileSize = (tileWidth + tileHeight) / 2;
        int range = averageTileSize * 4;

        // Konstruktor für den AttackType-Immediate
        return new Tower("Regular Tower", REGULAR_TOWER_DESCRIPTION, REGULAR_TOWER, 50, range, 0.5f,
                NORMAL, 0.45f, new LinkedList<>(), 0, 300, 150, 600, 1, 2,
                REGULAR_TOWER_ASSETS, 0, 0, new LinkedList<>(),
                tileWidth, tileHeight);
        //return new Tower("Regular Tower", REGULAR_TOWER_DESCRIPTION, REGULAR_TOWER, 300, 2 * TILE_SIZE, 2.0f,
        //        NORMAL, auras, 0, 300, 150, 600, 1, 10, REGULAR_TOWER_ASSETS, 0.5f, 100, attackDebuffs);
    }

    /**
     * Erzeugt einen Verlangsamungsturm
     *
     * @return Der erzeugte Verlangsamungsturm
     */
    private static Tower createSlowTower(final int tileWidth, final int tileHeight) {
        LinkedList<Aura> auras = new LinkedList<>();
        List<Debuff> attackDebuffs = new LinkedList<>();
        LinkedList<Debuff> debuffs = new LinkedList<>();

        Debuff slowDebuff = new Debuff("Slow-Effekt", AURA_REFRESH_RATE, -0.5f, 0.5f, 0, false);
        Aura slowAura = new Aura(200, debuffs, new LinkedList<>());
        Debuff frostDebuff = new Debuff("Frost-Debuff", 3, -5, 0.5f, -50, false);

        debuffs.add(slowDebuff);
        auras.add(slowAura);
        attackDebuffs.add(frostDebuff);

        return new Tower("Slowtower", SLOW_TOWER_DESCRIPTION, SLOW_TOWER, 50,
                4 * TILE_SIZE, 2, PIERCING, 0.15f,auras, 0, 300, 150, 600,
                1, 2, SLOW_TOWER_ASSETS, 0, 0, new LinkedList<>(), tileWidth, tileHeight);
    }


    /**
     * Erzeugt einen Corruption-Turm
     *
     * @return Der erzeugte Corruption-Turm
     */
    private static Tower createCorruptionTower(final int tileWidth, final int tileHeight) {
        LinkedList<Aura> auras = new LinkedList<>();
        List<Debuff> attackDebuffs = new LinkedList<>();
        LinkedList<Debuff> debuffs = new LinkedList<>();

        Debuff slowDebuff = new Debuff("Slow-Effekt", AURA_REFRESH_RATE, -0.5f, 0.5f, 0, false);
        Aura slowAura = new Aura(200, debuffs, new LinkedList<>());
        Debuff frostDebuff = new Debuff("Frost-Debuff", 3, -5, 0.5f, -50, false);

        debuffs.add(slowDebuff);
        auras.add(slowAura);
        attackDebuffs.add(frostDebuff);

        return new Tower("Corruption Tower", CORRUPTION_TOWER_DESCRIPTION, CORRUPTION_TOWER, 100, 4 * TILE_SIZE, 1,
                LOGIC, 0.4f, new LinkedList<>(), 0, 300, 150, 600, 1, 2, CORRUPTION_TOWER_ASSETS,
                0, 0, attackDebuffs, tileWidth, tileHeight);
    }

    /**
     * Erzeugt einen Explosiv-Turm
     *
     * @return Der erzeugte Explosiv-Turm
     */
    private static Tower createExplosiveTower(final int tileWidth, final int tileHeight) {
        LinkedList<Aura> auras = new LinkedList<>();
        List<Debuff> attackDebuffs = new LinkedList<>();
        LinkedList<Debuff> debuffs = new LinkedList<>();

        Debuff slowDebuff = new Debuff("Slow-Effekt", AURA_REFRESH_RATE, -0.5f, 0.5f, 0, false);
        Aura slowAura = new Aura(200, debuffs, new LinkedList<>());
        Debuff frostDebuff = new Debuff("Frost-Debuff", 3, -5, 0.5f, -50, false);

        debuffs.add(slowDebuff);
        auras.add(slowAura);
        attackDebuffs.add(frostDebuff);

        return new Tower("Explosive Tower", EXPLOSIVE_TOWER_DESCRIPTION, EXPLOSIVE_TOWER, 200, 4 * TILE_SIZE, 3,
                EXPLOSIVE, 0.4f, new LinkedList<>(), 0, 300, 150, 600, 1, 2, EXPLOSIVE_TOWER_ASSETS,
                0.5f, 100, new LinkedList<>(), "fireball", FIREBALL_ASSETS, 400, tileWidth, tileHeight);
    }

    /**
     * Erzeugt einen Aura-Turm
     *
     * @return Der erzeugte Aura-Turm
     */
    private static Tower createAuraTower(final int tileWidth, final int tileHeight) {
        LinkedList<Aura> auras = new LinkedList<>();
        List<Debuff> attackDebuffs = new LinkedList<>();
        LinkedList<Debuff> debuffs = new LinkedList<>();
        LinkedList<Buff> buffs = new LinkedList<>();

        Buff hasteBuff = new Buff("Haste-Buff", AURA_REFRESH_RATE, 4f, 1, false);
        buffs.add(hasteBuff);
        Aura hasteAura = new Aura(200, debuffs, buffs);

        auras.add(hasteAura);

        return new Tower("Aura Tower", AURA_TOWER_DESCRIPTION, AURA_TOWER, 100,
                2 * TILE_SIZE, 3, PIERCING, 0, auras,
                4 * TILE_SIZE, 300, 150, 600, 1,
                3, AURA_TOWER_ASSETS, 0.5f, 100, attackDebuffs,
                tileWidth, tileHeight);
    }
}
