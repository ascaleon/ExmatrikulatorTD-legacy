package de.diegrafen.exmatrikulatortd.view.screens.uielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TowerButton extends ImageButton {

    /**
     * Die Turm-Nummer. Relevant unter anderem für Hotkeys
     */
    private int towerNumber;

    /**
     * Der anzuzeigende Tooltip-Text
     */
    private String toolTipText;

    /**
     * Konstruiert einen neuen Towerbutton.
     *
     * @param imageUp Bild, das angezeigt wird, wenn der Button nicht gedrückt ist
     * @param imageDown Bild, das angezeigt wird, wenn der Button gedrückt ist
     */
    private TowerButton(Drawable imageUp, Drawable imageDown) {
        super(imageUp, imageDown, imageDown);
    }

    /**
     * Erzeugt einen neuen TowerButton mit Verweis auf eine Turmnummer
     *
     * @param towerNumber
     * @param imagePath
     * @param imageSelectedPath
     * @param toolTipText
     */
    public TowerButton(int towerNumber, String imagePath, String imageSelectedPath, String toolTipText) {
        this(new TextureRegionDrawable(new Texture(Gdx.files.internal(imagePath))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal(imageSelectedPath))));
        this.towerNumber = towerNumber;
        this.toolTipText = toolTipText;
    }

    public int getTowerNumber() {
        return towerNumber;
    }

    public String getToolTipText() {
        return toolTipText;
    }
}
