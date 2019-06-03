package de.diegrafen.towerwars.persistence;

import de.diegrafen.towerwars.models.Player;

public class PlayerDao extends BaseDao<Player> {

    public synchronized void create (Player player) {
        if (player == null) {
            //throw new IllegalAccessException();
            return;
        }

        try {
            super.create(player);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void update (Player player) {
        if (player == null) {
            //throw new IllegalAccessException();
            return;
        }

        try {
            super.update(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void delete (Player player) {
        super.delete(player);
    }

    public Player retrieve (Long id) {
        return super.retrieve(id);
    }

    @Override
    Class<Player> getClazz() {
        return Player.class;
    }
}


