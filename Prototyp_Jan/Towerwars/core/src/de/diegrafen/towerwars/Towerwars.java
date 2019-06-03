package de.diegrafen.towerwars;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import de.diegrafen.towerwars.states.GameStateManager;
import de.diegrafen.towerwars.states.MenuState;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Towerwars extends Game {

	private AssetManager assetManager = new AssetManager();
	private MenuScreen menuScreen;
	private SplashScreen splashScreen;

	private Preferences preferences;


	public static final int WIDTH = 1280;

    public static final int HEIGHT = 720;

    public static final String TITLE = "TowerWars";

    private GameStateManager gameStateManager;

	private SpriteBatch batch;

	Texture img;

	private Socket clientSocket;

	private boolean isConnected;

    TiledMap tiledMap;

    OrthographicCamera camera;

    TiledMapRenderer tiledMapRenderer;
	
	@Override
	public void create () {



/*	    gameStateManager = new GameStateManager();
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		//img = new Texture(Gdx.files.internal("assets/textures/badlogic.jpg"));

        Gdx.gl.glClearColor(1, 0, 0, 1);

        gameStateManager.push(new MenuState(gameStateManager));

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();
		tiledMap = new TmxMapLoader().load("test.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		Gdx.input.setInputProcessor(this);*/
		setScreen(new PlayScreen());
	}

	@Override
	public void render () {
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		gameStateManager.update(Gdx.graphics.getDeltaTime());
//		gameStateManager.render(batch);
//        camera.update();
//        tiledMapRenderer.setView(camera);
//        tiledMapRenderer.render();
		super.render();
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
		//batch.dispose();
		//img.dispose();
		super.dispose();
	}

	public void connectToServer() {
	    SocketHints socketHints = new SocketHints();

        clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 9001, socketHints);
    }

//	@Override
//	public boolean keyDown(int keycode) {
//		if(keycode == Input.Keys.LEFT)
//			camera.translate(-32,0);
//		if(keycode == Input.Keys.RIGHT)
//			camera.translate(32,0);
//		if(keycode == Input.Keys.UP)
//			camera.translate(0,-32);
//		if(keycode == Input.Keys.DOWN)
//			camera.translate(0,32);
//		if(keycode == Input.Keys.NUM_1)
//			tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
//		if(keycode == Input.Keys.NUM_2)
//			tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
//		return false;
//	}
}
