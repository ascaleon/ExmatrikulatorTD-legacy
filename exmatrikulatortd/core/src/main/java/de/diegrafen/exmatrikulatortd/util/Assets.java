package de.diegrafen.exmatrikulatortd.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 *
 * Definiert Konstanten, über die sich die Pfade der im Spiel verwendeten Assets abrufen lassen
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 00:58
 */
public final class Assets {

    public static final String MENU_BACKGROUND_IMAGE = "196922.jpg";

    public static final String TRANSPARENT_BACKGROUND_IMAGE = "transparentBG.png";

    public static final String MENU_ICON_PLACEHOLDER = "menuIcon_placeholder.png";

    public static final String WIN_IMAGE = "win.png";

    public static final String LOSE_IMAGE = "loose.png";

    public static final String UPRADE_ICON = "upgradeIcon.png";

    public static final String SELL_ICON = "sellIcon.png";

    public static final String SEND_REGULAR_ENEMY_ICON = "sendEnemyRegularIcon.png";

    public static final String SEND_HEAVY_ENEMY_ICON = "sendEnemyHeavyIcon.png";

    private static final String TOWER_SPRITE_PATH = "sprites/objects/towers/";

    public static final String REGULAR_TOWER_ASSETS = "tower_gun";

    public static final String REGULAR_TOWER_PORTRAIT = "gunwoman_portrait.png";

    public static final String REGULAR_TOWER_PORTRAIT_SELECTED = "gunwoman_portrait_clicked.png";

    public static final String SLOW_TOWER_ASSETS = "tower_skeletonMage";

    public static final String SLOW_TOWER_PORTRAIT = "skeleton_mage_portrait.png";

    public static final String SLOW_TOWER_PORTRAIT_SELECTED = "skeleton_mage_portrait_clicked.png";

    public static final String CORRUPTION_TOWER_ASSETS = "tower_archer";

    public static final String CORRUPTION_TOWER_PORTRAIT = "skeleton_archer_portrait.png";

    public static final String CORRUPTION_TOWER_PORTRAIT_SELECTED = "skeleton_archer_portrait_clicked.png";

    public static final String EXPLOSIVE_TOWER_ASSETS = "tower_mage";

    public static final String EXPLOSIVE_TOWER_PORTRAIT = "mage_portrait.png";

    public static final String EXPLOSIVE_TOWER_PORTRAIT_SELECTED = "mage_portrait_clicked.png";

    public static final String AURA_TOWER_ASSETS = "tower_buff";

    public static final String AURA_TOWER_PORTRAIT = "buff_portrait.png";

    public static final String AURA_TOWER_PORTRAIT_SELECTED = "buff_portrait_clicked.png";

    private static final String ENEMY_SPRITE_PATH = "sprites/objects/enemies/";

    public static final String REGULAR_ENEMY_ASSETS = "enemy_regular";

    public static final String HEAVY_ENEMY_ASSETS = "enemy_heavy";

    public static final String FAST_ENEMY_ASSETS = "enemy_fast";

    public static final String BOSS_ENEMY_ASSETS = "enemy_boss";

    public static final String DEATH_ANIMATION_ASSETS = "explosion";

    public static final String SINGLEPLAYER_MAP_PATH = "sprites/gamemap/singleplayerMap.tmx";

    public static final String MULTIPLAYER_MAP_PATH = "sprites/gamemap/multiplayer_map.tmx";

    private static final String PROJECTILE_SPRITE_PATH = "sprites/objects/projectiles/";

    public static final String FIREBALL_ASSETS = "fireball";

    public static final String SKIN = "ui-skin/golden-ui-skin.json";

    public static void queueAssets(AssetManager assetManager) {

        // load textures
        assetManager.load(MENU_BACKGROUND_IMAGE, Texture.class);
        assetManager.load(TRANSPARENT_BACKGROUND_IMAGE, Texture.class);
        assetManager.load(MENU_ICON_PLACEHOLDER, Texture.class);
        assetManager.load(WIN_IMAGE, Texture.class);
        assetManager.load(LOSE_IMAGE, Texture.class);
        assetManager.load(UPRADE_ICON, Texture.class);
        assetManager.load(SELL_ICON, Texture.class);
        assetManager.load(SEND_HEAVY_ENEMY_ICON, Texture.class);
        assetManager.load(SEND_REGULAR_ENEMY_ICON, Texture.class);

        // load tower atlasses
        assetManager.load(getTowerAssetPath(REGULAR_TOWER_ASSETS), TextureAtlas.class);
        assetManager.load(getTowerAssetPath(SLOW_TOWER_ASSETS), TextureAtlas.class);
        assetManager.load(getTowerAssetPath(CORRUPTION_TOWER_ASSETS), TextureAtlas.class);
        assetManager.load(getTowerAssetPath(EXPLOSIVE_TOWER_ASSETS), TextureAtlas.class);
        assetManager.load(getTowerAssetPath(AURA_TOWER_ASSETS), TextureAtlas.class);

        // load enemy atlasses
        assetManager.load(getEnemyAssetPath(REGULAR_ENEMY_ASSETS), TextureAtlas.class);
        assetManager.load(getEnemyAssetPath(HEAVY_ENEMY_ASSETS), TextureAtlas.class);
        assetManager.load(getEnemyAssetPath(FAST_ENEMY_ASSETS), TextureAtlas.class);
        assetManager.load(getEnemyAssetPath(BOSS_ENEMY_ASSETS), TextureAtlas.class);
        assetManager.load(getEnemyAssetPath(DEATH_ANIMATION_ASSETS), TextureAtlas.class);

        // load projectile atlasses
        assetManager.load(getProjectileAssetPath(FIREBALL_ASSETS), TextureAtlas.class);

        // Load skins
        assetManager.load(SKIN, Skin.class);
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
