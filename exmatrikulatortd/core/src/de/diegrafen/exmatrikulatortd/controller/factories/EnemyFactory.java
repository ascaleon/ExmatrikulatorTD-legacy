package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import static de.diegrafen.exmatrikulatortd.util.Assets.REGULAR_ENEMY_ASSETS;

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
    public static Enemy createNewEnemy (EnemyType enemyType) {

        Enemy enemy;

        switch (enemyType) {
            case REGULAR_ENEMY:
                enemy = createRegularEnemy();
                break;
            case HEAVY_ENEMY:
                enemy = createHeavyEnemy();
                break;
            case FAST_ENEMY:
                enemy = createFastEnemy();
                break;
            case BOSS_ENEMY:
                enemy = createBossEnemy();
                break;
            default:
                enemy = createRegularEnemy();
        }

        return enemy;
    }


    /**
     * Erzeugt einen normalen Gegner
     * @return Der erzeugte normale Gegner
     */
    private static Enemy createRegularEnemy () {
        return new Enemy("Regular Enemy", 10, 100, 1, 50, 50, REGULAR_ENEMY_ASSETS, 0, 0);
    }

    /**
     * Erzeugt einen gepanzerten Gegner
     * @return Der erzeugte gepanzerte Gegner
     */
    private static Enemy createHeavyEnemy () {
        return null;
    }

    /**
     * Erzeugt einen schnellen Gegner
     * @return Der erzeugte schnelle Gegner
     */
    private static Enemy createFastEnemy () {
        return null;
    }

    /**
     * Erzeugt einen Boss-Gegner
     * @return Der erzeugte Boss-Gegner
     */
    private static Enemy createBossEnemy () {
        return null;
    }

}
