package de.diegrafen.exmatrikulatortd.util;

/**
 *
 * Definiert globale Konstanten, die von anderen Modulen genutzt werden können
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 20:04
 */
public class Constants {

    public static final int SPRITE_SIZE = 256;

    public static final int UDP_PORT = 9001;

    public static final int TCP_PORT = 9002;

    public static final float TIME_BETWEEN_SPAWNS = 0.5f;

    public static final float TIME_BETWEEN_ROUNDS = 20f;

    /**
     * Konstante, die angibt, in welchem zeitlichen Abstand ein Turm nach einem neuen Gegner sucht.
     */
    public static final float SEARCH_TARGET_INTERVAL = 0.75f;

    public static final int TILE_SIZE = 64;

    public static final int[][] TEST_COLLISION_MAP = {
            {-1,-1,0,0,0,0,0,0},
            {0,-1,0,0,0,0,0,0},
            {0,-1,0,0,0,0,0,0},
            {0,-1,0,0,0,0,0,0},
            {0,-1,0,0,0,0,0,0},
            {0,-1,-1,-1,-1,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
    };

    public static float X_POS = 0;

    public static float Y_POS = 0;

    public static void setX (float x) {
        X_POS = x;
    }

    public static void setY (float y) {
        Y_POS = y;
    }

}
