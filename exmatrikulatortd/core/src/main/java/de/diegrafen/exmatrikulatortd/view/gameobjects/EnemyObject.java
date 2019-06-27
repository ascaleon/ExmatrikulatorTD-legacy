package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.diegrafen.exmatrikulatortd.model.ObservableUnit;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

/**
 *
 * Das Spielobjekt eines Gegners
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:03
 */
public class EnemyObject extends BaseObject {

    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 3, FRAME_ROWS = 4;

    // Objects used
    Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)

    Texture walkSheet;

    private Animation<TextureRegion> standing;

    private Animation<TextureRegion> runLeft;

    private Animation<TextureRegion> runRight;

    private Animation<TextureRegion> runUp;

    private Animation<TextureRegion> runDown;

    private Animation<TextureRegion> die;

    // A variable for tracking elapsed time for the animation
    float stateTime;

    /**
     * Konstruktor für Gegner-Objekte
     * @param name Der Name des Spielobjektes
     * @param assetsName Die mit dem Objekt assoziierten Assets
     * @param xPosition Die x-Position
     * @param yPosition Die y-Position
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

        walkSheet = getCurrentSprite();

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        //System.out.println(walkSheet.getWidth());
        //System.out.println(walkSheet.getHeight());

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<TextureRegion>(0.25f, walkFrames);

        // Reset the elapsed animation time to 0
        stateTime = 0f;
    }

    /**
     * Update-Methode. Aktualisiert den Zustand des Objektes
     */
    public void update() {
        super.update();
        //setxPosition(getxPosition() + movementSpeed * deltaTime);
    }

    /**
     * Zeichnet das Objekt auf dem Bildschirm
     *
     * @param spriteBatch Der spriteBatch, mit dem Objekt gerendert wird
     */
    @Override
    public void draw (SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        spriteBatch.draw(currentFrame, getxPosition(), getyPosition());
        //spriteBatch.draw(getCurrentSprite(), getxPosition(), getyPosition());
    }

    /**
     * Entfernt das Spielobjekt
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}
