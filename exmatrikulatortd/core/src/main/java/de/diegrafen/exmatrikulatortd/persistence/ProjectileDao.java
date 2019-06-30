package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.tower.Projectile;

/**
 * @author janro
 * @version 30.06.2019 19:54
 */
public class ProjectileDao extends BaseDao<Projectile> {

    /**
     * Gibt die Klasse zurück, die mit dem DAO-Objekt assoziiert ist
     *
     * @return
     */
    @Override
    Class<Projectile> getClazz() {
        return Projectile.class;
    }
}
