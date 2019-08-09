package de.diegrafen.exmatrikulatortd.view.screens.uielements;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class ProfileTextButton extends TextButton {

    private long ID;

    public ProfileTextButton(String text, Skin skin, long ID) {
        super(text, skin);
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }
}
