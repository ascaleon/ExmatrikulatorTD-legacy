package de.diegrafen.towerwars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import de.diegrafen.towerwars.persistance.DataBase;
import de.diegrafen.towerwars.sprites.Enemy;
import de.diegrafen.towerwars.sprites.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.05.2019 21:51
 */
public class PlayScreen implements Screen {


    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    private OrthographicCamera camera;

    private Enemy enemy;

    private DataBase dataBase;

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

        dataBase = new DataBase();

        tiledMap = new TmxMapLoader().load("test.tmx");

        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        camera = new OrthographicCamera();

        enemy = new Enemy(new Sprite(new Texture("badlogic.jpg")));

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();

        renderer.getBatch().begin();
        enemy.draw(renderer.getBatch());
        renderer.getBatch().end();

    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width * 2;
        camera.viewportHeight = height * 2;
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
        tiledMap.dispose();
        renderer.dispose();
        enemy.getTexture().dispose();
    }
}
