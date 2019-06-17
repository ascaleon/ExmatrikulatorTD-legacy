package de.diegrafen.exmatrikulatortd.model;

import javax.persistence.*;
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

}
