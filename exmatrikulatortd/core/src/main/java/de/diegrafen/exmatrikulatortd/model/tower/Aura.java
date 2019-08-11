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

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Debuff> debuffs;

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Buff> buffs;

    public Aura() {
    }

    public Aura(List<Debuff> debuffs, List<Buff> buffs) {
        this.debuffs = debuffs;
        this.buffs = buffs;
    }

    /**
     * Kopier-Konstruktor
     * @param aura Die Aura, die kopiert werden soll
     */
    Aura(Aura aura) {
        this.debuffs = new LinkedList<>();
        this.buffs = new LinkedList<>();

        aura.getBuffs().forEach(buff -> buffs.add(new Buff(buff)));
        aura.getDebuffs().forEach(debuff -> debuffs.add(new Debuff(debuff)));
    }

    public List<Debuff> getDebuffs() {
        return debuffs;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }
}


