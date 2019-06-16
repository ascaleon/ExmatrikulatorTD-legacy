package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.compression.lzma.Base;
import de.diegrafen.exmatrikulatortd.model.BaseModel;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:03
 */
public abstract class BaseObject implements GameObject {

    private TextureAtlas textureAtlas;

    private TextureRegion currentSprite;

    private float xPosition;

    private float yPosition;

    private String name;

    public BaseObject(String name, String assetsName, float xPosition, float yPosition) {

    }

    public void update(float deltaTime) {

    }

    public void draw(SpriteBatch spriteBatch) {

    }

    private void initializeSprite () {

    }

    public void dispose() {

    };

}
