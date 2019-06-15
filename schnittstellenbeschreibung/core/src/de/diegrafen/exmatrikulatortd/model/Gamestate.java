package de.diegrafen.exmatrikulatortd.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Gamestates")
public class Gamestate extends BaseModel {

    static final long serialVersionUID = 48546846516547L;

    String mapName;

    /**
     * Die Spielerinnennummer der lokalen Spielinstanz. Hierüber lässt sich auf die jeweiligen Spielinformationen zugreifen.
     */
    private transient int localPlayerNumber;


    /**
     * Die Spielerinnen. Umfasst im Singleplayer-Modus ein Element und im Multiplayer-Modus zwei Elemente.
     */
    @OneToMany(mappedBy="gameState")
    private List<Player> player;

    @OneToMany(mappedBy="gameState")
    private List<Coordinates> collisionMatrix;

    @Enumerated(EnumType.ORDINAL)
    private Difficulty difficulty;

    private int roundNumber;

    private int numberOfRounds;

    private boolean isActive;

    public Gamestate () {

    }

    public void initializeSinglePlayerGameState (Difficulty difficulty) {

    }

}
