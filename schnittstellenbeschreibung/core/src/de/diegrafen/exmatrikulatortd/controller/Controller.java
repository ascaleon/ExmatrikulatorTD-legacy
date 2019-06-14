package de.diegrafen.exmatrikulatortd.controller;

import com.badlogic.gdx.Screen;
import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.view.screens.BaseScreen;

public abstract class Controller {

    private boolean debugOn;

    protected BaseScreen screen;

    protected BaseModel model;

    public Controller (BaseScreen screen, BaseModel model) {
        this.screen = screen;
        this.model = model;
    }


    public boolean isDebugOn() {
        return debugOn;
    }

    public void setDebugOn(boolean debugOn) {
        this.debugOn = debugOn;
    }
}
