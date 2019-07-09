package de.diegrafen.exmatrikulatortd.model;

public interface ObservableUnit extends Observable {

    String getName();

    String getAssetsName();

    float getxPosition();

    float getyPosition();

    float getTargetxPosition();

    float getTargetyPosition();

    boolean isRemoved();
}
