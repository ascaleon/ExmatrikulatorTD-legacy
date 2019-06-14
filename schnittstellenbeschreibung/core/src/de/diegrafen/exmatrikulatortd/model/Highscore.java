package de.diegrafen.exmatrikulatortd.model;

import com.badlogic.gdx.utils.compression.lzma.Base;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:36
 */
@Entity
@Table(name = "Highscores")
public class Highscore extends BaseModel {

    static final long serialVersionUID = 81716312831239L;

    @ManyToOne
    @JoinColumn(name="profile_id")
    private Profile profile;

    private int roundNumberReached;

    private Date timePlayed;

}
