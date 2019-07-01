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

    private float damage;

    private float splashPercentage;

    private float splashRadius;

    private float speed;

    private float xPosition;

    private float yPosition;

    @OneToOne
    private Enemy target;

    private float targetxPosition;

    private float targetyPosition;

    @OneToMany(orphanRemoval = true, cascade= CascadeType.ALL)
    private List<Debuff> applyingDebuffs;

    public Projectile() {
    }

    public Projectile(String name, String assetsName, float damage, float splashPercentage, float splashRadius, float speed) {
        this.name = name;
        this.assetsName = assetsName;
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

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getSplashPercentage() {
        return splashPercentage;
    }

    public void setSplashPercentage(float splashPercentage) {
        this.splashPercentage = splashPercentage;
    }

    public float getSplashRadius() {
        return splashRadius;
    }

    public void setSplashRadius(float splashRadius) {
        this.splashRadius = splashRadius;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
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

    public void setApplyingDebuffs(List<Debuff> applyingDebuffs) {
        this.applyingDebuffs = applyingDebuffs;
    }

    public void addDebuff(Debuff debuff) {
        this.applyingDebuffs.add(debuff);
    }

    public void removeDebuff(Debuff debuff) {
        this.applyingDebuffs.remove(debuff);
    }
}
