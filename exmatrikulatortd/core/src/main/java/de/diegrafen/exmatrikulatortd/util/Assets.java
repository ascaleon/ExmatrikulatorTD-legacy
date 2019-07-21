package de.diegrafen.exmatrikulatortd.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 *
 * Definiert Konstanten, über die sich die Pfade der im Spiel verwendeten Assets abrufen lassen
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 00:58
 */
public final class Assets {

    public static final String MENU_BACKGROUND_IMAGE = "196922.jpg";

    private static final String TOWER_SPRITE_PATH = "sprites/objects/towers/";

    public static final String REGULAR_TOWER_ASSETS = "tower_gun";

    public static final String UPGRADED_REGULAR_TOWER_ASSETS = "tower_gun";

    public static final String REGULAR_TOWER_PORTRAIT = "gunwoman_portrait.png";

    public static final String REGULAR_TOWER_PORTRAIT_SELECTED = "gunwoman_portrait_clicked.png";

    public static final String SLOW_TOWER_ASSETS = "tower_mage";

    public static final String SLOW_TOWER_PORTRAIT = "skeleton_mage_portrait.png";

    public static final String SLOW_TOWER_PORTRAIT_SELECTED = "skeleton_mage_portrait_clicked.png";

    public static final String CORRUPTION_TOWER_ASSETS = "tower_mage";

    public static final String CORRUPTION_TOWER_PORTRAIT = "skeleton_archer_portrait.png";

    public static final String CORRUPTION_TOWER_PORTRAIT_SELECTED = "skeleton_archer_portrait_clicked.png";

    public static final String EXPLOSIVE_TOWER_ASSETS = "tower_mage";

    public static final String EXPLOSIVE_TOWER_PORTRAIT = "mage_portrait.png";

    public static final String EXPLOSIVE_TOWER_PORTRAIT_SELECTED = "mage_portrait_clicked.png";

    public static final String AURA_TOWER_ASSETS = "tower_mage";

    public static final String AURA_TOWER_PORTRAIT = "mage_portrait.png";

    public static final String AURA_TOWER_PORTRAIT_SELECTED = "mage_portrait.png";

    private static final String ENEMY_SPRITE_PATH = "sprites/objects/enemies/";

    public static final String REGULAR_ENEMY_ASSETS = "monster_cacto";

    public static final String HEAVY_ENEMY_ASSETS = "monster_golem";

    public static final String DEATH_ANIMATION_SPRITE_PATH = "sprites/objects/projectiles/mine/";

    public static final String DEATH_ANIMATION_ASSETS = "explosion";

    public static final String SINGLEPLAYER_MAP_PATH = "sprites/gamemap/singleplayerMap.tmx";

    public static final String MULTIPLAYER_MAP_PATH = "sprites/gamemap/multiplayer_map.tmx";

    private static final String PROJECTILE_SPRITE_PATH = "sprites/objects/projectiles/fireball/";

    public static final String FIREBALL_ASSETS = "fireball";

    public static void queueAssets(AssetManager assetManager) {

        // load Textures
        assetManager.load(MENU_BACKGROUND_IMAGE, Texture.class);

        // load atlasses
        assetManager.load(getTowerAssetPath(REGULAR_TOWER_ASSETS), TextureAtlas.class);
        assetManager.load(getTowerAssetPath(UPGRADED_REGULAR_TOWER_ASSETS), TextureAtlas.class);
        assetManager.load(getTowerAssetPath(SLOW_TOWER_ASSETS), TextureAtlas.class);
        assetManager.load(getTowerAssetPath(CORRUPTION_TOWER_ASSETS), TextureAtlas.class);
        assetManager.load(getTowerAssetPath(EXPLOSIVE_TOWER_ASSETS), TextureAtlas.class);
        assetManager.load(getTowerAssetPath(AURA_TOWER_ASSETS), TextureAtlas.class);

        assetManager.load(getEnemyAssetPath(REGULAR_ENEMY_ASSETS), TextureAtlas.class);
        assetManager.load(getEnemyAssetPath(HEAVY_ENEMY_ASSETS), TextureAtlas.class);
        // TODO: In normale Spritesheets verschieben.
        assetManager.load(DEATH_ANIMATION_SPRITE_PATH + DEATH_ANIMATION_ASSETS + ".atlas", TextureAtlas.class);

        assetManager.load(getProjectileAssetPath(FIREBALL_ASSETS), TextureAtlas.class);
    }

    public static String getTowerAssetPath(String towerAsset) {
        return TOWER_SPRITE_PATH + towerAsset + ".atlas";
    }

    public static String getEnemyAssetPath(String enemyAsset) {
        return ENEMY_SPRITE_PATH + enemyAsset + ".atlas";
    }

    public static String getProjectileAssetPath(String projectileAsset) {
        return PROJECTILE_SPRITE_PATH + projectileAsset + ".atlas";
    }
}
