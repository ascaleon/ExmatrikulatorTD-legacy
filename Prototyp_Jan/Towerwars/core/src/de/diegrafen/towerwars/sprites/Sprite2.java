package de.diegrafen.towerwars.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public abstract class Sprite2 {

    protected Vector3 position;

    protected Texture texture;

    public Sprite2(int xPosition, int yPosition, Texture texture) {
        this.position = new Vector3(xPosition, yPosition, 0);
        this.texture = texture;
    }

    public abstract void update(float deltaTime);
}
