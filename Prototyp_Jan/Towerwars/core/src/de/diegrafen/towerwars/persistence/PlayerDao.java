package de.diegrafen.towerwars.persistence;

import de.diegrafen.towerwars.models.Profile;

public class PlayerDao extends BaseDao<Profile> {

    public synchronized void create (Profile profile) {
        if (profile == null) {
            //throw new IllegalAccessException();
            return;
        }

        try {
            super.create(profile);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void update (Profile profile) {
        if (profile == null) {
            //throw new IllegalAccessException();
            return;
        }

        try {
            super.update(profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void delete (Profile profile) {
        super.delete(profile);
    }

    public Profile retrieve (Long id) {
        return super.retrieve(id);
    }

    @Override
    Class<Profile> getClazz() {
        return Profile.class;
    }
}


