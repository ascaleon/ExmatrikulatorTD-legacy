package de.diegrafen.exmatrikulatortd.view.screens.uielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TowerButton extends ImageButton {

    private int towerNumber;

    private String toolTipText;

    private TowerButton(Drawable imageUp, Drawable imageDown) {
        super(imageUp, imageDown, imageDown);
    }

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
