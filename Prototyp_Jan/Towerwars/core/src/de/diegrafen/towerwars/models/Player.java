package de.diegrafen.towerwars.models;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Player")
@NamedQueries({ @NamedQuery(name = "Player.findAll", query = "SELECT p FROM Player p")})
public class Player extends BaseEntity {

    static final long serialVersionUID = 223795568;

    private String name;

    public Player() {

    }

    public Player(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy="player")
    private List<Highscore> highscores;

    @Override
    public String toString() {
        return String.format("Player {id: %d, name: %s}", getId(), name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Highscore> getHighscores() {
        return highscores;
    }

    public void setHighscores(List<Highscore> highscores) {
        this.highscores = highscores;
    }
}
