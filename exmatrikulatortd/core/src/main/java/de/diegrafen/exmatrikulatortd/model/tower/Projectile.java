package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.ObservableModel;
import de.diegrafen.exmatrikulatortd.model.enemy.Debuff;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "Projectiles")
public class Projectile extends ObservableModel {

    private String name;

    private String assetsName;

    private int attackType;

    private float damage;

    private float splashAmount;

    private float splashRadius;

    private float speed;

    private float xPosition;

    private float yPosition;

    @ManyToOne
    private Enemy target;

    @ManyToOne
    private Tower towerThatShot;

    private float targetxPosition;

    private float targetyPosition;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Debuff> applyingDebuffs;

    public Projectile() {
    }

    // TODO: Könnte tatsächlich obsolet geworden sein
    public Projectile(String name, String assetsName, int attackType, float damage, float splashAmount, float splashRadius, float speed) {
        this.name = name;
        this.assetsName = assetsName;
        this.attackType = attackType;
        this.damage = damage;
        this.splashAmount = splashAmount;
        this.splashRadius = splashRadius;
        this.speed = speed;
        this.applyingDebuffs = new LinkedList<>();
    }

    public Projectile(Tower tower) {
        this.name = tower.getProjectileName();
        this.assetsName = tower.getProjectileAssetsName();
        this.attackType = tower.getAttackType();
        this.damage = tower.getCurrentAttackDamage();
        this.splashAmount = tower.getSplashAmount();
        this.splashRadius = tower.getSplashRadius();
        this.speed = tower.getProjectileSpeed();
        this.xPosition = tower.getxPosition();
        this.yPosition = tower.getyPosition();
        this.towerThatShot = tower;
        this.applyingDebuffs = new LinkedList<>();
        tower.getAttackDebuffs().forEach(debuff -> applyingDebuffs.add(new Debuff(debuff)));

        this.target = tower.getCurrentTarget();
    }

    public Projectile(Projectile projectile) {
        this.name = projectile.getName();
        this.assetsName = projectile.getAssetsName();
        this.attackType = projectile.getAttackType();
        this.damage = projectile.getDamage();
        this.splashAmount = projectile.getsplashAmount();
        this.splashRadius = projectile.getSplashRadius();
        this.speed = projectile.getSpeed();
        this.xPosition = projectile.getxPosition();
        this.yPosition = projectile.getyPosition();
        this.targetxPosition = projectile.getTargetxPosition();
        this.targetyPosition = projectile.getTargetyPosition();

        this.applyingDebuffs = new LinkedList<>();

        this.target = null;
        this.towerThatShot = null;

        projectile.getApplyingDebuffs().forEach(debuff -> applyingDebuffs.add(new Debuff(debuff)));
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAssetsName() {
        return assetsName;
    }

    public float getDamage() {
        return damage;
    }

    public float getsplashAmount() {
        return splashAmount;
    }

    public float getSplashRadius() {
        return splashRadius;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public float getxPosition() {
        return xPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    @Override
    public float getyPosition() {
        return yPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public Enemy getTarget() {
        return target;
    }

    public float getTargetxPosition() {
        return targetxPosition;
    }

    public void setTargetxPosition(float targetxPosition) {
        this.targetxPosition = targetxPosition;
    }

    public float getTargetyPosition() {
        return targetyPosition;
    }

    public void setTargetyPosition(float targetyPosition) {
        this.targetyPosition = targetyPosition;
    }

    public List<Debuff> getApplyingDebuffs() {
        return applyingDebuffs;
    }

    public Tower getTowerThatShot() {
        return towerThatShot;
    }

    public int getAttackType() {
        return attackType;
    }

    //FIXME: Eigenes Interface ohne obsolete Methoden implementieren
    @Override
    public float getCurrentMaxHitPoints() {
        return 0;
    }

    @Override
    public float getCurrentHitPoints() {
        return 0;
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }

    public void setTowerThatShot(Tower towerThatShot) {
        this.towerThatShot = towerThatShot;
    }
}
