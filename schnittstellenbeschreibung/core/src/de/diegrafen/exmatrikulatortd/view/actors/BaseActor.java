package de.diegrafen.exmatrikulatortd.view.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.diegrafen.exmatrikulatortd.model.BaseModel;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 01:42
 */
public abstract class BaseActor extends Actor {

    BaseModel model;

    TextureRegion texture;

    public BaseActor(BaseModel model)
    {
        this.model =  model;
        init();
    }

    /**
     * Initialisiert den Acotr
     */
    public void init () {

    };


    @Override
    public void act (float deltaTime) {
        super.act(deltaTime);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

}
