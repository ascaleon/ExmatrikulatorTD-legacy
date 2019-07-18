package de.diegrafen.exmatrikulatortd;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public interface GameInterface extends ApplicationListener {

    AssetManager getAssetManager();
}
