package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.diegrafen.exmatrikulatortd.model.Observable;
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
    private static final int FRAME_COLS = 4, FRAME_ROWS = 4;

    // Objects used
    Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    Texture walkSheet;

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

    public EnemyObject(Observable observable) {
        super(observable);

        walkSheet = new Texture(Gdx.files.internal("sprites/objects/enemies/HazMatHarry1@64x64.png"));

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_ROWS,
                walkSheet.getHeight() / FRAME_COLS);

        //System.out.println(walkSheet.getWidth());
        //System.out.println(walkSheet.getHeight());

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * 1];//[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<TextureRegion>(0.025f, walkFrames);

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
        //spriteBatch.draw(currentFrame, getxPosition(), getyPosition());
        spriteBatch.draw(getCurrentSprite(), getxPosition(), getyPosition());
    }

    /**
     * Entfernt das Spielobjekt
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}
