package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.diegrafen.exmatrikulatortd.model.ObservableUnit;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

/**
 * Das Spielobjekt eines Gegners
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:03
 */
public class EnemyObject extends BaseObject {

    private Texture walkSheet;

    private static final int FRAME_COLS = 3, FRAME_ROWS = 4;

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

        //standing = new Animation<>(0.033f, getTextureAtlas().findRegions(getAssetsName() + "standing"), Animation.PlayMode.LOOP);

        //runLeft = new Animation<>(0.033f, getTextureAtlas().findRegions(getAssetsName() + "runLeft"), Animation.PlayMode.LOOP);

        //runRight = new Animation<>(0.033f, getTextureAtlas().findRegions(getAssetsName() + "runRight"), Animation.PlayMode.LOOP);

        //runUp = new Animation<>(0.033f, getTextureAtlas().findRegions(getAssetsName() + "runUp"), Animation.PlayMode.LOOP);

        //runDown = new Animation<>(0.033f, getTextureAtlas().findRegions(getAssetsName() + "runDown"), Animation.PlayMode.LOOP);

        //die = new Animation<>(0.033f, getTextureAtlas().findRegions(getAssetsName() + "die"), Animation.PlayMode.LOOP);

        // TODO: Von Spritesheet auf TextureAtlas umsteigen

        walkSheet = new Texture(getAssetsName());

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkLeftFrames = new TextureRegion[FRAME_COLS];
        TextureRegion[] walkRightFrames = new TextureRegion[FRAME_COLS];
        TextureRegion[] walkUpFrames = new TextureRegion[FRAME_COLS];
        TextureRegion[] walkDownFrames = new TextureRegion[FRAME_COLS];

        for (int j = 0; j < FRAME_COLS; j++) {
            walkDownFrames[j] = tmp[0][j];
            walkLeftFrames[j] = tmp[1][j];
            walkRightFrames[j] = tmp[2][j];
            walkUpFrames[j] = tmp[3][j];
        }

        walkDownAnimation = new Animation<>(0.25f, walkDownFrames);
        walkLeftAnimation = new Animation<>(0.25f, walkLeftFrames);
        walkRightAnimation = new Animation<>(0.25f, walkRightFrames);
        walkUpAnimation = new Animation<>(0.25f, walkUpFrames);

        walkDownAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        walkLeftAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        walkRightAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        walkUpAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
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

        setStateTime(getStateTime() + deltaTime);

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
        walkSheet.dispose();
    }
}
