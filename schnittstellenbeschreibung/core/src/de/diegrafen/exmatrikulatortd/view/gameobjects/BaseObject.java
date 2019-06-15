package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.diegrafen.exmatrikulatortd.model.BaseModel;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:03
 */
public abstract class BaseObject implements GameObject {

    private BaseModel baseModel;

    private Sprite sprite;

    public BaseObject(final BaseModel baseModel) {

    }

    public abstract void update();

    public abstract void draw(SpriteBatch spriteBatch);

    private void initializeSprite () {

    }

    public abstract void dispose();

}
