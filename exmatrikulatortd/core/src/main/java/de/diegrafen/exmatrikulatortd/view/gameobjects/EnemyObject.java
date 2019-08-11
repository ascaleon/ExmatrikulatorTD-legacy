package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.diegrafen.exmatrikulatortd.model.enemy.ObservableEnemy;

import static de.diegrafen.exmatrikulatortd.util.Assets.*;
import static de.diegrafen.exmatrikulatortd.util.Assets.getEnemyAssetPath;

/**
 * Das Spielobjekt eines Gegners
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:03
 */
public class EnemyObject extends BaseObject {

    private Animation<TextureRegion> walkRightAnimation;

    private Animation<TextureRegion> walkLeftAnimation;

    private Animation<TextureRegion> walkUpAnimation;

    private Animation<TextureRegion> walkDownAnimation;

    private Animation<TextureRegion> standingAnimation;

    private Animation<TextureRegion> deathAnimation;

    private int healthBarMaxWitdth = 64;

    private int currentHealthBarWidth = healthBarMaxWitdth;

    private Texture greenHealthBar;
        
    private Texture redHealthBar;

    public EnemyObject(ObservableEnemy observableUnit, AssetManager assetManager) {
        super(observableUnit, assetManager);
    }

    /**
     * Initialisiert die Darstellung des Spielobjektes
     */
    @Override
    void initializeSprite() {
        super.initializeSprite();
        //dies ist eine änderung

        String assetsName = getAssetsName();
        TextureAtlas deathAnimationAtlas = getAssetManager().get(getEnemyAssetPath(DEATH_ANIMATION_ASSETS), TextureAtlas.class);
        setTextureAtlas(getAssetManager().get(getEnemyAssetPath(assetsName), TextureAtlas.class));

        //standingAnimation = new Animation<>(0.033f, getTextureAtlas().findRegions(getAssetsName() + "standing"), Animation.PlayMode.LOOP);
        walkLeftAnimation = new Animation<>(0.25f, getTextureAtlas().findRegions(assetsName + "_moveLeft"), Animation.PlayMode.LOOP_PINGPONG);
        walkRightAnimation = new Animation<>(0.25f, getTextureAtlas().findRegions(assetsName + "_moveRight"), Animation.PlayMode.LOOP_PINGPONG);
        walkUpAnimation = new Animation<>(0.25f, getTextureAtlas().findRegions(assetsName + "_moveUp"), Animation.PlayMode.LOOP_PINGPONG);
        walkDownAnimation = new Animation<>(0.25f, getTextureAtlas().findRegions(assetsName + "_moveDown"), Animation.PlayMode.LOOP_PINGPONG);
        deathAnimation = new Animation<>(0.125f, deathAnimationAtlas.findRegions("explosion"), Animation.PlayMode.LOOP);

        greenHealthBar = new Texture(createProceduralPixmap(0,1));
        redHealthBar = new Texture(createProceduralPixmap(1,0));
    }

    /**
     * Update-Methode. Aktualisiert den Zustand des Objektes durch Benachrichtigung eines beobachteten Daten-Objekts
     */
    public void update() {
        super.update();
        if (getObservable() != null) {
            currentHealthBarWidth = (int) (healthBarMaxWitdth * getObservable().getCurrentHitPoints() / getObservable().getCurrentMaxHitPoints());
        } else {
            currentHealthBarWidth = 0;
            healthBarMaxWitdth = 0;
        }
    }

    /**
     * Zeichnet das Objekt auf dem Bildschirm
     *
     * @param spriteBatch Der spriteBatch, mit dem Objekt gerendert wird
     */
    @Override
    public void draw(SpriteBatch spriteBatch, float deltaTime) {
        super.draw(spriteBatch, deltaTime);

        TextureRegion currentFrame;

        if (isAnimated()) {
            setStateTime(getStateTime() + deltaTime);
        }

        if (isPlayDeathAnimation()) {

            if (!deathAnimation.isAnimationFinished(getStateTime())) {
                currentFrame = deathAnimation.getKeyFrame(getStateTime());
            } else {
                removeObjectFromGame();
                return;
            }

        } else {

            double angle = (Math.atan2(getyTargetPosition() - getyPosition(), getxTargetPosition() - getxPosition()) * 180 / Math.PI) + 180;

            if (angle >= 135 & angle < 225) {
                currentFrame = walkRightAnimation.getKeyFrame(getStateTime(), true);
            } else if (angle >= 225 & angle < 315) {
                currentFrame = walkUpAnimation.getKeyFrame(getStateTime(), true);
            } else if (angle >= 45 & angle < 135) {
                currentFrame = walkDownAnimation.getKeyFrame(getStateTime(), true);
            } else {
                currentFrame = walkLeftAnimation.getKeyFrame(getStateTime(), true);
            }
        }

        spriteBatch.draw(currentFrame, getxPosition()+ (32-currentFrame.getRegionWidth()/2), getyPosition());
        int healthBarHeight = 10;
        spriteBatch.draw(redHealthBar,getxPosition(),getyPosition() + 64, healthBarMaxWitdth, healthBarHeight);
        spriteBatch.draw(greenHealthBar,getxPosition(),getyPosition() + 64, currentHealthBarWidth, healthBarHeight);
    }

    private Pixmap createProceduralPixmap (int r, int g) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        pixmap.setColor(r, g, 0, 1);
        pixmap.fill();

        return pixmap;
    }

    @Override
    ObservableEnemy getObservable() {
        return (ObservableEnemy) super.getObservable();
    }
}
