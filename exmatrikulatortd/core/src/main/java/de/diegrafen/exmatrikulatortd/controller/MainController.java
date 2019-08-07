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
    private final HighscoreDao highScoreDao;

    /**
     * DAO-Objekt für CRUD-Operationen mit Profil-Objekte
     */
    private final ProfileDao profileDao;

    /**
     * DAO-Objekt für CRUD-Operationen mit Savestate-Objekten
     */
    private final SaveStateDao saveStateDao;

    /**
     * Das aktuelle Profil
     */
    private Profile currentProfile;

    /**
     * Splashscreen
     */
    private final SplashScreen splashScreen;

    /**
     * Das Spiel-Objekt, das unter anderem das Rendern übernimmt
     */
    private final ExmatrikulatorTD game;

    /**
     * Gameserver für den Multiplayer-ModusGame
     */
    private GameServer gameServer;

    /**
     * Gameclient für den Multiplayer-Modus
     */
    private GameClient gameClient;

    private final Screen introScreen;

    private boolean databaseLoaded;

    private String hostAdress = "";

    private boolean host;

    /**
     * Erzeugt einen neuen Maincontroller, das ein Game-Objekt verwaltet
     *
     * @param game Das mit dem Game-Controller zu assoziierende Game-Objekt
     */
    public MainController(ExmatrikulatorTD game) {
        this.game = game;
        System.out.println(game.getAssetManager());
        this.splashScreen = new SplashScreen(this, game.getAssetManager());
        this.introScreen = new IntroScreen(this, game.getAssetManager());
        this.profileDao = new ProfileDao();
        this.highScoreDao = new HighscoreDao();
        this.saveStateDao = new SaveStateDao();

        //
    }

    /**
     * Zeigt den Splash-Screen an
     */
    public void showSplashScreen() {
        game.setScreen(splashScreen);
    }

    /**
     * Zeigt das Hauptmenü an
     */
    public void showMenuScreen() {
        game.setScreen(new MenuScreen(this, game.getAssetManager()));
    }


    /**
     * Zeigt den Endbildschirm einer Spielpartie an
     *
     * @param gamestate Der Spielzustand, dessen Informationen angezeigt werden sollen
     */
    public void setEndScreen(Gamestate gamestate) {
        //game.setScreen(new EndScreen(this, game.getAssetManager(), gamestate));
        showMenuScreen();
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
    public Profile createNewProfile(String profileName, Difficulty preferredDifficulty, String profilePicture) {
        Profile profile = new Profile(profileName, preferredDifficulty, profilePicture);
        profileDao.create(profile);
        return profile;
    }

    public Profile updateProfile(final Profile profile,final String newProfileName,final Difficulty newDifficulty, final String newProfilePicturePath){
        Profile updatedProfile = profile;
        updatedProfile.setProfileName(newProfileName);
        updatedProfile.setPreferredDifficulty(newDifficulty);
        updatedProfile.setProfilePicturePath(newProfilePicturePath);
        profileDao.update(updatedProfile);
        return updatedProfile;
    }

    public void deleteProfile(final Profile profile){
        profileDao.delete(profile);
    }

    /**
     * Erzeugt einen neuen GameServer
     */
    public void createServer() {
        this.gameServer = new GameServer();
        this.gameServer.setMainController(this);
        this.host = true;
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

    public void shutdownConnections() {
        if (gameClient != null) {
            gameClient.shutdown();
            gameClient = null;
        }
        if (gameServer != null) {
            gameServer.shutdown();
            gameServer = null;
        }

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
    public boolean connectToServer(String host) {
        return gameClient.connect(host);
    }

    public List<SaveState> getAllSavestates(){
        return saveStateDao.findAllSaveStates();
    }

    public List<SaveState> getSaveStatesForCurrentProfile() {
        return saveStateDao.findSaveStatesForProfile(currentProfile);
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
    //public void loadSinglePlayerGame(SaveState saveState) {
    public void loadSinglePlayerGame(Long idToLoad) {
        SaveState saveState = saveStateDao.retrieve(idToLoad);
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
        //showScreen(gameScreen);
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
        //showScreen(gameScreen);
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

    public boolean noProfilesYet(){
        return retrieveProfiles().isEmpty();
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

        //return new LinkedList<>();
        return highScoreDao.findHighestScores(limit);
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

    public String getHostAdress() {
        return hostAdress;
    }

    public void setHostAdress(String hostAdress) {
        this.hostAdress = hostAdress;
    }

    public void toggleReady() {
        if (host) {
            gameServer.setServerReady();
        } else {
            gameClient.reportReadiness();
        }
    }

    public HighscoreDao getHighScoreDao() {
        return highScoreDao;
    }

    public ProfileDao getProfileDao() {
        return profileDao;
    }
}
