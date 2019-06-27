package de.diegrafen.exmatrikulatortd.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Profilklasse. Dient der Verwaltung von Speicherständen und Highscores und ermöglicht Spielerinnen die Personalisierung
 * der eigenen Spielerfahrung durch die Möglichkeit, einen Namen anzugeben und ein Profilbild auszuwählen
 *
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:34
 */
@Entity
@Table(name = "Profiles")
public class Profile extends BaseModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 1093158194849553L;

    /**
     * Der Name der Person, der das Profil gehört
     */
    private String profileName;

    /**
     * Das Pfad zum Bild, das mit dem Profil verknüpft ist
     */
    private String profilePicturePath;

    /**
     * Die mit dem Profil assozierten Highscores
     */
    @OneToMany(mappedBy="profile")
    private List<Highscore> highscores;

    /**
     * Die mit dem Profil assozierten Speicherstände
     */
    @OneToMany(mappedBy="profile")
    private List<SaveState> saveStates;

    /**
     * Der Standardschwierigkeitsgrad, der für das Profil gewählt wurde
     */
    @Enumerated(EnumType.ORDINAL)
    private Difficulty preferredDifficulty;

    public Profile() {
        this.highscores = new ArrayList<>();
        this.saveStates = new ArrayList<>();
    }

    public Profile(String profileName, Difficulty preferredDifficulty) {
        this();
        this.profileName = profileName;
        this.preferredDifficulty = preferredDifficulty;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public List<Highscore> getHighscores() {
        return highscores;
    }

    public void setHighscores(List<Highscore> highscores) {
        this.highscores = highscores;
    }

    public void addHighscore(Highscore highscore) {
        highscores.add(highscore);
    }

    public List<SaveState> getSaveStates() {
        return saveStates;
    }

    public void setSaveStates(List<SaveState> saveStates) {
        this.saveStates = saveStates;
    }

    public Difficulty getPreferredDifficulty() {
        return preferredDifficulty;
    }

    public void setPreferredDifficulty(Difficulty preferredDifficulty) {
        this.preferredDifficulty = preferredDifficulty;
    }
}
