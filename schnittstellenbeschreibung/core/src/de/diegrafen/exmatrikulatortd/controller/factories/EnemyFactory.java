package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:56
 */
public final class EnemyFactory {

    private EnemyFactory () {

    }

    public enum EnemyType {
        REGULAR_ENEMY, HEAVY_ENEMY, FAST_ENEMY, BOSS_ENEMY
    }

    public static Enemy createEnemy (EnemyType enemyType) {
        return null;
    }


    private Enemy createRegularEnemy () {
        return null;
    }

    private Enemy createHeavyEnemy () {
        return null;
    }

    private Enemy createFastEnemy () {
        return null;
    }

    private Enemy createBossEnemy () {
        return null;
    }

}
