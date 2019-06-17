package de.diegrafen.exmatrikulatortd.model;

import com.badlogic.gdx.utils.compression.lzma.Base;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

}
