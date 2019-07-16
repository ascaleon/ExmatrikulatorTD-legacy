package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.diegrafen.exmatrikulatortd.model.Observable;
import de.diegrafen.exmatrikulatortd.view.Observer;

/**
 * Interface für Spielobjekte. Wendet das Observer-Pattern an, mit dem Spielobjekte immer genau dann aktualisiert werden,
 * wenn sich ihre Repräsentation im Datenbestand ändert.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:13
 */
public interface GameObject extends Observer {

    void setAnimated(boolean animated);

    boolean isRemoved();

    /**
     * Zeichnet das Objekt auf dem Bildschirm
     *
     * @param spriteBatch Der spriteBatch, mit dem Objekt gerendert wird
     */
    void draw(SpriteBatch spriteBatch, float deltaTime);

    /**
     * Entfernt das Spielobjekt
     */
    void dispose();

    float getyPosition();
}
