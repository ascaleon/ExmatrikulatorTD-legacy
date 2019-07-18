package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.diegrafen.exmatrikulatortd.model.ObservableUnit;

import static de.diegrafen.exmatrikulatortd.util.Assets.FIREBALL_ASSETS;
import static de.diegrafen.exmatrikulatortd.util.Assets.getProjectileAssetPath;

public class ProjectileObject extends BaseObject {

    private Animation<TextureRegion> flyingAnimation;

    private Animation<TextureRegion> deathAnimation;

    public ProjectileObject(ObservableUnit observableUnit, AssetManager assetManager) {
        super(observableUnit, assetManager);
    }

    /**
     * Initialisiert die Darstellung des Spielobjektes
     */
    @Override
    void initializeSprite() {
        super.initializeSprite();
        setTextureAtlas(getAssetManager().get(getProjectileAssetPath(getAssetsName()), TextureAtlas.class));

        flyingAnimation = new Animation<>(0.033f, getTextureAtlas().findRegions("fireball"), Animation.PlayMode.LOOP);
        deathAnimation = new Animation<>(0.033f, getTextureAtlas().findRegions("fireball_die"));
    }

    /**
     * Zeichnet das Objekt auf dem Bildschirm
     *
     * @param spriteBatch Der spriteBatch, mit dem Objekt gerendert wird
     */
    @Override
    public void draw(SpriteBatch spriteBatch, float deltaTime) {
        super.draw(spriteBatch, deltaTime);


        double angle = (Math.atan2(getyTargetPosition() - getyPosition(), getxTargetPosition() - getxPosition()) * 180 / Math.PI) + 90;

        TextureRegion currentFrame;

        if (isAnimated()) {
            setStateTime(getStateTime() + deltaTime);
        }

        if (!isPlayDeathAnimation()) {
            currentFrame = flyingAnimation.getKeyFrame(getStateTime(), true);
        } else if (!deathAnimation.isAnimationFinished(getStateTime())) {
            currentFrame = deathAnimation.getKeyFrame(getStateTime());
        } else {
            removeObjectFromGame();
            return;
        }

        spriteBatch.draw(currentFrame, getxPosition(), getyPosition(),
                currentFrame.getRegionWidth() / 2.0f,
                currentFrame.getRegionHeight() / 2.0f, currentFrame.getRegionWidth(),
                currentFrame.getRegionHeight(), 1f, 1f, (float) angle, false);
    }
}
