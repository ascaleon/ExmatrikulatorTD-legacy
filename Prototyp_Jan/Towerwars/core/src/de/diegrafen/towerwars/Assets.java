package de.diegrafen.towerwars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;




public class Assets {

    // textures
    public static final String PLAY_BUTTON_UP = "play.png";
    public static final String PLAY_BUTTON_DOWN = "play.png";
    public static final String PAUSE_BUTTON_UP = "pause.png";
    public static final String PAUSE_BUTTON_DOWN = "pause.png";
    public static final String BACKGROUND = "background.png";
    public static final String BACKGROUND_SMALL = "backgroundsmall.png";
    public static final String V_SCROLL = "sprites/hud/vscroll.png";
    public static final String V_SCROLL_KNOB = "sprites/hud/vscrollknob.png";
    public static final String H_SCROLL = "sprites/hud/hscroll.png";
    public static final String H_SCROLL_KNOB = "sprites/hud/hscrollknob.png";
    public static final String FAST_FORWARD_UP = "fastforward.png";
    public static final String FAST_FORWARD_DOWN = "fastforward.png";
    public static final String FAST_FORWARD_CHECKED = "fastforwardchecked.png";
    public static final String TURRET_ICON = "sprites/towers/turretIcon.png";
    public static final String TURRET_ICON_SMALL = "sprites/towers/turretIconSmall.png";
    public static final String TURRET_ICON_SMALL_SELECTED = "sprites/towers/turretIconSmallSelected.png";
    public static final String SLOW_ICON = "sprites/towers/slowIcon.png";
    public static final String SLOW_ICON_SMALL = "sprites/towers/slowIconSmall.png";
    public static final String SLOW_ICON_SMALL_SELECTED = "sprites/towers/slowIconSmallSelected.png";
    public static final String ARTILLERY_ICON = "sprites/towers/artilleryIcon.png";
    public static final String ARTILLERY_ICON_SMALL = "sprites/towers/artilleryIconSmall.png";
    public static final String ARTILLERY_ICON_SMALL_SELECTED = "sprites/towers/artilleryIconSmallSelected.png";
    public static final String TESLA_ICON = "sprites/towers/teslaIcon.png";
    public static final String TESLA_ICON_SMALL = "sprites/towers/teslaIconSmall.png";
    public static final String TESLA_ICON_SMALL_SELECTED = "sprites/towers/teslaIconSmallSelected.png";
    public static final String ROCKET_ICON = "sprites/towers/rocketIcon.png";
    public static final String ROCKET_ICON_SMALL = "sprites/towers/rocketIconSmall.png";
    public static final String ROCKET_ICON_SMALL_SELECTED = "sprites/towers/rocketIconSmallSelected.png";
    public static final String FIRE_ICON = "sprites/towers/fireIcon.png";
    public static final String FIRE_ICON_SMALL = "sprites/towers/fireIconSmall.png";
    public static final String FIRE_ICON_SMALL_SELECTED = "sprites/towers/fireIconSmallSelected.png";
    public static final String EMPTY_ICON = "sprites/hud/emptyicon.png";
    public static final String ACHIEVEMENT_UNLOCKED_ICON = "sprites/hud/achievementunlocked.png";
    public static final String ACHIEVEMENT_ICON = "sprites/hud/achievement.png";

    //public static final String LEVEL4 = "level4.png";
    //public static final String LEVEL3 = "level3.png";
    //public static final String LEVEL2 = "level2.png";
    //public static final String LEVEL1 = "level1.png";
    public static final String INCREASE_BUTTON = "sprites/hud/increasebutton.png";
    public static final String INCREASE_BUTTON_PRESSED = "sprites/hud/increasebuttonpressed.png";
    public static final String DECREASE_BUTTON = "sprites/hud/decreasebutton.png";
    public static final String DECREASE_BUTTON_PRESSED = "sprites/hud/decreasebuttonpressed.png";
    public static final String BACK_BUTTON = "sprites/hud/backbutton.png";
    public static final String BACK_BUTTON_PRESSED = "sprites/hud/backbuttonpressed.png";

    // fonts
    public static BitmapFont TEXT_FONT;
    public static BitmapFont TITLE_FONT_38;
    public static BitmapFont TITLE_FONT_64;
    public static BitmapFont TITLE_FONT_90;

    // colors
    public static final Color COLOR_RED = new Color(180f / 255f, 42f / 255f, 42f / 255f, 1);
    public static final Color COLOR_GREEN = new Color(0f / 255f, 81f / 255f, 44f / 255f, 1);
    public static final Color COLOR_YELLOW = new Color(212f / 255f, 207f / 255f, 17f / 255f, 1);

    // files
    public static final String SAVE_GAME_NAME = "heavydefense.save";
    public static final String SAVE_GAME_NAME_2_0_0 = "heavydefense2_0_0.save";
    public static final String GLOBAL_PREF_NAME = "towerdefense.pref";
    public static final String CURRENT_SAVE_GAME_NAME = SAVE_GAME_NAME_2_0_0;

