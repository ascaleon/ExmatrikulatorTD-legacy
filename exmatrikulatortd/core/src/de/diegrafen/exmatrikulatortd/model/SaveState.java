package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Profile;

import javax.persistence.*;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:17
 */
@Entity
@Table(name = "SaveState")
public class SaveState extends BaseModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 4918147183123L;

    /**
     * Das Datum des Spielstandes
     */
    private java.util.Date saveDate;

    /**
     * Gibt an, ob es sich um einen Multiplayer-Spielstand handelt
     */
    private boolean isMultiplayer;

    /**
     * Das mit dem Spielstand assoziierte Profil
     */
    @ManyToOne
    @JoinColumn(name="profile_id")
    private Profile profile;

    /**
     * Der abgespeicherte Zustand des Spiels
     */
    @OneToOne
    private Gamestate gamestate;

}
