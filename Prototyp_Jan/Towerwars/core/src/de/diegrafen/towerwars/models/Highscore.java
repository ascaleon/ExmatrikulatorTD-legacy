package de.diegrafen.towerwars.models;

import javax.persistence.*;

@Entity
@Table(name = "Highscores")
@NamedQueries({ @NamedQuery(name = "Highscore.findAll", query = "SELECT h FROM Highscore h")})
public class Highscore extends BaseEntity {

    static final long serialVersionUID = -953041539;

    private int score;

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;

    public Highscore () {

    }


}
