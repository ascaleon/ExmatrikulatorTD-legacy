package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.compression.lzma.Base;
import de.diegrafen.exmatrikulatortd.model.BaseModel;

/**
 *
 * Basisklasse für grafische Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:03
 */
public abstract class BaseObject implements GameObject {

    /**
     * Die Texturen des Objektes
     */
    private TextureAtlas textureAtlas;

    /**
     * Der aktuelle Sprite. Relevant für Animationen
     */
    private TextureRegion currentSprite;

    /**
     * Die aktuelle X-Position
     */
    private float xPosition;

    /**
     * Die aktuelle Y-Position
     */
    private float yPosition;

    /**
     * Der Name des Spielobjektes
     */
    private String name;

    /**
     * Konstruktor für Spiel-Objekte
     * @param name Der Name des Spielobjektes
     * @param assetsName Die mit dem Objekt assoziierten Assets
     * @param xPosition Die x-Position
     * @param yPosition Die y-Position
     */
    public BaseObject(String name, String assetsName, float xPosition, float yPosition) {

    }

    /**
     * Update-Methode. Aktualisiert den Zustand des Objektes
     * @param deltaTime Die Zeit zwischen zwei Frames
     */
    public void update(float deltaTime) {

    }

    /**
     * Zeichnet das Objekt auf dem Bildschirm
     * @param spriteBatch Der spriteBatch, mit dem Objekt gerendert wird
     */
    public void draw(SpriteBatch spriteBatch) {

    }

    /**
     * Initialisiert die Darstellung des Spielobjektes
     */
    private void initializeSprite () {

    }

    /**
     * Entfernt das Spielobjekt
     */
    public void dispose() {

    };

}
