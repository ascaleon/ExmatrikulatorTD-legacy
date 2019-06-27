package de.diegrafen.exmatrikulatortd.model;

import com.badlogic.gdx.utils.compression.lzma.Base;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * Diese Klasse dient der Speicherung von Highscores. Ein Highscore-Objekt ist einem Profil zugeordnet und verfügt
 * über Informationen zur erreichten Punktzahl, der erreichten Runde sowie dem Zeitpunkt, an dem gespielt wurde
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:36
 */
@Entity
@Table(name = "Highscores")
@NamedQueries({
        @NamedQuery(name="Highscore.findHighestScores",
                query="SELECT h FROM Highscore h ORDER BY h.score DESC"),
})
public class Highscore extends BaseModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 81716312831239L;

    /**
     * Das mit diesem Highscore assoziierte Profil
     */
    @ManyToOne
    @JoinColumn(name="profile_id")
    private Profile profile;

    /**
     * Der erreichte Punktzahl
     */
    private int score;

    /**
     * Die erreichte Runde
     */
    private int roundNumberReached;

    /**
     * Der Zeitpunkt, an dem der Highscore erzielt wurde
     */
    private Date datePlayed;

    public Highscore() {

    }

    public Highscore(Profile profile, int score, int roundNumberReached, Date datePlayed) {
        this.profile = profile;
        this.score = score;
        this.roundNumberReached = roundNumberReached;
        this.datePlayed = datePlayed;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRoundNumberReached() {
        return roundNumberReached;
    }

    public void setRoundNumberReached(int roundNumberReached) {
        this.roundNumberReached = roundNumberReached;
    }

    public Date getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(Date datePlayed) {
        this.datePlayed = datePlayed;
    }
}
