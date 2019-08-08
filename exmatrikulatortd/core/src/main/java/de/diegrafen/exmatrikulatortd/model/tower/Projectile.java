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

//    @ManyToOne
//    private Tower towerThatShot;

    private int playerNumber;

    private float targetxPosition;

    private float targetyPosition;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Debuff> applyingDebuffs;

    /**
     * Standardkonstruktor. Wird für JPA benötigt.
     */
    public Projectile() {
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
        this.playerNumber = tower.getPlayerNumber();
        this.applyingDebuffs = new LinkedList<>();
        tower.getAttackDebuffs().forEach(debuff -> applyingDebuffs.add(new Debuff(debuff)));

        this.target = tower.getCurrentTarget();
    }

    public Projectile(Projectile projectile) {
        this.name = projectile.name;
        this.assetsName = projectile.assetsName;
        this.attackType = projectile.attackType;
        this.damage = projectile.damage;
        this.splashAmount = projectile.splashAmount;
        this.splashRadius = projectile.splashRadius;
        this.speed = projectile.speed;
        this.xPosition = projectile.xPosition;
        this.yPosition = projectile.yPosition;
        this.targetxPosition = projectile.xPosition;
        this.targetyPosition = projectile.yPosition;
        this.playerNumber = projectile.playerNumber;

        this.applyingDebuffs = new LinkedList<>();

        this.target = null;
        //this.towerThatShot = null;

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



//    public Tower getTowerThatShot() {
//        return towerThatShot;
//    }

    public int getAttackType() {
        return attackType;
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }

//    public void setTowerThatShot(Tower towerThatShot) {
//        this.towerThatShot = towerThatShot;
//    }


    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
