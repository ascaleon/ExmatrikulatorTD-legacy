package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.enemy.Debuff;

import javax.persistence.*;
import java.util.List;

/**
 *
 * Die verschiedenen Aura-Typen, die ein Turm haben kann. Eine Aura kann entweder Gegner verlangsamen (@code{SLOW_AURA}),
 * die Angriffsgeschindigkeit befreundeter Türme erhöhen (@code{HASTE_AURA}) oder die Rüstung von Gegnern reduzieren
 * (@code{CORRUPTION_AURA}).
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 22:01
 */
@Entity
@Table(name = "Auras")
public class Aura extends BaseModel {

    private float range;

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    private List<Debuff> debuffs;

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    private List<Buff> buffs;

    public Aura() {
    }

    public Aura(float range, List<Debuff> debuffs, List<Buff> buffs) {
        this.range = range;
        this.debuffs = debuffs;
        this.buffs = buffs;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public List<Debuff> getDebuffs() {
        return debuffs;
    }

    public void setDebuffs(List<Debuff> debuffs) {
        this.debuffs = debuffs;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public void setBuffs(List<Buff> buffs) {
        this.buffs = buffs;
    }
}


