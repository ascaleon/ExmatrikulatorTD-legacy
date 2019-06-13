package de.diegrafen.exmatrikulatortd.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import de.diegrafen.exmatrikulatortd.controller.Controller;

import java.util.Collection;
import java.util.List;

public class Gamestate {

    private Player player;

    private TiledMap worldMap;

    private List<Vector2> wayPoints;

    private Difficulty difficulty;

    private int roundNumer;

    private Wave wave;

    private boolean isActive;

    public Gamestate () {

    }

    public void initializeGameState (Controller controller) {

        this.isActive = true;
    }


}
