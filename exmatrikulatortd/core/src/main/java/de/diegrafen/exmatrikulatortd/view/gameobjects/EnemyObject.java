package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.diegrafen.exmatrikulatortd.model.ObservableUnit;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import static de.diegrafen.exmatrikulatortd.util.Assets.ENEMY_SPRITE_PATH;
import static de.diegrafen.exmatrikulatortd.util.Assets.FIREBALL_ASSETS;

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

    /**
     * Konstruktor für Gegner-Objekte
     *
     * @param name       Der Name des Spielobjektes
     * @param assetsName Die mit dem Objekt assoziierten Assets
     * @param xPosition  Die x-Position
     * @param yPosition  Die y-Position
     */
    public EnemyObject(String name, String assetsName, float xPosition, float yPosition) {
        super(name, assetsName, xPosition, yPosition);
    }

    public EnemyObject(ObservableUnit observableUnit) {
        super(observableUnit);

        String assetsName = observableUnit.getAssetsName();

        setTextureAtlas(new TextureAtlas(ENEMY_SPRITE_PATH + assetsName + ".atlas"));

        //standing = new Animation<>(0.033f, getTextureAtlas().findRegions(getAssetsName() + "standing"), Animation.PlayMode.LOOP);

        walkLeftAnimation = new Animation<>(0.25f, getTextureAtlas().findRegions(assetsName + "_moveLeft"), Animation.PlayMode.LOOP_PINGPONG);

        walkRightAnimation = new Animation<>(0.25f, getTextureAtlas().findRegions(assetsName + "_moveRight"), Animation.PlayMode.LOOP_PINGPONG);

        walkUpAnimation = new Animation<>(0.25f, getTextureAtlas().findRegions(assetsName + "_moveUp"), Animation.PlayMode.LOOP_PINGPONG);

        walkDownAnimation = new Animation<>(0.25f, getTextureAtlas().findRegions(assetsName + "_moveDown"), Animation.PlayMode.LOOP_PINGPONG);

        //die = new Animation<>(0.033f, getTextureAtlas().findRegions(getAssetsName() + "die"), Animation.PlayMode.LOOP);
    }

    /**
     * Update-Methode. Aktualisiert den Zustand des Objektes durch Benachrichtigung eines beobachteten Daten-Objekts
     */
    public void update() {
        super.update();
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

        double angle = (Math.atan2(getyTargetPosition() - getyPosition(), getxTargetPosition() - getxPosition()) * 180 / Math.PI) + 180;

        if (isAnimated()) {
            setStateTime(getStateTime() + deltaTime);
        }

        TextureRegion currentFrame;

        if (angle >= 135 & angle < 225) {
            currentFrame = walkRightAnimation.getKeyFrame(getStateTime(), true);
        } else if (angle >= 225 & angle < 315) {
            currentFrame = walkUpAnimation.getKeyFrame(getStateTime(), true);
        } else if (angle >= 45 & angle < 135) {
            currentFrame = walkDownAnimation.getKeyFrame(getStateTime(), true);
        } else {
            currentFrame = walkLeftAnimation.getKeyFrame(getStateTime(), true);
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
