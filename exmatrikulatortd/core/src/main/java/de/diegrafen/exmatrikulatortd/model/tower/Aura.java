package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.enemy.Debuff;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.LinkedList;
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
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Debuff> debuffs;

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Buff> buffs;

    public Aura() {
    }

    public Aura(float range, List<Debuff> debuffs, List<Buff> buffs) {
        this.range = range;
        this.debuffs = debuffs;
        this.buffs = buffs;
    }

    public Aura(Tower tower) {
        this.range = tower.getAuraRange();
        this.debuffs = new LinkedList<>();
        this.buffs = new LinkedList<>();

        //tower.getAuraDebuffs().forEach(debuff -> debuffs.add(new Debuff(debuff)));
        //tower.getAuraBuffs().forEach(buff -> buffs.add(new Buff(buff)));
    }

    /**
     * Kopier-Konstruktor
     * @param aura Die Aura, die kopiert werden soll
     */
    Aura(Aura aura) {
        this.range = aura.getRange();
        this.debuffs = new LinkedList<>();
        this.buffs = new LinkedList<>();

        aura.getBuffs().forEach(buff -> buffs.add(new Buff(buff)));
        aura.getDebuffs().forEach(debuff -> debuffs.add(new Debuff(debuff)));
    }

    public float getRange() {
        return range;
    }

    public List<Debuff> getDebuffs() {
        return debuffs;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }
}


