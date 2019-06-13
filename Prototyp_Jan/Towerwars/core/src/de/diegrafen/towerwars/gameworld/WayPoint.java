package de.diegrafen.towerwars.gameworld;

import com.badlogic.gdx.math.Vector2;

public class WayPoint {

    private Vector2 position;
    private int index;
    private float width;
    private float height;

    public WayPoint() {
    }

    public WayPoint(Vector2 position, float width, float height, int index) {
        super();
        this.position = position;
        this.index = index;
        this.width = width;
        this.height = height;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getIndex() {
        return index;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public WayPoint copy() {
        return new WayPoint(position, width, height, index);
    }
}