    private static AssetManager assetManager = null;


    public static void setAssetManager(AssetManager assetManager) {
        Assets.assetManager = assetManager;
    }

    public static TextureAtlas getTextureAtlas(String id) {
        return assetManager.get(id);
    }

    public static void loadFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixelfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 26;
        TEXT_FONT = generator.generateFont(parameter);
        parameter.size = 38;
        TITLE_FONT_38 = generator.generateFont(parameter);
        parameter.size = 64;
        TITLE_FONT_64 = generator.generateFont(parameter);
        parameter.size = 90;
        TITLE_FONT_90 = generator.generateFont(parameter);

        generator.dispose();
    }

    public static Texture getTexture(String path) {
        return assetManager.get(path);
    }

    public static void queueAssets() {
        // load textures

        assetManager.load(PLAY_BUTTON_UP, Texture.class);
        assetManager.load(PLAY_BUTTON_UP, Texture.class);
        assetManager.load(PLAY_BUTTON_DOWN, Texture.class);
        assetManager.load(PAUSE_BUTTON_UP, Texture.class);
        assetManager.load(PAUSE_BUTTON_DOWN, Texture.class);
        assetManager.load(BACKGROUND, Texture.class);
        assetManager.load(BACKGROUND_SMALL, Texture.class);
        assetManager.load(V_SCROLL, Texture.class);
        assetManager.load(V_SCROLL_KNOB, Texture.class);
        assetManager.load(H_SCROLL, Texture.class);
        assetManager.load(H_SCROLL_KNOB, Texture.class);
        assetManager.load(FAST_FORWARD_UP, Texture.class);
        assetManager.load(FAST_FORWARD_DOWN, Texture.class);
        assetManager.load(FAST_FORWARD_CHECKED, Texture.class);
        assetManager.load(TURRET_ICON, Texture.class);
        assetManager.load(TURRET_ICON_SMALL, Texture.class);
        assetManager.load(TURRET_ICON_SMALL_SELECTED, Texture.class);
        assetManager.load(SLOW_ICON, Texture.class);
        assetManager.load(SLOW_ICON_SMALL, Texture.class);
        assetManager.load(SLOW_ICON_SMALL_SELECTED, Texture.class);
        assetManager.load(ARTILLERY_ICON, Texture.class);
        assetManager.load(ARTILLERY_ICON_SMALL, Texture.class);
        assetManager.load(ARTILLERY_ICON_SMALL_SELECTED, Texture.class);
        assetManager.load(TESLA_ICON, Texture.class);
        assetManager.load(TESLA_ICON_SMALL, Texture.class);
        assetManager.load(TESLA_ICON_SMALL_SELECTED, Texture.class);
        assetManager.load(ROCKET_ICON, Texture.class);
        assetManager.load(ROCKET_ICON_SMALL, Texture.class);
        assetManager.load(ROCKET_ICON_SMALL_SELECTED, Texture.class);
        assetManager.load(FIRE_ICON, Texture.class);
        assetManager.load(FIRE_ICON_SMALL, Texture.class);
        assetManager.load(FIRE_ICON_SMALL_SELECTED, Texture.class);
        assetManager.load(EMPTY_ICON, Texture.class);
        //assetManager.load(LEVEL1, Texture.class);
        //assetManager.load(LEVEL2, Texture.class);
        //assetManager.load(LEVEL3, Texture.class);
        //assetManager.load(LEVEL4, Texture.class);
        assetManager.load(INCREASE_BUTTON, Texture.class);
        assetManager.load(INCREASE_BUTTON_PRESSED, Texture.class);
        assetManager.load(DECREASE_BUTTON, Texture.class);
        assetManager.load(DECREASE_BUTTON_PRESSED, Texture.class);
        assetManager.load(BACK_BUTTON, Texture.class);
        assetManager.load(BACK_BUTTON_PRESSED, Texture.class);
        assetManager.load(ACHIEVEMENT_ICON, Texture.class);
        assetManager.load(ACHIEVEMENT_UNLOCKED_ICON, Texture.class);

        // load particle effects
        //final ParticleEffectLoaderParameters parameters = new ParticleEffectLoaderParameters(IMAGES_DIR);

        //assetManager.load(ROCKET_SMOKE, ParticleEffect.class, parameters);
        //assetManager.load(BURNING, ParticleEffect.class, parameters);
        //assetManager.load(FIRE_EFFECT, ParticleEffect.class, parameters);
        //assetManager.load(FROST_EFFECT, ParticleEffect.class, parameters);

        // load atlasses
        //assetManager.load(TOWER_ATLAS, TextureAtlas.class);
        //assetManager.load(ENEMY_ATLAS, TextureAtlas.class);
        //assetManager.load(OVERLAY_ATLAS, TextureAtlas.class);

    }

}
