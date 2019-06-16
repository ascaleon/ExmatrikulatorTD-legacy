package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import javax.persistence.*;
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
            orphanRemoval = true
    )
    private List<Enemy> enemies;

    /**
     * Der mit der Welle assoziierte Spieler
     */
    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void addEnemy (Enemy enemy) {

    }

    public void removeEnemy (Enemy enemy) {

    }

}
