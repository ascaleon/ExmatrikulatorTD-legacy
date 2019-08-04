package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.ObservableUnit;

/**
 * @author janro
 * @version 04.08.2019 03:46
 */
public interface ObservableEnemy extends ObservableUnit {

    float getCurrentMaxHitPoints();

    float getCurrentHitPoints();
}
