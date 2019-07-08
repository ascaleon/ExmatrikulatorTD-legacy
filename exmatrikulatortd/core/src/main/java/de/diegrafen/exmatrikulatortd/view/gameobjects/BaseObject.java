package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.compression.lzma.Base;
import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Observable;
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
        //this.currentSprite = new Texture(observable.getAssetsName());
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
            setxPosition(observable.getxPosition());
            setyPosition(observable.getyPosition());
            setxTargetPosition(observable.getTargetxPosition());
            setyTargetPosition(observable.getTargetyPosition());
        }
    }

    void removeObject() {
        dispose();
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

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public void setTextureAtlas(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public Texture getCurrentSprite() {
        return currentSprite;
    }

    public void setCurrentSprite(Texture currentSprite) {
        this.currentSprite = currentSprite;
    }

    public float getxPosition() {
        return xPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNewPosition(float xPosition, float yPosition) {
        this.xPosition = xPosition - currentSprite.getWidth() / 2;
        this.yPosition = yPosition - currentSprite.getHeight() / 2;
    }

    @Override
    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public float getxTargetPosition() {
        return xTargetPosition;
    }

    public void setxTargetPosition(float xTargetPosition) {
        this.xTargetPosition = xTargetPosition;
    }

    public float getyTargetPosition() {
        return yTargetPosition;
    }

    public void setyTargetPosition(float yTargetPosition) {
        this.yTargetPosition = yTargetPosition;
    }

    public boolean isPlayDeathAnimation() {
        return playDeathAnimation;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public boolean isAnimated() {
        return animated;
    }

    @Override
    public void setAnimated(boolean animated) {
        this.animated = animated;
    }
}
