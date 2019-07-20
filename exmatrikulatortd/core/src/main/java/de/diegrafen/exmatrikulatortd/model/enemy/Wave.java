package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    private int enemySpawnIndex = 0;

    /**
     * Die Gegner, die die Welle beinhaltet
     */
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Enemy> enemies;

    public Wave () {
        this.enemies = new ArrayList<>();
    }

    public Wave(Wave wave) {
        this.enemySpawnIndex = wave.getEnemySpawnIndex();
        this.enemies = new LinkedList<>();
        for (Enemy enemy : wave.getEnemies()) {
            this.enemies.add(new Enemy(enemy));
        }
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

    public int getEnemySpawnIndex() {
        return enemySpawnIndex;
    }

    public void setEnemySpawnIndex(int enemySpawnIndex) {
        this.enemySpawnIndex = enemySpawnIndex;
    }
}
