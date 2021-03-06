package de.diegrafen.exmatrikulatortd.util;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.persistence.TowerDao;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import java.util.EnumSet;

import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.createNewTower;
import static de.diegrafen.exmatrikulatortd.util.Constants.NUMBER_OF_TOWERS;

/**
 * Utility-Klsse für die Nutzung von Hibernate. Dient insbesondere der Bereitstellung einer Session-Factory für
 * die Erzeugung von Sessions zur Interaktion mit der Datenbank.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:23
 */
public class HibernateUtils {

    /**
     * Privater Konstruktor
     */
    private HibernateUtils() {
    }

    /**
     * Die Session-Factory
     */
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Gibt die Session-Factory zurück
     *
     * @return Die Session-Factory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Schließt die aktuelle Session-Factory
     */
    public static void shutdown() {
        getSessionFactory().close();
    }

    /**
     * Erzeugt mithilfe der TowerFactory die baubaren Türme, die während des Spiels aus der Datenbank abgerufen werden
     * können
     */
    public static void createTemplateTowers() {
        TowerDao towerDao = new TowerDao();
        if (!towerDao.hasTableTemplateTowers()) {
            for (int i = 0; i < NUMBER_OF_TOWERS; i++) {
                Tower buildableTower = createNewTower(i);
                towerDao.create(buildableTower);
            }
        }
    }
}
