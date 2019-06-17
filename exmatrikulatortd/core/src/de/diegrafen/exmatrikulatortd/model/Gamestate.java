package de.diegrafen.exmatrikulatortd.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.*;

/**
 *
 * Der Spielzustand. Verwaltet alle spielrelevanten Informationen über das Spielfeld, die Spieler, die Türme,
 * Angriffswellen und Gegner.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:36
 */
@Entity
@Table(name = "Gamestates")
public class Gamestate extends BaseModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 48546846516547L;

    /**
     * Der Name der Map
     */
    private String mapName;

    /**
     * Die Spielerinnennummer der lokalen Spielinstanz. Hierüber lässt sich auf die jeweiligen Spielinformationen zugreifen.
     */
    private transient int localPlayerNumber;


    /**
     * Die Spielerinnen. Umfasst im Singleplayer-Modus ein Element und im Multiplayer-Modus zwei Elemente.
     */
    @OneToMany(mappedBy="gameState")
    private List<Player> players;

    /**
     * Die Kollisionsmatrix, mit der bestimmt wird, ob ein Turm an einer bestimmten Stelle auf dem Spielfeld gebaut werden kann
     */
    @OneToMany(mappedBy="gameState")
    private List<Coordinates> collisionMatrix;

    @OneToMany(mappedBy="gameState")
    private List<Enemy> enemies;

    @OneToMany(mappedBy="gameState")
    private List<Tower> towers;

    /**
     * Der Schwierigkeitsgrad des Spieles
     */
    @Enumerated(EnumType.ORDINAL)
    private Difficulty difficulty;

    /**
     * Die aktuelle Rundennummer
     */
    private int roundNumber;

    /**
     * Die Anzahl der Runden
     */
    private int numberOfRounds;

    /**
     * Gibt an, ob das Spiel gerade aktiv ist
     */
    private boolean active;

    /**
     * Konstruktor, der den Spielzustand mit Spielern und einem Schwierigkeitsgrad initialisiert
     */
    public Gamestate (List<Player> players, Difficulty difficulty) {


    }

    /**
     * Default-Konstruktor. Wird von JPA benötigt
     */
    public Gamestate() {
        enemies = new ArrayList<Enemy>();
        towers = new ArrayList<Tower>();
    }

    public void addEnemy (EnemyType enemyType) {

    }

    public void addEnemy (Enemy enemy) {
        enemies.add(enemy);

    }

    public void addEnemy (Enemy enemy, Player player) {
        enemy.setAttackedPlayer(player);
        player.addEnemy(enemy);
        enemies.add(enemy);
    }


    public List<Enemy> getEnemies() {
        return enemies;
    }

    public int getLocalPlayerNumber() {
        return localPlayerNumber;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addTower(Tower tower) {
        towers.add(tower);
    }
}
