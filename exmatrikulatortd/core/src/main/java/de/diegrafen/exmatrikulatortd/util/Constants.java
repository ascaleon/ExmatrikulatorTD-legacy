package de.diegrafen.exmatrikulatortd.util;

/**
 * Definiert globale Konstanten, die von anderen Modulen genutzt werden können
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 02.07.2019 22:09
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

    public static final float AURA_REFRESH_RATE = 0.5f;

    public static final float DAMAGE_REDUCTION_FACTOR = 0.06f;

    /**
     * Angriffstyp "Normaler Schaden"
     */
    public static final int NORMAL = 0;

    /**
     * Angriffstyp "Stichschaden"
     */
    public static final int PIERCING = 1;

    /**
     * Angriffstyp "Explosivschaden"
     */
    public static final int EXPLOSIVE = 2;

    /**
     * Angriffstyp "Logikschaden". Überfordert Studierende mit vielen mathematischen Symbolen.
     */
    public static final int LOGIC = 3;

    /**
     * Rüstungstyp "ohne Rüstung"
     */
    public static final int UNARMORED = 0;

    /**
     * Rüstungstyp "leichte Rüstung"
     */
    public static final int LIGHT = 1;

    /**
     * Rüstungstyp "mittlere Rüstung"
     */
    public static final int MEDIUM = 2;

    /**
     * Rüstungstyp "schwere Rüstung"
     */
    public static final int HEAVY = 3;

    /**
     * Rüstungstyp "unbesiegbar"
     */
    public static final int INVINCIBLE = 4;

    /**
     * Matrix, die Schadensboni bzw. -mali für die verschiedenen Angriffs- und Rüstungstyp-Kombinationen definiert
     */
    public static final float[][] ATTACK_DEFENSE_MATRIX = {
            {1.00f, 1.00f, 1.50f, 0.70f, 0},
            {1.50f, 2.00f, 0.75f, 0.70f, 0},
            {1.50f, 0.50f, 1.00f, 2.00f, 0},
            {1.00f, 1.25f, 0.75f, 1.50f, 0}};
}
