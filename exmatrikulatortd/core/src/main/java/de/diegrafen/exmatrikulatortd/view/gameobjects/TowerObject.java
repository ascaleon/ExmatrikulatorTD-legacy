package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.diegrafen.exmatrikulatortd.model.ObservableUnit;
import static de.diegrafen.exmatrikulatortd.util.Assets.TOWER_SPRITE_PATH;

/**
 *
 * Das Spielobjekt eines Turms
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:03
 */
public class TowerObject extends BaseObject {

    private Animation<TextureRegion> attackLeftAnimation;

    private Animation<TextureRegion> attackRightAnimation;

    private Animation<TextureRegion> idleRightAnimation;

    //private Animation<TextureRegion> idleLeftAnimation;

    private boolean attacking;


    public TowerObject (ObservableUnit observableUnit) {
        super(observableUnit);
    }

    /**
     * Initialisiert die Darstellung des Spielobjektes
     */
    @Override
    void initializeSprite() {
        super.initializeSprite();

        String assetsName = getAssetsName();

        setTextureAtlas(new TextureAtlas(TOWER_SPRITE_PATH + assetsName + ".atlas"));

        idleRightAnimation = new Animation<>(0.10f, getTextureAtlas().findRegions(assetsName + "_idle"), Animation.PlayMode.LOOP);

        attackRightAnimation = new Animation<>(0.10f, getTextureAtlas().findRegions(assetsName + "_attackRight"), Animation.PlayMode.LOOP);
    }

    /**
     * Update-Methode. Aktualisiert den Zustand des Objektes
     *
     * @param deltaTime Die Zeit zwischen zwei Frames
     */
    public void update (float deltaTime) {
        super.update();
    }

    @Override
    public void update() {
        super.update();
        attacking = getObservable().isAttacking();
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
            setRemoved(true);
            return;
        }

        if (isAnimated()) {
            setStateTime(getStateTime() + deltaTime);
        }

        TextureRegion currentFrame;

        if (attacking){
            currentFrame = attackRightAnimation.getKeyFrame(getStateTime());
        } else {
            currentFrame = idleRightAnimation.getKeyFrame(getStateTime(), true);
        }

        spriteBatch.draw(currentFrame, getxPosition(), getyPosition());

    }

    /**
     * Entfernt das Spielobjekt
     */
    @Override
    public void dispose() {
        super.dispose();
    }


}
