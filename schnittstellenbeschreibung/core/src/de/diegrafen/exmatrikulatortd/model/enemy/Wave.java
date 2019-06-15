package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Waves")
public class Wave extends BaseModel {

    static final long serialVersionUID = 41197591759175915L;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Enemy> enemies;

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
