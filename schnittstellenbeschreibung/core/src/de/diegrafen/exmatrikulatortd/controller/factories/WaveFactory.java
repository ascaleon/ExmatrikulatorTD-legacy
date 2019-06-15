package de.diegrafen.exmatrikulatortd.controller.factories;

import de.diegrafen.exmatrikulatortd.model.enemy.Wave;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:01
 */
public class WaveFactory {

    private WaveFactory () {

    }

    public enum WaveType {

        REGULAR_WAVE, HEAVY_WAVE, FAST_WAVE, REGULAR_AND_HEAVY_WAVE, REGULAR_AND_FAST_WAVE, HEAVY_AND_FAST_WAVE,
        BOSS_WAVE
    }

    public static Wave createWave (WaveType waveType) {
        return null;
    }



}
