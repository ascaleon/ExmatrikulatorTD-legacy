package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * Eine Angriffswelle. Beinhaltet eine bestimmte Anzahl von Gegnern und ist immer genau einem Spieler zugewiesen,
 * den die Einheiten versuchen anzugreifen
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 */
@Entity
@Table(name = "Waves")
public class Wave extends BaseModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 41197591759175915L;

    /**
     * Die Nummer der Welle
     */
    private int waveNumber;

    /**
     * Die Gegner, die die Welle beinhaltet
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "wave"
    )
    private List<Enemy> enemies;

    /**
     * Der mit der Welle assoziierte Spieler
     */
    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;

    public Wave () {
        this.enemies = new ArrayList<>();
    }

    public Wave(Wave wave) {
        this.enemies = new LinkedList<>();
        for (Enemy enemy : wave.getEnemies()) {
            this.enemies.add(new Enemy(enemy));
        }
        this.player = wave.getPlayer();
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void addEnemy (Enemy enemy) {
        enemies.add(enemy);
    }

    public void removeEnemy (Enemy enemy) {

    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public void setWaveNumber(int waveNumber) {
        this.waveNumber = waveNumber;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
