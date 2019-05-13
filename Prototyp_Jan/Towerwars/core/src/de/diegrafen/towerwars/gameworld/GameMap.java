package de.diegrafen.towerwars.gameworld;

import com.badlogic.gdx.graphics.OrthographicCamera;

public abstract class GameMap {

    public abstract void render (OrthographicCamera camera);

    public abstract void update ();

    public abstract void dispose ();
}