package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.List;

public class Wave extends BaseModel {

    static final long serialVersionUID = 918376839414L;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Enemy> enemies;
}
