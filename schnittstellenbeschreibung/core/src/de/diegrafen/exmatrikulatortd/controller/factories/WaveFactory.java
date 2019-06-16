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
     * Erzeugt eine Welle von normalen Gegnern
     * @return Die erzeugte Welle
     */
    private Wave createRegularWave ( ) {
        return null;
    }

    /**
     * Erzeugt eine Welle von schwer gapanzerten Gegnern
     * @return Die erzeugte Welle
     */
    private Wave createHeavyWave () {
        return null;
    }

    /**
     * Erzeugt eine Welle von schwer schnellen Gegnern
     * @return Die erzeugte Welle
     */
    private Wave createFastWave () {
        return null;
    }

    /**
     * Erzeugt eine Welle von normalen und schwer gapanzerten Gegnern
     * @return Die erzeugte Welle
     */
    private Wave createRegularAndHeavyWave () {
        return null;
    }

    /**
     * Erzeugt eine Welle von normalen und schnellen Gegnern
     * @return Die erzeugte Welle
     */
    private Wave createRegularAndFastWave () {
        return null;
    }

    /**
     * Erzeugt eine Welle von schwer gepanzerten und schnellen Gegnern
     * @return Die erzeugte Welle
     */
    private Wave createHeavyAndFastWave () {
        return null;
    }

    /**
     * Erzeugt eine Boss-Welle
     * @return Die erzeugte Welle
     */
    private Wave createBossWave () {
        return null;
    }

}
