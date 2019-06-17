package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * Interface für Spielobjekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:13
 */
public interface GameObject {

    /**
     * Update-Methode. Aktualisiert den Zustand des Objektes
     * @param deltaTime Die Zeit zwischen zwei Frames
     */
    void update(float deltaTime);

    /**
     * Zeichnet das Objekt auf dem Bildschirm
     * @param spriteBatch Der spriteBatch, mit dem Objekt gerendert wird
     */
    void draw(SpriteBatch spriteBatch);

    /**
     * Entfernt das Spielobjekt
     */
    void dispose();
}
