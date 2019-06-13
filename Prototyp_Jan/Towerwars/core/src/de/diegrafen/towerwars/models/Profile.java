package de.diegrafen.towerwars.models;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Profile")
@NamedQueries({ @NamedQuery(name = "Profile.findAll", query = "SELECT p FROM Profile p")})
public class Profile extends BaseEntity {

    static final long serialVersionUID = 223795568;

    private String name;

    public Profile() {

    }

    public Profile(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy="profile")
    private List<Highscore> highscores;

    @Override
    public String toString() {
        return String.format("Profile {id: %d, name: %s}", getId(), name);
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
