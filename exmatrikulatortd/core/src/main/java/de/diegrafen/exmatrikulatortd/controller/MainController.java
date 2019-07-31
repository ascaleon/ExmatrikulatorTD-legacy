package de.diegrafen.exmatrikulatortd.controller;

import com.badlogic.gdx.Screen;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;
import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.ClientGameLogicController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.GameLogicController;
import de.diegrafen.exmatrikulatortd.model.*;
import de.diegrafen.exmatrikulatortd.persistence.HighscoreDao;
import de.diegrafen.exmatrikulatortd.persistence.ProfileDao;
import de.diegrafen.exmatrikulatortd.persistence.SaveStateDao;
import de.diegrafen.exmatrikulatortd.view.screens.*;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.net.InetAddress;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Haupt-Controller. Dient der Verwaltung der Bildschirme und der Interaktion mit der Kommunikations- und Persistenz-
 * Komponente
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 18:08
 */
public class MainController {

    /**
     * DAO-Objekt für CRUD-Operationen mit Highscore-Objekten
     */
    private HighscoreDao highScoreDao;

    /**
     * DAO-Objekt für CRUD-Operationen mit Profil-Objekte
     */
    private ProfileDao profileDao;

    /**
     * DAO-Objekt für CRUD-Operationen mit Savestate-Objekten
     */
    private SaveStateDao saveStateDao;

    /**
     * Das aktuelle Profil
     */
    private Profile currentProfile;

    /**
     * Menü-Bildschirm
     */
    private final MenuScreen menuScreen;

    /**
     * Splashscreen
     */
    private SplashScreen splashScreen;

    /**
     * Das Spiel-Objekt, das unter anderem das Rendern übernimmt
     */
    private ExmatrikulatorTD game;

    /**
     * Gameserver für den Multiplayer-ModusGame
     */
    private GameServer gameServer;

    /**
     * Gameclient für den Multiplayer-Modus
     */
    private GameClient gameClient;

    private Screen introScreen;

    private boolean databaseLoaded;

    /**
     * Erzeugt einen neuen Maincontroller, das ein Game-Objekt verwaltet
     *
     * @param game Das mit dem Game-Controller zu assoziierende Game-Objekt
     */
    public MainController(ExmatrikulatorTD game) {
        this.game = game;
        System.out.println(game.getAssetManager());
        this.splashScreen = new SplashScreen(this, game.getAssetManager());
        this.menuScreen = new MenuScreen(this, game.getAssetManager());
        this.introScreen = new IntroScreen(this, game.getAssetManager());
        this.profileDao = new ProfileDao();
        this.highScoreDao = new HighscoreDao();
    }

    /**
     * Zeigt den Splash-Screen an
     */
    public void showSplashScreen() {
        game.setScreen(splashScreen);
    }

    /**
     * Gibt an, ob die Assets vom game.getAssetManager() bereits geladen sind
     *
     * @return True, wenn die Assets geladen sind, ansonsten false
     */
    public boolean areAssetsLoaded() {
        return true;
    }

    /**
     * Zeigt das Hauptmenü an
     */
    public void showMenuScreen() {
        game.setScreen(menuScreen);
    }


