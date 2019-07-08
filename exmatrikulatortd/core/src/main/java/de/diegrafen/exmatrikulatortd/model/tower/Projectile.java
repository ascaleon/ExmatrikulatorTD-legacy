package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.ObservableModel;
import de.diegrafen.exmatrikulatortd.model.enemy.Debuff;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

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

    private float splashPercentage;

    private float splashRadius;

    private float speed;

    private float xPosition;

    private float yPosition;

    @OneToOne
    private Enemy target;

    @OneToOne
    private Tower towerThatShot;

    private float targetxPosition;

    private float targetyPosition;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Debuff> applyingDebuffs;

    public Projectile() {
    }

    public Projectile(String name, String assetsName, int attackType, float damage, float splashPercentage, float splashRadius, float speed) {
        this.name = name;
        this.assetsName = assetsName;
        this.attackType = attackType;
        this.damage = damage;
        this.splashPercentage = splashPercentage;
        this.splashRadius = splashRadius;
        this.speed = speed;
        this.applyingDebuffs = new LinkedList<>();
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

    public float getSplashPercentage() {
        return splashPercentage;
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

    public void setTarget(Enemy target) {
        this.target = target;
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

    public void addDebuff(Debuff debuff) {
        this.applyingDebuffs.add(debuff);
    }

    public Tower getTowerThatShot() {
        return towerThatShot;
    }

    public void setTowerThatShot(Tower towerThatShot) {
        this.towerThatShot = towerThatShot;
    }

    public int getAttackType() {
        return attackType;
    }
}
