package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.ObservableUnit;

/**
 * @author janro
 * @version 04.08.2019 02:28
 */
public interface ObservableTower extends ObservableUnit {

    int getTowerType();

    String getPortraitPath();

    String getSelectedPortraitPath();

    String getDescriptionText();

    boolean isAttacking();

    float getAttackSpeed();
}
