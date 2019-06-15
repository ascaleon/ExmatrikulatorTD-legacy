package de.diegrafen.exmatrikulatortd.util;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 20:04
 */
public class Constants {

    public static final int UDP_PORT = 9001;

    public static final int TCP_PORT = 9002;

    public static final float TIME_BETWEEN_SPAWNS = 0.5f;

    public static final float TIME_BETWEEN_ROUNDS = 20f;

    /**
     * Konstante, die angibt, in welchem zeitlichen Abstand ein Turm nach einem neuen Gegner sucht.
     */
    private static final float SEARCH_TARGET_INTERVAL = 0.75f;
}
