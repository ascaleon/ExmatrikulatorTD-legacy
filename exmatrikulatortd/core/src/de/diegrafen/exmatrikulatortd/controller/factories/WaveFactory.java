package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.EnemyType.*;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.createNewEnemy;

/**
 *
 * Factory für Gegner-Wellen
 *
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:01
 */
public final class WaveFactory {

    private static int enemiesPerWave = 10;


    /**
     * Versteckter Konstruktor
     */
    private WaveFactory () {

    }

    /**
     * Die verschiedenen Wellentypen
     */
    public enum WaveType {

        REGULAR_WAVE, HEAVY_WAVE, FAST_WAVE, REGULAR_AND_HEAVY_WAVE, REGULAR_AND_FAST_WAVE, HEAVY_AND_FAST_WAVE,
        BOSS_WAVE
    }

    /**
     * Erzeugt eine neue Welle des angegebenen Typs
     * @param waveType Der Wellentyp
     * @return Die neue Welle
     */
    public static Wave createWave (WaveType waveType) {

        Wave wave = new Wave();

        switch (waveType) {
            case REGULAR_WAVE:
                wave = createRegularWave();
                break;
            case HEAVY_WAVE:
                wave = createHeavyWave();
                break;
            case FAST_WAVE:
                wave = createFastWave();
                break;
            case REGULAR_AND_HEAVY_WAVE:
                wave = createRegularAndHeavyWave();
                break;
            case REGULAR_AND_FAST_WAVE:
                wave = createRegularAndFastWave();
                break;
            case HEAVY_AND_FAST_WAVE:
                wave = createHeavyAndFastWave();
                break;
            case BOSS_WAVE:
                wave = createBossWave();
                break;
            default:
                wave = createRegularWave();
                break;
        }

        return wave;
    }


    /**
     * Erzeugt eine Welle von normalen Gegnern
     * @return Die erzeugte Welle
     */
    private static Wave createRegularWave ( ) {
        Wave wave = new Wave();
        for (int i = 0; i < enemiesPerWave; i++) {
            wave.addEnemy(createNewEnemy(REGULAR_ENEMY));
        }
        return wave;
    }

    /**
     * Erzeugt eine Welle von schwer gapanzerten Gegnern
     * @return Die erzeugte Welle
     */
    private static Wave createHeavyWave () {
        Wave wave = new Wave();
        for (int i = 0; i < enemiesPerWave; i++) {
            wave.addEnemy(createNewEnemy(HEAVY_ENEMY));
        }
        return wave;
    }

    /**
     * Erzeugt eine Welle von schwer schnellen Gegnern
     * @return Die erzeugte Welle
     */
    private static Wave createFastWave () {
        Wave wave = new Wave();
        for (int i = 0; i < enemiesPerWave; i++) {
            wave.addEnemy(createNewEnemy(REGULAR_ENEMY));
        }
        return wave;
    }

    /**
     * Erzeugt eine Welle von normalen und schwer gapanzerten Gegnern
     * @return Die erzeugte Welle
     */
    private static Wave createRegularAndHeavyWave () {
        Wave wave = new Wave();
        List<Enemy> waveEnemies = new ArrayList<Enemy>();
        for (int i = 0; i < enemiesPerWave / 2; i++) {
            waveEnemies.add(createNewEnemy(REGULAR_ENEMY));
        }
        for (int i = 0; i < enemiesPerWave / 2; i++) {
            waveEnemies.add(createNewEnemy(HEAVY_ENEMY));
        }
        Collections.shuffle(waveEnemies);
        wave.setEnemies(waveEnemies);
        return wave;
    }

    /**
     * Erzeugt eine Welle von normalen und schnellen Gegnern
     * @return Die erzeugte Welle
     */
    private static Wave createRegularAndFastWave () {
        Wave wave = new Wave();
        for (int i = 0; i < enemiesPerWave / 2; i++) {
            wave.addEnemy(createNewEnemy(REGULAR_ENEMY));
        }
        for (int i = 0; i < enemiesPerWave / 2; i++) {
            wave.addEnemy(createNewEnemy(FAST_ENEMY));
        }
        return wave;
    }

    /**
     * Erzeugt eine Welle von schwer gepanzerten und schnellen Gegnern
     * @return Die erzeugte Welle
     */
    private static Wave createHeavyAndFastWave () {
        Wave wave = new Wave();
        for (int i = 0; i < enemiesPerWave / 2; i++) {
            wave.addEnemy(createNewEnemy(REGULAR_ENEMY));
        }
        for (int i = 0; i < enemiesPerWave / 2; i++) {
            wave.addEnemy(createNewEnemy(FAST_ENEMY));
        }
        return wave;
    }

    /**
     * Erzeugt eine Boss-Welle
     * @return Die erzeugte Welle
     */
    private static Wave createBossWave () {
        Wave wave = new Wave();
        wave.addEnemy(createNewEnemy(BOSS_ENEMY));
        return wave;
    }

}