    /**
     * Zeigt den Endbildschirm einer Spielpartie an
     *
     * @param gamestate Der Spielzustand, dessen Informationen angezeigt werden sollen
     */
    public void setEndScreen(Gamestate gamestate) {
        game.setScreen(new EndScreen(this, game.getAssetManager(), gamestate));
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(Profile currentProfile) {
        this.currentProfile = currentProfile;
    }

    /**
     * Erzeugt ein neues Profil
     *
     * @param profileName    Der Name des Profils
     * @param profilePicture Das Bild des Profils
     */
    public void createNewProfile(String profileName, Difficulty preferredDifficulty, String profilePicture) {
        Profile profile = new Profile(profileName, preferredDifficulty, profilePicture);
        profileDao.create(profile);
    }

    /**
     * Erzeugt einen neuen GameServer
     */
    public void createServer() {
        this.gameServer = new GameServer();
        this.gameServer.setMainController(this);
    }

    /**
     * Erzeugt einen neuen GameClient
     */
    public void createClient() {
        if (gameClient == null) {
            this.gameClient = new GameClient();
            this.gameClient.setMainController(this);
        }
    }

    public List<String> getLocalGameServers() {

        // Code, um Server-Funktionalität zu testen.
        List<InetAddress> servers = gameClient.discoverLocalServers();


        // TODO: Empfangene Informationen müssen geparst werden
        //for (InetAddress inetAddress : servers) {
        //System.out.println(inetAddress.getHostAddress());
        //serverList.add(inetAddress.getHostAddress());
        //}

        List<String> serverList = gameClient.getReceivedSessionInfo();

        for (String string : serverList) {
            System.out.println(string);
        }

        //if (!servers.isEmpty()) {
            //gameClient.connect(servers.get(0).getHostName());
        //} else {
        if (servers.isEmpty()) {
            System.out.println("Keine Server gefunden!");
        }

        return serverList;
    }

    public void shutdownClient() {
        this.gameClient.shutdown();
        //this.gameClient = null;
    }

    /**
     * Startet den GameServer
     */
    public void startServer() {
        gameServer.startServer(2);
    }

    /**
     * Verbindet den GameClient mit einem GameServer
     *
     * @param host Die IP-Adresse des Servers
     */
    public void connectToServer(String host) {
        gameClient.connect(host);
    }

    /**
     * Erstellt ein neues Einzelspieler-Spiel
     */
    public void createNewSinglePlayerGame(int gamemode, String mapPath) {
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        new GameLogicController(this, currentProfile, 1, 0, gamemode, gameScreen, mapPath);
        showScreen(gameScreen);
    }

    /**
     * Lädt ein Einzelspieler-Spiel
     *
     * @param saveState Der Spielzustand, der geladen werden soll
     */
    public void loadSinglePlayerGame(SaveState saveState) {
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        new GameLogicController(this, saveState, gameScreen);
        showScreen(gameScreen);
    }

    /**
     * Erzeugt ein neues Multiplayer-Spiel als Client
     */
    public void createNewMultiplayerClientGame(int numberOfPlayers, int allocatedPlayerNumber, int gamemode, String mapPath) {
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        new ClientGameLogicController(this, currentProfile, numberOfPlayers, allocatedPlayerNumber, gamemode, gameScreen, mapPath, gameClient);
        showScreen(gameScreen);
    }

    /**
     * Lädt ein Multiplayerspiel als Client
     */
    public void loadMultiPlayerClientGame(SaveState saveState, int allocatedPlayerNumber) {
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        new ClientGameLogicController(this, saveState, allocatedPlayerNumber, gameScreen, gameClient);
        showScreen(gameScreen);
    }

    /**
     * Erzeugt ein Multiplayer-Spiel als Server
     */
    public void createNewMultiplayerServerGame(int numberOfPlayers, int allocatedPlayerNumber, int gamemode, String mapPath) {
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        new GameLogicController(this, currentProfile, numberOfPlayers, allocatedPlayerNumber, gamemode, gameScreen, mapPath, gameServer);
        showScreen(gameScreen);
    }

    /**
     * Lädt ein Multiplayerspiel als Server
     *
     * @param saveState Der Spielzustand, der geladen werden soll
     */
    public void loadMultiPlayerServerGame(SaveState saveState) {
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        new GameLogicController(this, saveState, gameScreen, gameServer);
        showScreen(gameScreen);
    }

    public List<Profile> retrieveProfiles(){
        /*try{
            return profileDao.openCurrentSession().createQuery("from Profiles").list();
        } catch (final Exception e){
            return new LinkedList<>();
        }*/
        final Session session=profileDao.openCurrentSession();
        CriteriaBuilder criteriaBuilder=session.getCriteriaBuilder();
        CriteriaQuery<Profile> criteriaQuery=criteriaBuilder.createQuery(Profile.class);
        criteriaQuery.from(Profile.class);
        return session.createQuery(criteriaQuery).getResultList();
    }

    public void deleteProfile(final Profile profile){
        profileDao.delete(profile);
    }

    public List<Highscore> retrieveHighscores(int limit) {

/*        createNewProfile("Sherlock Holmes", Difficulty.EASY, "sherlock.png.");

        Profile profile = profileDao.retrieve(1L);

        Highscore highscore1 = new Highscore(profile, 9000, 25, new Date());
        Highscore highscore2 = new Highscore(profile, 5012, 691, new Date());
        Highscore highscore3 = new Highscore(profile, 1337, 42, new Date());

        highScoreDao.create(highscore1);
        highScoreDao.create(highscore2);
        highScoreDao.create(highscore3);*/

        return new LinkedList<>();
        //return highScoreDao.findHighestScores(limit);
    }

    public void showScreen(Screen screen) {
        game.setScreen(screen);
    }

    public void showIntroScreen() {
        game.setScreen(introScreen);
    }

    public boolean isDatabaseLoaded() {
        return databaseLoaded;
    }

    public void setDatabaseLoaded(boolean databaseLoaded) {
        this.databaseLoaded = databaseLoaded;
    }
}
