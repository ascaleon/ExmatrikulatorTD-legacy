package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:13
 */
public interface GameObject {

    void update(float deltaTime);

    void draw(SpriteBatch spriteBatch);

    void dispose();
}
