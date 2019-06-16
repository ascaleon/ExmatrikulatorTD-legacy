package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

/**
 *
 * Factory für Gegner
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:56
 */
public final class EnemyFactory {

    /**
     * Versteckter Konstruktor
     */
    private EnemyFactory () {

    }

    /**
     * Die verschiedenen Gegner-Typen (normal, gepanzert, schnell, Boss)
     */
    public enum EnemyType {
        REGULAR_ENEMY, HEAVY_ENEMY, FAST_ENEMY, BOSS_ENEMY
    }

    /**
     * Erzeugt einen neuen Gegner
     * @param enemyType Der Typ des Gegners
     * @return Der erzeugte Gegner
     */
    public static Enemy createEnemy (EnemyType enemyType) {
        return null;
    }


    /**
     * Erzeugt einen normalen Gegner
     * @return Der erzeugte normale Gegner
     */
    private Enemy createRegularEnemy () {
        return null;
    }

    /**
     * Erzeugt einen gepanzerten Gegner
     * @return Der erzeugte gepanzerte Gegner
     */
    private Enemy createHeavyEnemy () {
        return null;
    }

    /**
     * Erzeugt einen schnellen Gegner
     * @return Der erzeugte schnelle Gegner
     */
    private Enemy createFastEnemy () {
        return null;
    }

    /**
     * Erzeugt einen Boss-Gegner
     * @return Der erzeugte Boss-Gegner
     */
    private Enemy createBossEnemy () {
        return null;
    }

}
