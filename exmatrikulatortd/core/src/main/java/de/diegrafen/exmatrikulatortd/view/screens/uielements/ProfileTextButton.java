package de.diegrafen.exmatrikulatortd.view.screens.uielements;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Erweitert den Textbutton um ein ID-Feld, um sie mit einem Profil-Eintrag in der Datenbank verknüpfen zu können
 */
public class ProfileTextButton extends TextButton {

    /**
     * Eine ID, die auf einen Eintrag in der Profil-Datenbank verweist
     */
    private long ID;

    /**
     *
     * Erzeugt einen neuen ProfileTextButton
     *
     * @param text Der Buttontext
     * @param skin Der Buttonskin
     * @param ID Die Profil-ID
     */
    public ProfileTextButton(String text, Skin skin, long ID) {
        super(text, skin);
        this.ID = ID;
    }

    /**
     * Gibt die Profil-ID zurück, die der Button gespeichert hat
     * @return Die Profil-ID
     */
    public long getID() {
        return ID;
    }
}
