package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.diegrafen.exmatrikulatortd.model.ObservableUnit;

import static de.diegrafen.exmatrikulatortd.util.Assets.*;

/**
 * Das Spielobjekt eines Turms
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:03
 */
public class TowerObject extends BaseObject {

    private Animation<TextureRegion> attackLeftAnimation;

    private Animation<TextureRegion> attackRightAnimation;

    private Animation<TextureRegion> idleRightAnimation;

    private Animation<TextureRegion> idleLeftAnimation;

    private boolean attacking;

    private float animationTime = 0;

    private boolean lookingLeft = false;

    public TowerObject(ObservableUnit observableUnit, AssetManager assetManager) {
        super(observableUnit, assetManager);
    }

    /**
     * Initialisiert die Darstellung des Spielobjektes
     */
    @Override
    void initializeSprite() {
        super.initializeSprite();

        String assetsName = getAssetsName();
        setTextureAtlas(getAssetManager().get(getTowerAssetPath(assetsName), TextureAtlas.class));

        idleLeftAnimation = new Animation<>(0.10f, getTextureAtlas().findRegions(assetsName + "_idleLeft"), Animation.PlayMode.LOOP);
        idleRightAnimation = new Animation<>(0.10f, getTextureAtlas().findRegions(assetsName + "_idleRight"), Animation.PlayMode.LOOP);

        attackLeftAnimation = new Animation<>(0.10f, getTextureAtlas().findRegions(assetsName + "_attackLeft"), Animation.PlayMode.LOOP);
        attackRightAnimation = new Animation<>(0.10f, getTextureAtlas().findRegions(assetsName + "_attackRight"), Animation.PlayMode.LOOP);
    }

    @Override
    public void update() {
        super.update();
        if (getObservable() != null) {
            attacking = getObservable().isAttacking();
        }

        lookingLeft = getxPosition() - getxTargetPosition() > 0;

    }

    /**
     * Zeichnet das Objekt auf dem Bildschirm
     *
     * @param spriteBatch Der spriteBatch, mit dem Objekt gerendert wird
     */
    @Override
    public void draw(SpriteBatch spriteBatch, float deltaTime) {
        super.draw(spriteBatch, deltaTime);

        if (isPlayDeathAnimation()) {
            removeObjectFromGame();
            return;
        }

        if (isAnimated()) {
            setStateTime(getStateTime() + deltaTime);
        }

        TextureRegion currentFrame;

        if (attacking) {
            if (isAnimated()) {
                animationTime += deltaTime;
            }
            if (lookingLeft) {
                currentFrame = attackLeftAnimation.getKeyFrame(animationTime);
            } else {
                currentFrame = attackRightAnimation.getKeyFrame(animationTime);
            }
            if (attackRightAnimation.isAnimationFinished(animationTime) || attackLeftAnimation.isAnimationFinished(animationTime)) {
                attacking = false;
                animationTime = 0;
            }
        } else {
            if (lookingLeft) {
                currentFrame = idleLeftAnimation.getKeyFrame(getStateTime(), true);
            } else {
                currentFrame = idleRightAnimation.getKeyFrame(getStateTime(), true);
            }
        }


        spriteBatch.draw(currentFrame, getxPosition() + (32 - currentFrame.getRegionWidth() / 2),
                getyPosition() + (32 - currentFrame.getRegionHeight() / 2));

    }

    /**
     * Entfernt das Spielobjekt
     */
    @Override
    public void dispose() {
        super.dispose();
    }


}
