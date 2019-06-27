package de.diegrafen.exmatrikulatortd.util;

/**
 *
 * Definiert globale Konstanten, die von anderen Modulen genutzt werden können
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 20:04
 */
public class Constants {

    public static final String GAME_TITLE = "Exmatrikulator TD";

    public static final int UDP_PORT = 9001;

    public static final int TCP_PORT = 9002;

    public static final float TIME_BETWEEN_SPAWNS = 1f;

    public static final float TIME_BETWEEN_ROUNDS = 5f;

    /**
     * Konstante, die angibt, in welchem zeitlichen Abstand ein Turm nach einem neuen Gegner sucht.
     */
    public static final float SEARCH_TARGET_INTERVAL = 0.75f;

    public static final int TILE_SIZE = 64;
}
