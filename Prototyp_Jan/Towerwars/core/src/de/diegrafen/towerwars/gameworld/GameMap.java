package de.diegrafen.towerwars.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import de.diegrafen.towerwars.sprites.Enemy;
import de.diegrafen.towerwars.sprites.Tower;

import static de.diegrafen.towerwars.Towerwars.GRID_SIZE;

public class GameMap extends Group {

    private OrthographicCamera camera;


    private transient OrthogonalTiledMapRenderer mapRenderer;
    //private transient ShapeRenderer shapeRenderer;
    private transient TiledMap map;

    private transient Group mapGroup = new Group();
    private transient Group enemiesGroup = new Group();
    private transient Group towersGroup = new Group();
    private transient Group overlayGroup = new Group();

    private Array<Actor> mapActors = new Array<Actor>();
    private Array<Enemy> enemies = new Array<Enemy>();
    private Array<Tower> towers = new Array<Tower>();
    private Array<Actor> overlayActors = new Array<Actor>();

    private transient boolean[][] collisionMap;


    public GameMap () {

        final TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        //parameters.flipY = false;

        TmxMapLoader mapLoader = new TmxMapLoader();
        this.map = mapLoader.load("prototypeMap.tmx", parameters);
        this.mapRenderer = new OrthogonalTiledMapRenderer(map);



        setHeight(map.getProperties().get("height", Integer.class) * 64); //HeavyDefenseGame.TILE_SIZE);
        setWidth(map.getProperties().get("width", Integer.class) * 64); //HeavyDefenseGame.TILE_SIZE);

        addActor(mapGroup);
        addActor(towersGroup);
        addActor(enemiesGroup);
        addActor(overlayGroup);

    }

    public void render (OrthographicCamera camera) {

//        mapRenderer.getBatch().setProjectionMatrix(camera.combined);
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        camera.update();
//        mapRenderer.setView(camera);
//        mapRenderer.render();

    };

    public void update () {

    };

    public boolean canBuildTower(float x, float y) {
        final int gridX = (int) (x / GRID_SIZE);
        final int gridY = (int) (y / GRID_SIZE);

        // check out of bounds
        if (gridX < 0 || gridY < 0 || gridX > collisionMap.length || gridY > collisionMap[gridX].length) return false;

        return !collisionMap[gridX][gridY];
    }


    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        System.out.println("Draw me!");
        // update the alpha value of the spritebatch
        final Color batchColor = batch.getColor();
        batch.setColor(batchColor.a, batch.getColor().g, batch.getColor().b, getColor().a);
        final Color mapBatchColor = mapRenderer.getBatch().getColor();
        mapRenderer.getBatch().setColor(mapBatchColor.a, mapRenderer.getBatch().getColor().g, mapRenderer.getBatch().getColor().b,
                getColor().a);

        // draw map
        mapRenderer.setView(camera);
        mapRenderer.render();
        batch.end();


        batch.begin();
    }

    public void addEnemy(Enemy enemy) {
        enemiesGroup.addActor(enemy);
        enemies.add(enemy);

        enemiesGroup.getChildren().sort();
    }
}