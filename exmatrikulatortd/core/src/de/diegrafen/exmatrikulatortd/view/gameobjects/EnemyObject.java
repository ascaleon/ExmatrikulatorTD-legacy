package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

/**
 *
 * Das Spielobjekt eines Gegners
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:03
 */
public class EnemyObject extends BaseObject {


    private float movementSpeed = 10;

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

    public EnemyObject(Enemy enemy) {
        super(enemy.getName(), enemy.getAssetsName(), enemy.getxPosition(), enemy.getyPosition());
    }

    /**
     * Update-Methode. Aktualisiert den Zustand des Objektes
     *
     * @param deltaTime Die Zeit zwischen zwei Frames
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //setxPosition(getxPosition() + movementSpeed * deltaTime);
    }

    /**
     * Zeichnet das Objekt auf dem Bildschirm
     *
     * @param spriteBatch Der spriteBatch, mit dem Objekt gerendert wird
     */
    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
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
