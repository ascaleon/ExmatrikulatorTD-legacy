package de.diegrafen.towerwars.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.diegrafen.towerwars.Towerwars;
import de.diegrafen.towerwars.sprites.Tower;

public class PlayState extends State {

    private Tower tower;

    private Texture background;

    public PlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
        camera.setToOrtho(false, Towerwars.WIDTH / 2, Towerwars.HEIGHT / 2);
        tower = new Tower(50, 100);
        background = new Texture("background.png");
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            tower.jump();
        }

    }

    @Override
    public void update(float deltaTime) {
        handleInput();
        tower.update(deltaTime);

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background, camera.position.x - camera.viewportWidth / 2, 0);
        spriteBatch.draw(tower.getTexture(), tower.getPosition().x, tower.getPosition().y);
        spriteBatch.end();

    }

    @Override
    public void dispose() {

    }
}
