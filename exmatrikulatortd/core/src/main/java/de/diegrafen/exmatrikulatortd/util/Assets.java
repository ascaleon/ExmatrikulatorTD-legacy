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
public class Assets {

    private static final String TOWER_SPRITE_PATH = "sprites/objects/towers/";

    public static final String REGULAR_TOWER_ASSETS = "WanderingEye@64x64.png";

    public static final String UPGRADED_REGULAR_TOWER_ASSETS = "WanderingEye2@64x64.png";

    public static final String SLOW_TOWER_ASSETS = "WanderingEye@64x64.png";

    public static final String CORRUPTION_TOWER_ASSETS = "WanderingEye@64x64.png";

    public static final String EXPLOSIVE_TOWER_ASSETS = "WanderingEye@64x64.png";

    public static final String AURA_TOWER_ASSETS = "WanderingEye@64x64.png";

    private static final String ENEMY_SPRITE_PATH = "sprites/objects/enemies/";

    public static final String REGULAR_ENEMY_ASSETS = "monster_cacto";

    public static final String HEAVY_ENEMY_ASSETS = "monster_golem";

    public static final String DEATH_ANIMATION_SPRITE_PATH = "sprites/objects/projectiles/mine/";

    public static final String DEATH_ANIMATION_ASSETS = "explosion";

    public static final String SINGLEPLAYER_MAP_PATH = "sprites/gamemap/prototypeMap.tmx";

    public static final String MULTIPLAYER_MAP_PATH = "sprites/gamemap/multiplayer_map.tmx";

    private static final String PROJECTILE_SPRITE_PATH = "sprites/objects/projectiles/fireball/";

    public static final String FIREBALL_ASSETS = "fireball";

    public static void queueAssets(AssetManager assetManager) {

        // load Textures

        assetManager.load(getTowerAssetPath(REGULAR_TOWER_ASSETS), Texture.class);
        assetManager.load(getTowerAssetPath(UPGRADED_REGULAR_TOWER_ASSETS), Texture.class);
        assetManager.load(getTowerAssetPath(SLOW_TOWER_ASSETS), Texture.class);
        assetManager.load(getTowerAssetPath(CORRUPTION_TOWER_ASSETS), Texture.class);
        assetManager.load(getTowerAssetPath(EXPLOSIVE_TOWER_ASSETS), Texture.class);
        assetManager.load(getTowerAssetPath(AURA_TOWER_ASSETS), Texture.class);


        // load atlasses
        assetManager.load(getEnemyAssetPath(REGULAR_ENEMY_ASSETS), TextureAtlas.class);
        assetManager.load(getEnemyAssetPath(HEAVY_ENEMY_ASSETS), TextureAtlas.class);
        // TODO: In normale Spritesheets verschieben.
        assetManager.load(DEATH_ANIMATION_SPRITE_PATH + DEATH_ANIMATION_ASSETS + ".atlas", TextureAtlas.class);

        assetManager.load(getProjectileAssetPath(FIREBALL_ASSETS), TextureAtlas.class);
    }

    public static String getTowerAssetPath(String towerAsset) {
        return TOWER_SPRITE_PATH + towerAsset;
    }

    public static String getEnemyAssetPath(String enemyAsset) {
        return ENEMY_SPRITE_PATH + enemyAsset + ".atlas";
    }

    public static String getProjectileAssetPath(String projectileAsset) {
        return PROJECTILE_SPRITE_PATH + projectileAsset + ".atlas";
    }
}
