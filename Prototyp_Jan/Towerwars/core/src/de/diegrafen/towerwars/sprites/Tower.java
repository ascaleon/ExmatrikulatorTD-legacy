package de.diegrafen.towerwars.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Tower extends Group {

    private final static int GRAVITY = -15;

    private Vector3 velocity;

    private float attackSpeed;

    private int attackRange;

    private int damage;

    private int price;

    private int level;

    public Tower(int xPosition, int yPosition) {
        //super(xPosition, yPosition, new Texture("badlogic.jpg"));
        velocity = new Vector3(0, 0, 0);
    }
}
