package de.diegrafen.exmatrikulatortd.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:31
 */
@Entity
@Table(name = "ServerState")
public class ServerState extends BaseModel  {

    static final long serialVersionUID = 9875641254L;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Gamestate> singlePlayerStates;
}
