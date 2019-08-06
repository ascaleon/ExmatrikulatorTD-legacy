package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import org.hibernate.Session;

import java.util.List;

/**
 *
 * DAO-Klasse für Tower-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:50
 */
public class TowerDao extends BaseDao<Tower> {

    /**
     * Gibt die SaveState-Klasse zurück
     * @return Der Tower-Typ
     */
    @Override
    Class<Tower> getClazz() {
        return Tower.class;
    }

    public boolean hasTableTemplateTowers() {
        Session session = openCurrentSessionwithTransaction();
        final List<Tower> towers = session.createNamedQuery("Tower.findTemplateTowers", getClazz()).getResultList();
        closeCurrentSessionwithTransaction();
        return !towers.isEmpty();
    }

    public List<Tower> retrieveTemplateTowers() {
        Session session = openCurrentSessionwithTransaction();
        final List<Tower> towers = session.createNamedQuery("Tower.findTemplateTowers", getClazz()).getResultList();
        closeCurrentSessionwithTransaction();
        return towers;
    }

}
