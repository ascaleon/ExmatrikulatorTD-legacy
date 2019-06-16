package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.enemy.Wave;

/**
 *
 * Factory für Gegner-Wellen
 *
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:01
 */
public final class WaveFactory {


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
        return null;
    }


    /**
     * 
     * @return
     */
    private Wave createRegularWave ( ) {
        return null;
    }

    private Wave createHeavyWave () {
        return null;
    }

    private Wave createFastWave () {
        return null;
    }

    private Wave createRegularAndHeavyWave () {
        return null;
    }

    private Wave createRegularAndFastWave () {
        return null;
    }

    private Wave createHeavyAndFastWave () {
        return null;
    }

    private Wave createBossWave () {
        return null;
    }

}
