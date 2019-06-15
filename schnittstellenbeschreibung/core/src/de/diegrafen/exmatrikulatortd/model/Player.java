package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Players")
public class Player extends BaseModel {

    static final long serialVersionUID = 4918147183123L;

    @ManyToOne
    @JoinColumn(name="gamestate_id")
    private Gamestate gameState;

    private int playerNumber;

    private String playerName;

    private int score;

    private int resources;

    private int maxLives;

    private int currentLives;

    @OneToMany(mappedBy="owner")
    private List<Tower> towers;

    public Player () {

    }

}
