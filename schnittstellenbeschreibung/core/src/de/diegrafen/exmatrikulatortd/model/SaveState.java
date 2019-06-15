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

    private java.util.Date saveDate;

    private boolean isMultiplayer;

    @ManyToOne
    @JoinColumn(name="profile_id")
    private Profile profile;

    @OneToOne
    private Gamestate gamestate;

}
