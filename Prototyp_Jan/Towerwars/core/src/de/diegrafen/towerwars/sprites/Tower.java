package de.diegrafen.towerwars.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Tower extends Sprite2 {
    private final static int GRAVITY = -15;

    private Vector3 velocity;

    private float attackSpeed;

    private int attackRange;

    private int damage;

    private int price;

    private int level;

    public Tower(int xPosition, int yPosition) {
        super(xPosition, yPosition, new Texture("badlogic.jpg"));
        velocity = new Vector3(0, 0, 0);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public void update(float deltaTime) {
        if (position.y > 0) {
            velocity.add(0, GRAVITY, 0);
        }
        velocity.scl(deltaTime);
        position.add(0, velocity.y, 0);
        if (position.y < 0) {
            position.y = 0;
        }

        velocity.scl(1 / deltaTime);
    }

    public void jump() {
        velocity.y = 250;
    }
}
