package de.diegrafen.exmatrikulatortd.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import de.diegrafen.exmatrikulatortd.controller.Controller;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Gamestates")
public class Gamestate extends BaseModel {

    static final long serialVersionUID = 48546846516547L;

    String mapName;

    @OneToMany(mappedBy="gameState")
    private List<Player> player;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Coordinates> wayPoints;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Coordinates> collisionMatrix;

    @Enumerated(EnumType.ORDINAL)
    private Difficulty difficulty;

    private int roundNumber;

    private int numberOfRounds;

    private Wave wave;

    private boolean isActive;

    public Gamestate () {

    }

    public void initializeGameState (Controller controller) {

        this.isActive = true;
    }

}
