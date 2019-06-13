package de.diegrafen.towerwars.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import de.diegrafen.towerwars.gameworld.GameMap;
import de.diegrafen.towerwars.gameworld.WayPoint;

import java.util.ArrayList;

public class Enemy extends Group {

    private transient TextureRegion[] sprites;

    private transient TextureRegion currentSprite;

    private int textureWidth;

    private int textureHeight;

    private boolean reachedEnd = false;

    private boolean isDead = false;

    private float money;

    private int armor;

    private Vector3 velocity;

    private float currentSpeed;

    private float speed;

    private float hitPoints;

    private float maxHitPoints;

    private float regenerationRate;

    //EnemyType enemyType;

    private int targetIndex = 0;

    private WayPoint currentTarget;

    private WayPoint end;

    private Array<WayPoint> wayPoints;

    private int wayPointIndex;

    private GameMap gameMap;

    public Enemy(float x, float y, Vector3 velocity,
                 //EnemyType enemyType,
                 //ArmorType armorType,
                 Array<WayPoint> waypoints,
                 // String textureSheetName,
                 GameMap gameMap,
                 float hitPoints, float speed,
                 //int wave,
                 float money,
                 float regenerationRate) {
        //this.enemyType = enemyType;
        //this.armorType = armorType;
        //this.textureSheetName = textureSheetName;
        //this.wave = wave;
        this.gameMap = gameMap;
        this.maxHitPoints = hitPoints;
        this.hitPoints = hitPoints;
        this.regenerationRate = regenerationRate;
        this.money = money;
        this.speed = speed;
        this.velocity = velocity;
        this.wayPointIndex = 0;

        // copy nodes
        this.wayPoints = new Array<WayPoint>(waypoints.size);
        for (WayPoint wayPoint : waypoints) {
            this.wayPoints.add(wayPoint.copy());
        }

        this.currentTarget = waypoints.get(++targetIndex);
        this.end = waypoints.get(waypoints.size - 1);

        setPosition(x + currentTarget.getWidth() / 2, y + currentTarget.getHeight() / 2);
        setTouchable(Touchable.disabled);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        currentSpeed = speed;

        // move
        float targetX = currentTarget.getPosition().x - textureWidth / 2 + currentTarget.getWidth() / 2;
        float targetY = currentTarget.getPosition().y - textureHeight / 2 + currentTarget.getHeight() / 2;
        velocity.set(targetX - getX(), targetY - getY(), 0);
        velocity.nor().scl(currentSpeed);

        computeTransform().translate(velocity);

        //computeTransform().translate(velocity.x * delta, velocity.y * delta);

        //translate(velocity.x * delta, velocity.y * delta);

        float dx = getX() - targetX;
        float dy = getY() - targetY;

        // reached target node
        if (Math.hypot(dx, dy) < 3) {
            if (targetIndex < wayPoints.size - 1) {
                currentTarget = wayPoints.get(++targetIndex);
            } else {
                // at the end
                //if (!reachedEnd) {
                //fireReachedEndEvent();
                //}
                reachedEnd = true;
                //gameMap.removeEnemy(this);

            }
        }

        // update rotation
        updateRotation();
    }

    protected void updateRotation() {
        double angle = Math.toDegrees(Math.atan2(velocity.y, velocity.x));
        if (angle > -180 && angle <= -157.5) {
            // 3
            changeSprite(3);
        } else if (angle > -157.5 && angle <= -112.5) {
            // 0
            changeSprite(0);
        } else if (angle > -112.5 && angle <= -67.5) {
            // 1
            changeSprite(1);
        } else if (angle > -67.5 && angle <= -22.5) {
            // 2
            changeSprite(2);
        } else if (angle > -22.5 && angle <= 22.5) {
            // 4
            changeSprite(4);
        } else if (angle > 22.5 && angle <= 67.5) {
            // 7
            changeSprite(7);
        } else if (angle > 67.5 && angle <= 112.5) {
            // 6
            changeSprite(6);
        } else if (angle > 112.5 && angle <= 157.5) {
            // 5
            changeSprite(5);
        } else if (angle > 157.5 && angle <= 180) {
            // 3
            changeSprite(3);
        }
    }

    protected void changeSprite(int index) {
        currentSprite = sprites[index];
    }

}
