package de.diegrafen.exmatrikulatortd.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:34
 */
@Entity
@Table(name = "Profiles")
public class Profile extends BaseModel {

    static final long serialVersionUID = 1093158194849553L;

    private String profileName;

    private String profilePicturePath;

    @OneToMany(mappedBy="profile")
    private List<Highscore> highscores;

    @OneToMany(mappedBy="profile")
    private List<SaveState> saveStates;

}
