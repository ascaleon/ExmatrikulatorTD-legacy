package de.diegrafen.towerwars.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class AbstractScreen implements Screen {

    protected Stage stage;
    protected Stage ui;
    protected OrthographicCamera stageCamera;
    private boolean paused;
    private float speed = 1;

    public AbstractScreen() {
        super();

        final SpriteBatch stageBatch = new SpriteBatch(2048);

        this.stage = new Stage(new ScreenViewport());
        this.ui = new Stage(new ScreenViewport(), stageBatch);

        //this.ui = new Stage(new StretchViewport(480,270), stageBatch);
        //this.stage = new Stage( 480, 270, true);
        //this.ui = new Stage(480, 270, true, stageBatch);
        this.stageCamera = (OrthographicCamera) stage.getCamera();
    }

    @Override
    public void show() {
        final InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(ui);
        multiplexer.addProcessor(createStageMultiplexer());

        InputProcessor backInputProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
//                if (keycode == Input.Keys.P) {
//                    HeavyDefenseGame.captureScreen();
//                }
//
//                if (keycode == Input.Keys.R) {
//                    HeavyDefenseGame.toggleRecording();
//                }

//                if (keycode == Input.Keys.BACK) {
//                    if (handleBack()) {
//                        onBack();
//                    }
//                    return true;
//                }

                return false;
            }
        };
        multiplexer.addProcessor(backInputProcessor);

        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!paused) {
            stage.act(delta * speed);
        }
        stage.draw();

        ui.act(delta);
        ui.draw();

    }

    @Override
    public void resize(int width, int height) {
        float gameWidth = width;
        float gameHeight = height;

        while (gameWidth >= 800) {
            gameWidth /= 2;
            gameHeight /= 2;
        }

        //ui.setViewport(gameWidth, gameHeight, true);

        stage.setViewport(new ScreenViewport());
        stageCamera.setToOrtho(true, gameWidth, gameHeight);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    protected InputMultiplexer createStageMultiplexer() {
        return new InputMultiplexer(stage);
    }

    protected boolean handleBack() {
        return true;
    }

    public Stage getUi() {
        return ui;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }
}
