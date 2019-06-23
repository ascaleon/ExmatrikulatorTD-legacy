package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.view.Observer;
import de.diegrafen.exmatrikulatortd.view.gameobjects.GameObject;

import javax.persistence.MappedSuperclass;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 20.06.2019 16:28
 */
@MappedSuperclass
public abstract class ObservableModel extends BaseModel implements ObservableUnit  {

    private transient Observer observer;

    private transient boolean removed;

    public ObservableModel () {

    }

    @Override
    public void registerObserver(Observer observer) {
        this.observer = observer;
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observer = null;
    }

    @Override
    public void notifyObserver() {
        if (observer != null) {
            observer.update();
        }
//        for (GameObject observer : observers) {
//            observer.update();
//        }
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
        notifyObserver();
    }
}
