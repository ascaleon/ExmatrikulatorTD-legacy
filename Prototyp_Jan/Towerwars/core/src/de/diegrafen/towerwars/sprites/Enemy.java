package de.diegrafen.towerwars.sprites;

import com.badlogic.gdx.graphics.Texture;

public class Enemy extends Sprite {

    private int armor;

    private int speed;

    private int hitPoints;

    private float regenerationRate;

    public Enemy(int xPosition, int yPosition, Texture texture) {
        super(xPosition, yPosition, texture);
    }

    @Override
    public void update(float deltaTime) {

    }
}
