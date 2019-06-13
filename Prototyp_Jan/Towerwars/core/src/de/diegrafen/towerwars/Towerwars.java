package de.diegrafen.towerwars;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import de.diegrafen.towerwars.screens.MenuScreen;
import de.diegrafen.towerwars.screens.PlayScreen;
import de.diegrafen.towerwars.screens.SplashScreen;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static de.diegrafen.towerwars.util.HibernateUtils.getSessionFactory;

public class Towerwars extends Game {

    private AssetManager assetManager = new AssetManager();
    private MenuScreen menuScreen;
    private SplashScreen splashScreen;

    private Preferences preferences;

    private static long frame;
    private long start;
    private long count = 0;
    private long startTime = System.currentTimeMillis();
    private boolean skipTipp = true;

    public static final int TILE_SIZE = 20;
    public static final int GRID_SIZE = 3 * TILE_SIZE;


    public static final int WIDTH = 1280;

    public static final int HEIGHT = 680;

    public static final String TITLE = "Exmatrikulator TD";

    private SpriteBatch batch;

    Texture img;

    private Socket clientSocket;

    private boolean isConnected;

    TiledMap tiledMap;

    OrthographicCamera camera;

    TiledMapRenderer tiledMapRenderer;

    public Towerwars() {
        this.start = System.currentTimeMillis();
    }

    @Override
    public void create() {

        Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //Gdx.gl.glHint(GL20, GL20.GL_NICEST);
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);

        preferences = Gdx.app.getPreferences(Assets.GLOBAL_PREF_NAME);
        //skipTipp = preferences.contains(SplashScreen.REMEMBER_KEY) && preferences.getBoolean(SplashScreen.REMEMBER_KEY);

        assetManager.setLoader(ParticleEffect.class, new ParticleEffectLoader(new InternalFileHandleResolver()));

        Texture.setAssetManager(assetManager);
        Assets.setAssetManager(assetManager);

        Assets.loadFonts();
        Assets.queueAssets();

        splashScreen = new SplashScreen(this);
    //gameStateManager = new GameStateManager();
		//batch = new SpriteBatch();
		//setScreen(new PlayScreen());
	}

	@Override
	public void render () {
		//super.render();

        if (assetManager.update()) {

            // instantiate if not already here
            if (menuScreen == null) {
                menuScreen = new MenuScreen(this);
                System.out.println("Blah2");
            }

            splashScreen.setLoadingPercentage(1);

            // automatic change change to menu screen
            if (skipTipp) {
                if (getScreen() == splashScreen) {
                    System.out.println("Blah3");
                    setScreen(menuScreen);
                    System.out.println("Blah4");
                }
            }

        } else {
            if (getScreen() == null) {
                setScreen(splashScreen);
            }
            splashScreen.setLoadingPercentage(assetManager.getProgress());
        }

        if (getScreen() != null) getScreen().render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize (int width, int height) {
	    super.resize(width, height);
    }

    @Override
    public void pause () {
	    super.pause();
    }

    @Override
    public void resume () {
        super.resume();
    }

	@Override
	public void dispose () {
		super.dispose();
	}

	public void connectToServer() {
	    SocketHints socketHints = new SocketHints();

        clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 9001, socketHints);
    }

    public void switchToMenu() {
        menuScreen.validateGameScreen();

        setScreen(menuScreen);
    }
}
