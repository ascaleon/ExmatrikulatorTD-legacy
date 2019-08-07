package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Profile;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:17
 */
@Entity
@Table(name = "SaveState")
@NamedQueries({
        @NamedQuery(name="SaveState.findAll",
                query="SELECT s FROM SaveState s"),
})
public class SaveState extends BaseModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 4918147183123L;

    private String saveStateName;

    /**
     * Das Datum des Spielstandes
     */
    private java.util.Date saveDate;

    /**
     * Gibt an, ob es sich um einen Multiplayer-Spielstand handelt
     */
    private boolean multiplayer;

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

    private int localPlayerNumber;

    private String mapPath;

    public SaveState() {
    }

    public SaveState(String saveStateName, Date saveDate, boolean multiplayer, Profile profile, Gamestate gamestate, int localPlayerNumber, String mapPath) {
        this.saveStateName = saveStateName;
        this.saveDate = saveDate;
        this.multiplayer = multiplayer;
        this.profile = profile;
        this.gamestate = gamestate;
        this.localPlayerNumber = localPlayerNumber;
        this.mapPath = mapPath;
    }

    public Date getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }

    public boolean isMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }

    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    public int getLocalPlayerNumber() {
        return localPlayerNumber;
    }

    public void setLocalPlayerNumber(int localPlayerNumber) {
        this.localPlayerNumber = localPlayerNumber;
    }

    public String getMapPath() {
        return mapPath;
    }

    public String getSaveStateName() {
        return saveStateName;
    }
}
