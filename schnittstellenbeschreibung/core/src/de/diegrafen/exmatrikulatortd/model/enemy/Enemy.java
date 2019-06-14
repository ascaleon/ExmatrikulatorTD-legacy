package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "Enemies")
public class Enemy extends BaseModel {

    static final long serialVersionUID = 21268121294890L;

    private float baseSpeed;

    private float currentSpeed;

    private float maxHitPoints;

    private float currentHitPoints;

    private float xPosition;

    private float yPosition;

    private int livesTakenFromPlayer;

    private int resourcesForKill;

    @Enumerated(EnumType.ORDINAL)
    private ArmorType armorType;

    private String textureName;

}
