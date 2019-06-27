package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.Highscore;
import org.hibernate.Session;

import javax.persistence.NamedQuery;
import java.util.List;

/**
 *
 * DAO-Klasse für Highscore-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 14:31
 */
public class HighscoreDao extends BaseDao<Highscore> {


    /**
     * Gibt die Highscore-Klasse zurück
     * @return Der Klassen-Typ
     */
    @Override
    Class<Highscore> getClazz() {
        return Highscore.class;
    }


    public List<Highscore> findHighestScores(int limit) {
        if (limit < 0) {
            return null;
        }

        Session session = openCurrentSessionwithTransaction();
        final List<Highscore> highscores = session.createNamedQuery("Highscore.findHighestScores", getClazz()).setMaxResults(limit).getResultList();
        closeCurrentSessionwithTransaction();
        return highscores;
    }
}
