package de.diegrafen.towerwars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import de.diegrafen.towerwars.models.Player;
import de.diegrafen.towerwars.persistence.PlayerDao;
import de.diegrafen.towerwars.Towerwars;
import de.diegrafen.towerwars.sprites.Enemy;

import java.util.List;


/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.05.2019 21:51
 */
public class PlayScreen implements Screen, InputProcessor {


    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    private SpriteBatch batch;

    private Enemy enemy;

    private PlayerDao playerDao = new PlayerDao();

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

        Player player1 = new Player("Rainer Koschke");
        Player player2 = new Player("Tim Haga");
        Player player3 = new Player("Jan Paleska");

        playerDao.create(player1);
        playerDao.create(player2);
        playerDao.create(player3);

        List<Player> players = playerDao.findAll();

        for (Player player : players) {
            System.out.println(player.toString());
        }

        playerDao.deleteAll();

        players = playerDao.findAll();

        for (Player player : players) {
            System.out.println(player.toString());
        }

        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.generateMipMaps = true;

        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("prototypeMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera.setToOrtho(false, Towerwars.WIDTH, Towerwars.HEIGHT);

        MapProperties properties = map.getProperties();

        camera.position.x = MathUtils.clamp(camera.position.x,
                camera.viewportWidth / 2,
                properties.get("width", Integer.class) - camera.viewportWidth / 2);
        camera.position.y = MathUtils.clamp(camera.position.y,
                camera.viewportHeight / 2,
                properties.get("height", Integer.class) - camera.viewportHeight / 2);

        enemy = new Enemy(new Sprite(new Texture("badlogic.jpg")));


    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {



        mapRenderer.getBatch().setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();


    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;// * 2;
        camera.viewportHeight = height;// * 2;

        MapProperties properties = map.getProperties();

        camera.position.x = MathUtils.clamp(camera.position.x,
                camera.viewportWidth / 2,
                properties.get("width", Integer.class) - camera.viewportWidth / 2);
        camera.position.y = MathUtils.clamp(camera.position.y,
                camera.viewportHeight / 2,
                properties.get("height", Integer.class) - camera.viewportHeight / 2);
        camera.viewportWidth = width;
        camera.viewportHeight = height;

        camera.update();

    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        enemy.getTexture().dispose();
    }
    public boolean keyDown (int keycode) {
        if(keycode == Input.Keys.LEFT){return false;}

        if(keycode == Input.Keys.RIGHT){return false;}
        else{return false;}
    }

    public boolean keyUp (int keycode) {
        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (int amount) {
        return false;
    }

}
