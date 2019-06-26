package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import static de.diegrafen.exmatrikulatortd.util.Assets.REGULAR_ENEMY_ASSETS;
import static de.diegrafen.exmatrikulatortd.util.Assets.HEAVY_ENEMY_ASSETS;

/**
 * Factory für Gegner
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:56
 */
public final class EnemyFactory {

    public static final int REGULAR_ENEMY = 0;

    public static final int HEAVY_ENEMY = 1;

    public static final int FAST_ENEMY = 2;

    public static final int BOSS_ENEMY = 3;

    /**
     * Versteckter Konstruktor
     */
    private EnemyFactory() {

    }

    /**
     * Erzeugt einen neuen Gegner
     *
     * @param enemyType Der Typ des Gegners
     * @return Der erzeugte Gegner
     */
    public static Enemy createNewEnemy(int enemyType) {

        Enemy enemy = null;

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
        }

        return enemy;
    }


    /**
     * Erzeugt einen normalen Gegner
     *
     * @return Der erzeugte normale Gegner
     */
    private static Enemy createRegularEnemy() {
        return new Enemy("Regular Enemy", 175, 100, 1, 50, 50, REGULAR_ENEMY_ASSETS, 0, 0, 10);
    }

    /**
     * Erzeugt einen gepanzerten Gegner
     *
     * @return Der erzeugte gepanzerte Gegner
     */
    private static Enemy createHeavyEnemy() {
        return new Enemy("Heavy Enemy", 175, 150, 2, 100, 100, HEAVY_ENEMY_ASSETS, 0, 0, 20);
    }


    /**
     * Erzeugt einen schnellen Gegner
     *
     * @return Der erzeugte schnelle Gegner
     */
    private static Enemy createFastEnemy() {
        return null;
    }

    /**
     * Erzeugt einen Boss-Gegner
     *
     * @return Der erzeugte Boss-Gegner
     */
    private static Enemy createBossEnemy() {
        return null;
    }

}