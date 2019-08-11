package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.enemy.Wave;

import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.*;
import static de.diegrafen.exmatrikulatortd.util.Constants.*;

/**
 * Factory für Gegner-Wellen
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:01
 */
final class WaveFactory {

    /**
     * Versteckter Konstruktor
     */
    private WaveFactory() {

    }

    /**
     * Erzeugt eine neue Welle des angegebenen Typs
     *
     * @param waveType Der Wellentyp
     * @return Die neue Welle
     */
    static Wave createWave(int waveType) {

        Wave wave = null;

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
        }

        return wave;
    }


    /**
     * Erzeugt eine Welle von normalen Gegnern
     *
     * @return Die erzeugte Welle
     */
    private static Wave createRegularWave() {
        Wave wave = new Wave();
        for (int i = 0; i < ENEMIES_PER_WAVE; i++) {
            addEnemyToWaveEnemies(REGULAR_ENEMY, wave);
        }
        return wave;
    }

    /**
     * Erzeugt eine Welle von schwer gapanzerten Gegnern
     *
     * @return Die erzeugte Welle
     */
    private static Wave createHeavyWave() {
        Wave wave = new Wave();
        for (int i = 0; i < ENEMIES_PER_WAVE; i++) {
            addEnemyToWaveEnemies(HEAVY_ENEMY, wave);
        }
        return wave;
    }

    /**
     * Erzeugt eine Welle von schwer schnellen Gegnern
     *
     * @return Die erzeugte Welle
     */
    private static Wave createFastWave() {
        Wave wave = new Wave();
        for (int i = 0; i < ENEMIES_PER_WAVE; i++) {
            addEnemyToWaveEnemies(FAST_ENEMY, wave);
        }
        return wave;
    }

    /**
     * Erzeugt eine Welle von normalen und schwer gapanzerten Gegnern
     *
     * @return Die erzeugte Welle
     */
    private static Wave createRegularAndHeavyWave() {
        Wave wave = new Wave();
        for (int i = 0; i < ENEMIES_PER_WAVE / 2; i++) {
            addEnemyToWaveEnemies(REGULAR_ENEMY, wave);
            addEnemyToWaveEnemies(HEAVY_ENEMY, wave);
        }
        return wave;
    }

    /**
     * Erzeugt eine Welle von normalen und schnellen Gegnern
     *
     * @return Die erzeugte Welle
     */
    private static Wave createRegularAndFastWave() {
        Wave wave = new Wave();
        for (int i = 0; i < ENEMIES_PER_WAVE / 2; i++) {
            addEnemyToWaveEnemies(REGULAR_ENEMY, wave);
            addEnemyToWaveEnemies(FAST_ENEMY, wave);
        }
        return wave;
    }

    /**
     * Erzeugt eine Welle von schwer gepanzerten und schnellen Gegnern
     *
     * @return Die erzeugte Welle
     */
    private static Wave createHeavyAndFastWave() {
        Wave wave = new Wave();
        for (int i = 0; i < ENEMIES_PER_WAVE / 2; i++) {
            addEnemyToWaveEnemies(HEAVY_ENEMY, wave);
            addEnemyToWaveEnemies(FAST_ENEMY, wave);
        }
        return wave;
    }

    /**
     * Erzeugt eine Boss-Welle
     *
     * @return Die erzeugte Welle
     */
    private static Wave createBossWave() {
        Wave wave = new Wave();
        addEnemyToWaveEnemies(BOSS_ENEMY, wave);
        return wave;
    }

    /**
     * Fügt einen Gegner des angegebenen Typs zur übergebenen Welle hinzu
     * @param enemyType Der gewünschte Gegner-Typ
     * @param wave Die betreffende Welle
     */
    private static void addEnemyToWaveEnemies(int enemyType, Wave wave) {
        wave.addEnemy(createNewEnemy(enemyType));
    }
}
