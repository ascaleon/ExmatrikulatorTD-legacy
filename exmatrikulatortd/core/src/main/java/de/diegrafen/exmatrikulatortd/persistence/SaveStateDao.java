package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.SaveState;
import org.hibernate.Session;

import java.util.List;

/**
 *
 * DAO-Klasse für SaveState-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 14:24
 */
public class SaveStateDao extends BaseDao<SaveState> {

    /**
     * Gibt die SaveState-Klasse zurück
     * @return Der Klassen-Typ
     */
    @Override
    Class<SaveState> getClazz() {
        return SaveState.class;
    }

    public List<SaveState> findAllSaveStates() {

        Session session = openCurrentSessionwithTransaction();
        final List<SaveState> saveStates = session.createNamedQuery("SaveState.findAll", getClazz()).getResultList();
        closeCurrentSessionwithTransaction();
        return saveStates;
    }
}