package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.diegrafen.exmatrikulatortd.model.ObservableUnit;

public class ProjectileObject extends BaseObject {

    public ProjectileObject(ObservableUnit observableUnit) {
        super(observableUnit);
    }

    /**
     * Zeichnet das Objekt auf dem Bildschirm
     *
     * @param spriteBatch Der spriteBatch, mit dem Objekt gerendert wird
     */
    @Override
    public void draw (SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        spriteBatch.draw(getCurrentSprite(), getxPosition(), getyPosition());
    }
}
