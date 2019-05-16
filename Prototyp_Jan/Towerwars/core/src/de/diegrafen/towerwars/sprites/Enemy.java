package de.diegrafen.towerwars.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Sprite {

    private int armor;

    private Vector2 velocity = new Vector2();

    private float speed = 60 * 2, gravity = 60 * 1.6f;

    private int hitPoints;

    private float regenerationRate;

    public Enemy(Sprite sprite) {
        //super(xPosition, yPosition, texture);
        super(sprite);
    }

    @Override
    public void draw (Batch spriteBatch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }

    public void update(float deltaTime) {
        velocity.y -= gravity * deltaTime;

        if (velocity.y > speed) {
            velocity.y = speed;
        } else if (velocity.y < speed) {
            velocity.y = -speed;
        }

        //System.out.println(getX());

        setX(getX() + velocity.x * deltaTime);
        setY(getY() + velocity.y * deltaTime);

        translateX(3);
    }
}
