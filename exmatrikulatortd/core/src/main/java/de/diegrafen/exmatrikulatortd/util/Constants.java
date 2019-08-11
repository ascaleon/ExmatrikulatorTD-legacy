package de.diegrafen.exmatrikulatortd.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Definiert globale Konstanten, die von anderen Komponenten genutzt werden können
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 02.07.2019 22:09
 */
public final class Constants {

    public static final int MIN_NUMBER_OF_UPDATES = 30;

    public static final int NUMBER_OF_TOWERS = 5;

    public static final float CAMERA_TRANSLATE_VALUE = 5;

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static final int REGULAR_TOWER = 0;

    public static final int SLOW_TOWER = 1;

    public static final int CORRUPTION_TOWER = 2;

    public static final int EXPLOSIVE_TOWER = 3;

    public static final int AURA_TOWER = 4;

    public static final String GAME_TITLE = "Exmatrikulator TD";

    public static final int UDP_PORT = 9001;

    public static final int TCP_PORT = 9002;

    public static final float TIME_BETWEEN_SPAWNS = 1;

    public static final float TIME_BETWEEN_ROUNDS = 15f;

    public static final float DISTANCE_TOLERANCE = 3f;

    /**
     * Konstante, die angibt, in welchem zeitlichen Abstand ein Turm nach einem neuen Gegner sucht.
     */
    public static final float SEARCH_TARGET_INTERVAL = 0;

    public static final float AURA_REFRESH_RATE = 0.1f;

    public static final float DAMAGE_REDUCTION_FACTOR = 0.06f;

    public static final float SPEED_INCREASE_PER_LEVEL = 3f;

    public static final float ARMOR_INCREASE_PER_LEVEL = 2f;

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
     * Angriffstyp "Magieschaden".
     */
    public static final int MAGIC = 3;

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
    public static final int REGULAR = 2;

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

    public final static int EASY = 0;

    public final static String EASY_STRING = "Leicht";

    public final static int MEDIUM = 1;

    public final static String MEDIUM_STRING = "Mittel";

    public final static int HARD = 2;

    public final static String HARD_STRING = "Schwer";

    public final static int TESTMODE = 3;

    public final static String TESTMODE_STRING = "Tutormodus";

    public static final int STANDARD_SINGLE_PLAYER_GAME = 0;

    public static final int ENDLESS_SINGLE_PLAYER_GAME = 1;

    public static final int MULTIPLAYER_DUEL = 2;

    public static final int MULTIPLAYER_STANDARD_GAME = 3;

    public static final int MULTIPLAYER_ENDLESS_GAME = 4;

    public static final int ENEMIES_PER_WAVE = 20;

    public static final int REGULAR_WAVE = 0;

    public static final int HEAVY_WAVE = 1;

    public static final int FAST_WAVE = 2;

    public static final int REGULAR_AND_HEAVY_WAVE = 3;

    public static final int REGULAR_AND_FAST_WAVE = 4;

    public static final int HEAVY_AND_FAST_WAVE = 5;

    public static final int BOSS_WAVE = 6;
}
