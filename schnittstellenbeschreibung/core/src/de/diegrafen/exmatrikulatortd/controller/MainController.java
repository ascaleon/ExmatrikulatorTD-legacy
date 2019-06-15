package de.diegrafen.exmatrikulatortd.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.persistence.HighscoreDao;
import de.diegrafen.exmatrikulatortd.persistence.ProfileDao;
import de.diegrafen.exmatrikulatortd.view.screens.EndScreen;
import de.diegrafen.exmatrikulatortd.view.screens.GameScreen;
import de.diegrafen.exmatrikulatortd.view.screens.MenuScreen;
import de.diegrafen.exmatrikulatortd.view.screens.SplashScreen;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 18:08
 */
public class MainController {

    private HighscoreDao highScoreDao;

    private ProfileDao profileDao;

    private Profile currentProfile;

    private final MenuScreen menuScreen;

    private SplashScreen splashScreen;

    private Game game;

    private AssetManager assetManager;

    private GameServer gameServer;

    private GameClient gameClient;

    public MainController (Game game) {
        this.game = game;
        this.splashScreen = new SplashScreen(this);
        this.menuScreen = new MenuScreen(this);
        this.assetManager = new AssetManager();
        this.profileDao = new ProfileDao();
        this.highScoreDao = new HighscoreDao();
    }

    public void setSplashScreen () {
        game.setScreen(splashScreen);
    }

    public void loadAssets() {
    }

    public boolean areAssetsLoaded() {
        return true;
    }

    public void setMenuScreen() {
        game.setScreen(menuScreen);
    }


    public void setEndScreen(Gamestate gamestate) {
        game.setScreen(new EndScreen(this, gamestate));
    }

    public void createNewProfile (String profileName, String profilePicture) {

    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(Profile currentProfile) {
        this.currentProfile = currentProfile;
    }

    public void createServer() {

    }

    public void createClient() {

    }

    public void startServer() {

    }

    public void connectToServer (String host) {

    }

    public void createNewSinglePlayerGame () {
        game.setScreen(new GameScreen(this, currentProfile));
    }

    public void loadSinglePlayerGame (Gamestate gamestate) {
        game.setScreen(new GameScreen(this, currentProfile, gamestate));
    }

    public void createNewMultiplayerClientGame() {
        new GameScreen(this, currentProfile, gameClient);
    }

    public void loadMultiPlayerClientGame () {
        game.setScreen(new GameScreen(this, currentProfile, new Gamestate()));
    }

    public void createNewMultiplayerServerGame() {
        new GameScreen(this, currentProfile, gameServer);
    }

    public void loadMultiPlayerServerGame (Gamestate gamestate) {
        game.setScreen(new GameScreen(this, currentProfile, gameServer, gamestate));
    }

}
