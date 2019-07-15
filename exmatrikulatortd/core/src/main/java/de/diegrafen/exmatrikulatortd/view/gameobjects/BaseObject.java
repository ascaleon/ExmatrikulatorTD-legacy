package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import de.diegrafen.exmatrikulatortd.model.ObservableUnit;

/**
 * Basisklasse für grafische Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:03
 */
public abstract class BaseObject implements GameObject {

    private ObservableUnit observable;

    /**
     * Die Texturen des Objektes
     */
    private TextureAtlas textureAtlas;

    /**
     * Der aktuelle Sprite. Relevant für Animationen
     */
    private Texture currentSprite;

    /**
     * Die aktuelle X-Position
     */
    private float xPosition;

    /**
     * Die aktuelle Y-Position
     */
    private float yPosition;

    /**
     * Die X-Ziel-Position
     */
    private float xTargetPosition;

    /**
     * Die Y-Ziel-Position
     */
    private float yTargetPosition;

    /**
     * Der Name des Spielobjektes
     */
    private String name;

    private String assetsName;

    private boolean removed;

    private boolean playDeathAnimation;

    private float stateTime = 0f;

    private boolean animated;

    BaseObject(ObservableUnit observable) {
        this.observable = observable;
        this.name = observable.getName();
        this.assetsName = observable.getAssetsName();
        this.xPosition = observable.getxPosition();
        this.yPosition = observable.getyPosition();
        this.xTargetPosition = observable.getTargetxPosition();
        this.yTargetPosition = observable.getTargetyPosition();
        this.removed = false;
        observable.registerObserver(this);
        initializeSprite();
    }

    /**
     * Konstruktor für Spiel-Objekte
     *
     * @param name       Der Name des Spielobjektes
     * @param assetsName Die mit dem Objekt assoziierten Assets
     * @param xPosition  Die x-Position
     * @param yPosition  Die y-Position
     */
    BaseObject(String name, String assetsName, float xPosition, float yPosition) {
        this.name = name;
        this.currentSprite = new Texture(assetsName);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.removed = false;
        this.animated = true;
    }

    /**
     * Update-Methode. Aktualisiert den Zustand des Objektes
     */
    @Override
    public void update() {
        if (observable.isRemoved()) {
            playDeathAnimation = true;
            observable.removeObserver(this);
            observable = null;
            stateTime = 0f;
        } else if (!assetsName.equals(observable.getAssetsName())) {
            this.assetsName = observable.getAssetsName();
            initializeSprite();
        } else {
            xPosition = observable.getxPosition();
            yPosition = observable.getyPosition();
            xTargetPosition = observable.getTargetxPosition();
            yTargetPosition = observable.getTargetyPosition();
        }
    }

    /**
     * Zeichnet das Objekt auf dem Bildschirm
     *
     * @param spriteBatch Der spriteBatch, mit dem Objekt gerendert wird
     */
    public void draw(SpriteBatch spriteBatch, float deltaTime) {

    }

    /**
     * Initialisiert die Darstellung des Spielobjektes
     */
    void initializeSprite() {

    }

    /**
     * Entfernt das Spielobjekt
     */
    public void dispose() {
        if (currentSprite != null) {
            currentSprite.dispose();
        }
        if (textureAtlas != null) {
            textureAtlas.dispose();
        }
    }

    TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    void setTextureAtlas(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    Texture getCurrentSprite() {
        return currentSprite;
    }

    void setCurrentSprite(Texture currentSprite) {
        this.currentSprite = currentSprite;
    }

    public float getxPosition() {
        return xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isRemoved() {
        return removed;
    }

    void setRemoved(boolean removed) {
        this.removed = removed;
    }

    String getAssetsName() {
        return assetsName;
    }

    float getxTargetPosition() {
        return xTargetPosition;
    }

    float getyTargetPosition() {
        return yTargetPosition;
    }

    boolean isPlayDeathAnimation() {
        return playDeathAnimation;
    }

    float getStateTime() {
        return stateTime;
    }

    void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    boolean isAnimated() {
        return animated;
    }

    @Override
    public void setAnimated(boolean animated) {
        this.animated = animated;
    }
}
