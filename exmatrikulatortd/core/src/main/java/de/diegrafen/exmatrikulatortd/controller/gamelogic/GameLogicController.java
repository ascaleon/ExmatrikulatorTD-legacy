package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerUpgrader;
import de.diegrafen.exmatrikulatortd.model.*;
import de.diegrafen.exmatrikulatortd.model.enemy.Debuff;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.*;
import de.diegrafen.exmatrikulatortd.persistence.*;
import de.diegrafen.exmatrikulatortd.util.DistanceComparator;
import de.diegrafen.exmatrikulatortd.view.screens.GameView;

import java.util.*;

import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.createNewEnemy;
import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.*;

import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.createNewTower;
import static de.diegrafen.exmatrikulatortd.controller.factories.WaveFactory.*;
import static de.diegrafen.exmatrikulatortd.util.Constants.*;
import static de.diegrafen.exmatrikulatortd.util.Constants.DISTANCE_TOLERANCE;
import static java.awt.geom.Point2D.distance;

/**
 * Der Standard-Spiellogik-Controller.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 01:24
 */
public class GameLogicController implements LogicController {

    /**
     * Der Hauptcontroller
     */
    private MainController mainController;

    /**
     * Der Spielzustand
     */
    private Gamestate gamestate;

    /**
     * Der Spielbildschirm
     */
    private GameView gameScreen;

    /**
     * Das lokale Spieler-Profil
     */
    private Profile profile;

    /**
     * DAO für CRUD-Operationen mit Spielzustand-Objekten
     */
    private GameStateDao gameStateDao;

    /**
     * DAO für CRUD-Operationen mit Spielstand-Objekten
     */
    private SaveStateDao saveStateDao;

    private float auraRefreshTimer = 0;

    private int localPlayerNumber;

    private boolean multiplayer;

    private GameServer gameServer;

    private boolean server;

    private boolean pause;

    private String mapPath;

    /**
     *
     */
    public GameLogicController(MainController mainController, Profile profile, int numberOfPlayers, int localPlayerNumber,
                               int gamemode, GameView gameScreen, String mapPath, GameServer gameServer) {
        this(mainController, profile, numberOfPlayers, localPlayerNumber, gamemode, gameScreen, mapPath);
        this.gameServer = gameServer;
        this.server = true;
        gameServer.attachRequestListeners(this);
    }

    /**
     *
     */
    public GameLogicController(MainController mainController, Profile profile, int numberOfPlayers, int localPlayerNumber,
                               int gamemode, GameView gameScreen, String mapPath) {

        this.mainController = mainController;
        this.profile = profile;
        this.mapPath = mapPath;
        this.gameStateDao = new GameStateDao();
        this.saveStateDao = new SaveStateDao();
        this.localPlayerNumber = localPlayerNumber;
        this.multiplayer = gamemode >= MULTIPLAYER_DUEL;
        System.out.println("Multiplayer? " + multiplayer);
        this.gameScreen = gameScreen;
        this.gameScreen.setLogicController(this);


        this.gamestate = createGameState(gamemode, numberOfPlayers);
        this.gameScreen.setGameState(gamestate);
        this.gamestate.registerObserver(gameScreen);
        this.gamestate.getPlayers().forEach(player -> player.registerObserver(gameScreen));
        initializeMap(mapPath);
        this.gameScreen.loadMap(mapPath);
    }

    private Gamestate createGameState(int gamemode, int numberOfPlayers) {

        List<Player> players = new LinkedList<>();

        // TODO: Informationen wie Spielerinnen-Name etc. müssen auch irgendwie berücksichtigt werden
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(i));
        }

        List<Wave> waves = new LinkedList<>();
        waves.add(createWave(HEAVY_WAVE));
        int i = 0;
        while (i < 12) {
            waves.add(createWave(REGULAR_WAVE));
            waves.add(createWave(REGULAR_AND_HEAVY_WAVE));
            i++;
        }

        Gamestate gamestate = new Gamestate(players, waves);

        gamestate.setGameMode(gamemode);
        if (gamemode == ENDLESS_SINGLE_PLAYER_GAME | gamemode == MULTIPLAYER_ENDLESS_GAME) {
            gamestate.setEndlessGame(true);
        }

        return gamestate;
    }

    public GameLogicController(MainController mainController, SaveState saveState, GameView gameView, GameServer gameServer) {
        this(mainController, saveState, gameView);
        this.gameServer = gameServer;
        this.server = true;
        gameServer.attachRequestListeners(this);

    }

    public GameLogicController(MainController mainController, SaveState saveState, GameView gameView) {
        this.mainController = mainController;
        this.gamestate = new Gamestate(saveState.getGamestate());
        this.profile = saveState.getProfile();
        this.mapPath = saveState.getMapPath();
        this.localPlayerNumber = saveState.getLocalPlayerNumber();
        this.multiplayer = saveState.isMultiplayer();
        this.gameScreen = gameView;

        this.gameScreen.setLogicController(this);
        this.gameScreen.setGameState(gamestate);
        this.gamestate.registerObserver(gameScreen);
        this.gamestate.getPlayers().forEach(player -> player.registerObserver(gameScreen));
        initializeMap(mapPath);
        this.gameScreen.loadMap(mapPath);
    }

    /**
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    @Override
    public void update(float deltaTime) {
        if (!gamestate.isGameOver() && !pause) {
            determineNewRound();
            if (gamestate.isRoundEnded()) {
                determineGameOver();
            }

            if (!gamestate.isGameOver()) {
                if (gamestate.isRoundEnded()) {
                    gamestate.setRoundEnded(false);
                    startNewRound();
                }
                //applyPlayerDamage();
                spawnWave(deltaTime);
                applyAuras(deltaTime);
                applyMovement(deltaTime);
                makeAttacks(deltaTime);
                moveProjectiles(deltaTime);
                applyBuffsToTowers(deltaTime);
                applyDebuffsToEnemies(deltaTime);
            }
        }
    }

    /**
     * Wendet Auras auf die Objekte des Spiels an
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    private void applyAuras(float deltaTime) {

        if (auraRefreshTimer > 0) {
            auraRefreshTimer -= deltaTime;
            return;
        } else {
            auraRefreshTimer = AURA_REFRESH_RATE;
            auraRefreshTimer -= deltaTime;
        }

        for (Tower tower : gamestate.getTowers()) {
            List<Aura> auras = tower.getAuras();
            for (Aura aura : auras) {
                List<Debuff> debuffs = aura.getDebuffs();
                List<Buff> buffs = aura.getBuffs();

                for (Debuff debuff : debuffs) {
                    List<Enemy> enemiesInRange = getEnemiesInTowerRange(tower, aura.getRange());
                    for (Enemy enemyInRange : enemiesInRange) {
                        addDebuffToEnemy(enemyInRange, debuff);
                    }
                }

                for (Buff buff : buffs) {
                    List<Tower> towersInRange = getTowersInRange(tower.getxPosition(), tower.getyPosition(), aura.getRange());
                    for (Tower towerInRange : towersInRange) {
                        addBuffToTower(towerInRange, buff);
                    }
                }
            }
        }
    }

    /**
     *
     */
    private void addDebuffToEnemy(Enemy enemy, Debuff debuff) {
        for (Debuff debuffToCheck : enemy.getDebuffs()) {
            if (debuff.getName().equals(debuffToCheck.getName())) {
                return;
            }
        }

        enemy.addDebuff(new Debuff(debuff));
    }

    /**
     *
     */
    private void addBuffToTower(Tower tower, Buff buff) {
        for (Buff buffToCheck : tower.getBuffs()) {
            if (buff.getName().equals(buffToCheck.getName())) {
                return;
            }
        }

        tower.addBuff(new Buff(buff));
    }

    /**
     *
     * Ermittelt eine Liste von Türmen in einem Umkreis um einen Punkt.
     *
     * @param xPosition Die x-Position des Umkreises, in dem gesucht wird
     * @param yPosition Die y-Position des Umkreises, in dem gesucht wird
     * @param range Der Radius, in dem gesucht werden soll
     * @return Die Liste der gefundenen Türme
     */
    private List<Tower> getTowersInRange(float xPosition, float yPosition, float range) {
        List<Tower> towersInRange = new LinkedList<>();

        for (Tower tower : gamestate.getTowers()) {

            double distance = distance(xPosition, yPosition, tower.getxPosition(), tower.getyPosition());

            if (distance <= range) {
                towersInRange.add(tower);
            }
        }

        return towersInRange;
    }

    /**
     * Gibt alle Gegner zurück, die sich in einem angegebenen Umkreis eines Turms befinden.
     *
     * @param tower Der Turm, der als Referenzpunkt genutzt wird
     * @param range Der Radius um den Turm, in dem gesucht wird
     * @return Die Liste der gefundenen Gegner
     */
    private List<Enemy> getEnemiesInTowerRange(Tower tower, float range) {
        List<Enemy> enemiesInRange = new LinkedList<>();

        for (Enemy enemy : tower.getOwner().getAttackingEnemies()) {
            if (isEnemyInRangeOfTower(enemy, tower, range)) {
                enemiesInRange.add(enemy);
            }
        }

        return enemiesInRange;
    }

    /**
     * Gibt alle Gegner zurück, die sich Flächenschaden-Radius eines Projektils befinden
     *
     * @param projectile Das Projektil, das den Flächenschaden verursacht
     * @return Die Liste der ermittelten Gegner
     */
    private List<Enemy> getEnemiesInSplashRadius(Projectile projectile) {
        List<Enemy> enemiesInRange = new LinkedList<>();

        for (Enemy enemy : projectile.getTowerThatShot().getOwner().getAttackingEnemies()) {
            if (isEnemeyInSplashRadius(enemy, projectile)) {
                enemiesInRange.add(enemy);
            }
        }

        return enemiesInRange;
    }

    /**
     * Überprüft, ob sich ein Gegner im Flächenschaden-Radius eines Projektils befindet.
     *
     * @param enemy Der Gegner, der geprüft werden soll.
     * @param projectile Das Projektil, von dem der Flächenschaden ausgeht.
     * @return @code{true}, wenn sich der Gegner im Radius befindet. Ansonsten @code{false}
     */
    private boolean isEnemeyInSplashRadius(Enemy enemy, Projectile projectile) {
        double distance = distance(projectile.getxPosition(), projectile.getyPosition(), enemy.getxPosition(), enemy.getyPosition());
        return distance <= projectile.getSplashRadius();
    }

    /**
     * Überprüft, ob sich ein Gegner in Reichweite eines Turmes befindet.
     *
     * @param enemy Der Gegner, der geprüft werden soll.
     * @param tower Der Turm, dessen Reichweite geprüft werden soll
     * @param range Die Reichweite des Turms
     * @return @code{true}, wenn sich der Gegner im Radius befindet. Ansonsten @code{false}
     */
    private boolean isEnemyInRangeOfTower(Enemy enemy, Tower tower, float range) {
        double distance = distance(tower.getxPosition(), tower.getyPosition(), enemy.getxPosition(), enemy.getyPosition());
        return distance <= range;
    }

    /**
     *
     * Wendet die Buffs aller Türme auf diese an.
     *
     * @param deltaTime Die Zeit, die seit dem letzten Rendern vergangen ist
     */
    private void applyBuffsToTowers(float deltaTime) {
        for (Tower tower : gamestate.getTowers()) {

            List<Buff> buffsToRemove = new LinkedList<>();

            tower.setCurrentAttackDamage(tower.getBaseAttackDamage());
            tower.setCurrentAttackSpeed(tower.getBaseAttackSpeed());

            for (Buff buff : tower.getBuffs()) {
                tower.setCurrentAttackSpeed(tower.getCurrentAttackSpeed() * (1 / buff.getAttackSpeedMultiplier()));
                tower.setCurrentAttackDamage(tower.getCurrentAttackDamage() * buff.getAttackDamageMultiplier());
                buff.setDuration(buff.getDuration() - deltaTime);
                if (buff.getDuration() < 0) {
                    buffsToRemove.add(buff);
                }
            }

            buffsToRemove.forEach(tower::removeBuff);
        }
    }

    /**
     *
     * Wendet die Buffs aller Gegner auf diese an.
     *
     * @param deltaTime Die Zeit, die seit dem letzten Rendern vergangen ist
     */
    private void applyDebuffsToEnemies(float deltaTime) {
        for (Enemy enemy : gamestate.getEnemies()) {

            List<Debuff> debuffsToRemove = new LinkedList<>();

            float oldMaxHitPoints = enemy.getCurrentMaxHitPoints();

            enemy.setCurrentSpeed(enemy.getBaseSpeed());
            enemy.setCurrentArmor(enemy.getBaseArmor());
            enemy.setCurrentMaxHitPoints(enemy.getBaseMaxHitPoints());
            // TODO: Damage over Time Debuffs ergänzen.

            for (Debuff debuff : enemy.getDebuffs()) {
                enemy.setCurrentSpeed(enemy.getCurrentSpeed() * debuff.getSpeedMultiplier());
                enemy.setCurrentArmor(enemy.getCurrentArmor() + debuff.getArmorBonus());
                enemy.setCurrentMaxHitPoints(enemy.getCurrentMaxHitPoints() + debuff.getHealthBonus());

                if (enemy.getCurrentMaxHitPoints() < 0) {
                    enemy.setCurrentMaxHitPoints(1);
                }

                if (!debuff.isPermanent()) {
                    debuff.setDuration(debuff.getDuration() - deltaTime);
                }

                if (debuff.getDuration() < 0) {
                    debuffsToRemove.add(debuff);
                }
            }

            enemy.setCurrentHitPoints(enemy.getCurrentHitPoints() * enemy.getCurrentMaxHitPoints() / oldMaxHitPoints);

            for (Debuff debuff : debuffsToRemove) {
                enemy.removeDebuff(debuff);
            }
        }
    }

    /**
     * Bewegt die Einheiten
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    private void applyMovement(float deltaTime) {
        for (Enemy enemy : gamestate.getEnemies()) {
            if (Math.floor(getDistanceToNextPoint(enemy)) <= DISTANCE_TOLERANCE) {
                enemy.incrementWayPointIndex();
            }
            if (enemy.getWayPointIndex() >= enemy.getAttackedPlayer().getWayPoints().size()) {
                applyDamageToPlayer(enemy);
                if (enemy.isRespawning()) {
                    addEnemy(new Enemy(enemy), enemy.getAttackedPlayer().getPlayerNumber());
                }
                break;
            }

            moveInTargetDirection(enemy, deltaTime);
            enemy.notifyObserver();
        }
    }


    /**
     * Bewegt die Geschosse. Trifft ein Geschoss sein Ziel, wird diesem Schaden zugefügt.
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    private void moveProjectiles(float deltaTime) {
        List<Projectile> projectilesThatHit = new ArrayList<>();

        for (Projectile projectile : gamestate.getProjectiles()) {

            if (Math.floor(getDistanceToTarget(projectile)) <= DISTANCE_TOLERANCE) {
                projectilesThatHit.add(projectile);
                continue;
            }
            moveInTargetDirection(projectile, deltaTime);
        }

        for (Projectile projectile : projectilesThatHit) {
            applyDamageToTarget(projectile);
        }
    }


    private void moveInTargetDirection(Enemy enemy, float deltaTime) {
        // FIXME: Bei Verschieben des Fensters wird die "Kollision" mit Wegpunkten nicht korrekt berechnet
        // Hierfür könnte eine Aktualisierung des Spielzustandes, unabhängig von der der View, tatsächlich Sinn ergeben...
        Coordinates nextWayPoint = enemy.getAttackedPlayer().getWayPoints().get(enemy.getWayPointIndex());

        float xPosition = enemy.getxPosition();
        float yPosition = enemy.getyPosition();
        float targetxPosition = nextWayPoint.getXCoordinate() * gamestate.getTileWidth();
        float targetyPosition = nextWayPoint.getYCoordinate() * gamestate.getTileHeight();
        float currentSpeed = enemy.getCurrentSpeed();

        float angle = (float) Math.atan2(targetyPosition - yPosition, targetxPosition - xPosition);

        enemy.setTargetxPosition(targetxPosition);// + tileSize / 2;
        enemy.setTargetyPosition(targetyPosition);// + tileSize / 2;
        enemy.setxPosition(xPosition + (float) Math.cos(angle) * currentSpeed * deltaTime);
        enemy.setyPosition(yPosition + (float) Math.sin(angle) * currentSpeed * deltaTime);
    }

    private void moveInTargetDirection(Projectile projectile, float deltaTime) {

        float xPosition = projectile.getxPosition();
        float yPosition = projectile.getyPosition();
        float targetxPosition = projectile.getTarget().getxPosition();
        float targetyPosition = projectile.getTarget().getyPosition();

        float speed = projectile.getSpeed();

        float angle = (float) Math.atan2(targetyPosition - yPosition, targetxPosition - xPosition);
        projectile.setxPosition(xPosition + (float) Math.cos(angle) * speed * deltaTime);
        projectile.setyPosition(yPosition + (float) Math.sin(angle) * speed * deltaTime);
        projectile.setTargetxPosition(targetxPosition);
        projectile.setTargetyPosition(targetyPosition);
        projectile.notifyObserver();
    }

    private void applyDamageToTarget(Projectile projectile) {
        List<Enemy> enemiesHit = new LinkedList<>();
        Enemy mainTarget = projectile.getTarget();
        mainTarget.setCurrentHitPoints(mainTarget.getCurrentHitPoints() - projectile.getDamage() * calculateDamageMultiplier(mainTarget, projectile.getAttackType()));
        enemiesHit.add(mainTarget);

        if (projectile.getSplashRadius() > 0) {
            List<Enemy> enemiesInSplashRadius = getEnemiesInSplashRadius(projectile);
            for (Enemy enemyInSplashRadius : enemiesInSplashRadius) {
                float damageMultiplier = calculateDamageMultiplier(enemyInSplashRadius, projectile.getAttackType());
                enemyInSplashRadius.setCurrentHitPoints(enemyInSplashRadius.getCurrentHitPoints() - projectile.getDamage() * projectile.getsplashAmount() * damageMultiplier);
                enemiesHit.add(enemyInSplashRadius);
            }
        }

        for (Enemy enemyHit : enemiesHit) {
            for (Debuff debuff : projectile.getApplyingDebuffs()) {
                addDebuffToEnemy(enemyHit, debuff);
            }
            calculateDamageImpact(enemyHit, projectile.getTowerThatShot());
        }
        removeProjectile(projectile);
    }

    private void calculateDamageImpact(Enemy enemy, Tower tower) {
        if (enemy.getCurrentHitPoints() <= 0) {
            Player attackedPlayer = enemy.getAttackedPlayer();
            if (attackedPlayer != null) {
                attackedPlayer.addToResources(enemy.getBounty());
                attackedPlayer.addToScore(enemy.getPointsGranted());
                attackedPlayer.notifyObserver();
                removeEnemy(enemy);
            }
            tower.setCurrentTarget(null);
        }

    }

    private void removeProjectile(Projectile projectileToRemove) {
        projectileToRemove.setRemoved(true);
        projectileToRemove.notifyObserver();
        gamestate.removeProjectile(projectileToRemove);
    }

    private float calculateDamageMultiplier(Enemy enemy, int attackType) {
        float armor = enemy.getCurrentArmor();
        float damageModifier = 1;

        // TODO: Eindeutigeren Namen hinzufügen
        float armorEffect = ATTACK_DEFENSE_MATRIX[attackType][enemy.getArmorType()];

        if (armor > 0) {
            damageModifier = 1 - (armor * DAMAGE_REDUCTION_FACTOR) / (1 + DAMAGE_REDUCTION_FACTOR * armor);
        } else if (armor < 0) {
            damageModifier = 2 - (float) Math.pow(0.94, -armor);
        }

        return armorEffect * damageModifier;
    }

    /**
     * Berechnet die Distanz eines Geschosses zu seinem Ziel
     *
     * @param projectile Das Geschoss, für das die Distanz zum Zielobjekt berechnet werden soll
     * @return Die Distanz zum Zielobjekt des Projektils
     */
    private float getDistanceToTarget(Projectile projectile) {
        float x1 = projectile.getxPosition();
        float x2 = projectile.getTarget().getxPosition();
        float y1 = projectile.getyPosition();
        float y2 = projectile.getTarget().getyPosition();
        return (float) distance(x1, y1, x2, y2);
    }

    /**
     * Lässt die Türme angreifen
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    private void makeAttacks(float deltaTime) {

        for (Tower tower : gamestate.getTowers()) {

            if (tower.getCooldown() > 0) {
                tower.setCooldown(tower.getCooldown() - deltaTime);
            }

            Enemy newTarget = null;

            if (!isTargetStillInTowerRange(tower)) {
                tower.setCurrentTarget(null);
                float timeSinceLastSearch = tower.getTimeSinceLastSearch();
                if (timeSinceLastSearch >= SEARCH_TARGET_INTERVAL) {
                    List<Enemy> enemiesInRange = getEnemiesInTowerRange(tower, tower.getAttackRange());
                    enemiesInRange.sort(new DistanceComparator(tower.getxPosition(), tower.getyPosition()));
                    if (!enemiesInRange.isEmpty()) {
                        newTarget = enemiesInRange.get(0);
                    }
                    tower.setCurrentTarget(newTarget);
                    tower.setTimeSinceLastSearch(0);
                } else {
                    tower.setTimeSinceLastSearch(timeSinceLastSearch + deltaTime);
                }
            }

            if (tower.getCurrentTarget() != null) {
                letTowerAttack(tower);
            }
        }
    }

    private void letTowerAttack(Tower tower) {
        Enemy enemy = tower.getCurrentTarget();
        if (tower.getCooldown() <= 0) {
            switch (tower.getAttackStyle()) {
                // TODO: Differenzierung nach Projektilarten einbauen
                case PROJECTILE:
                    addProjectile(tower);
                    break;
                case IMMEDIATE: //TODO: Animationen wie Blitze oder Ähnliches triggern lassen.
                    enemy.setCurrentHitPoints(enemy.getCurrentHitPoints() - tower.getCurrentAttackDamage());
                    calculateDamageImpact(enemy, tower);
            }

            tower.setCooldown(tower.getCurrentAttackSpeed());
        }
    }

    private void applyDamageToPlayer(Enemy enemy) {
        Player attackedPlayer = enemy.getAttackedPlayer();
        attackedPlayer.setCurrentLives(attackedPlayer.getCurrentLives() - enemy.getAmountOfDamageToPlayer());
        removeEnemy(enemy);
    }

    private void removeEnemy(Enemy enemy) {
        enemy.getAttackedPlayer().removeEnemy(enemy);
        enemy.setAttackedPlayer(null);
        enemy.setDebuffs(new LinkedList<>());
        gamestate.removeEnemy(enemy);
        enemy.setGameState(null);
        enemy.setRemoved(true);
        enemy.notifyObserver();
    }

    private float getDistanceToNextPoint(Enemy enemy) {
        float x1 = enemy.getxPosition();
        float x2 = enemy.getTargetxPosition();
        float y1 = enemy.getyPosition();
        float y2 = enemy.getTargetyPosition();
        return (float) distance(x1, y1, x2, y2);
    }

    /**
     * Ermittelt, ob das Ziel eines Turms immer noch in Reichweite ist
     *
     * @param tower Der Turm, für den die Ermittlung durchgeführt werden soll.
     * @return true, wenn das Ziel immer noch in Reichweite ist, ansonsten false
     */
    private boolean isTargetStillInTowerRange(Tower tower) {

        Enemy target = tower.getCurrentTarget();

        return target != null && !target.isRemoved() & isEnemyInRangeOfTower(target, tower, tower.getAttackRange());
    }

    /**
     * Spawnt die nächste Angriffswelle
     *
     * @param deltaTime Die Zeit seit dem letzten Rendern.
     */
    private void spawnWave(float deltaTime) {
        float timeUntilNextRound = gamestate.getTimeUntilNextRound();
        if (timeUntilNextRound <= 0) {

            int roundNumber = gamestate.getRoundNumber();

            for (Player player : gamestate.getPlayers()) {

                List<Wave> waves = player.getWaves();
                Wave currentWave = waves.get(roundNumber);

                if (currentWave.getEnemySpawnIndex() == 0) {
                    Collections.shuffle(currentWave.getEnemies());
                }

                if (currentWave.getEnemies().size() <= currentWave.getEnemySpawnIndex()) {
                    currentWave.setEnemySpawnIndex(0);
                    player.setEnemiesSpawned(true);
                    gamestate.setNewRound(false);
                    if (gamestate.isEndlessGame()) {
                        waves.add(new Wave(waves.get(roundNumber)));
                    }
                    break;
                }

                if (!player.isEnemiesSpawned() && player.getTimeSinceLastSpawn() > TIME_BETWEEN_SPAWNS) {
                    Enemy enemy = currentWave.getEnemies().get(currentWave.getEnemySpawnIndex());
                    currentWave.setEnemySpawnIndex(currentWave.getEnemySpawnIndex() + 1);
                    addEnemy(enemy, player.getPlayerNumber());
                    player.setTimeSinceLastSpawn(0);
                } else {
                    player.setTimeSinceLastSpawn(player.getTimeSinceLastSpawn() + deltaTime);
                }
            }
        }

        gamestate.setTimeUntilNextRound(timeUntilNextRound - deltaTime);
    }

    /**
     * Stellt fest, ob die Runde zuende ist
     */
    private void determineNewRound() {
        if (gamestate.getEnemies().isEmpty() && !gamestate.isNewRound()) {
            gamestate.setRoundEnded(true);
            if (gamestate.getRoundNumber() < gamestate.getNumberOfRounds()) {
                gamestate.setRoundNumber(gamestate.getRoundNumber() + 1);
            }

            List<Projectile> remainingProjectiles = new LinkedList<>(gamestate.getProjectiles());
            for (Projectile remainingProjectile : remainingProjectiles) {
                removeProjectile(remainingProjectile);
                remainingProjectile.notifyObserver();
            }

            gamestate.notifyObserver();
            //gameStateDao.update(gamestate);
            System.out.println("Runde zuende!");
        }
    }

    /**
     * Ermittelt, ob das Spiel vorbei ist und setzt in diesem Fall das Attribut gameOver des GameStates auf true.
     * Ansonsten wird das Attribut auf false gesetzt.
     */
    private void determineGameOver() {
        boolean gameOver = false;

        determineLosers();

        if (!gamestate.isEndlessGame() && gamestate.getRoundNumber() >= gamestate.getNumberOfRounds()) {
            gameOver = true;
            for (Player player : gamestate.getPlayers()) {
                player.setVictorious(true);
            }
            System.out.println("Alle waren Sieger, obwohl einer nur gewinnen kann...");
        } else if (multiplayer && determineWinner() >= 0) {
            int victoriousPlayer = determineWinner();
            gamestate.getPlayerByNumber(victoriousPlayer).setVictorious(true);
            gameOver = true;
            System.out.println("Spielerin " + (victoriousPlayer + 1) + " hat gewonnen!");
        } else if (haveAllPlayersLost()) {
            gameOver = true;
            System.out.println("Alle Spielerinnen haben verloren!");
        }

        gamestate.setGameOver(gameOver);
    }

    private void determineLosers() {

        for (Player player : gamestate.getPlayers()) {
            if (!player.hasLost() & player.getCurrentLives() <= 0) {
                player.setLost(true);
                System.out.println("Spielerin " + (player.getPlayerNumber() + 1) + " hat verloren!");
            }
        }
    }

    /**
     * Bestimmt, ob alle Spielerinnen verloren haben
     *
     * @return true, wenn alle Spielerinnen verloren haben, ansonsten false.
     */
    private boolean haveAllPlayersLost() {

        boolean allPlayersLost = true;

        for (Player player : gamestate.getPlayers()) {
            allPlayersLost &= player.hasLost();
        }
        return allPlayersLost;

    }

    /**
     * Bestimmt die Nummer der siegreichen Spielerin, sollte es eine solche geben
     *
     * @return Wenn es eine siegreiche Spielerin geben sollte, deren Spielernummer. Ansonsten -1
     */
    private int determineWinner() {

        int victoriousPlayer = -1;

        int numberOfPlayers = gamestate.getPlayers().size();

        for (Player player : gamestate.getPlayers()) {
            if (player.hasLost()) {
                numberOfPlayers -= 1;
            } else {
                victoriousPlayer = player.getPlayerNumber();
            }
        }

        if (numberOfPlayers != 1) {
            victoriousPlayer = -1;
        }

        return victoriousPlayer;
    }

    /**
     * Startet eine neue Runde
     */
    private void startNewRound() {
        System.out.println("New round started!");
        gamestate.setNewRound(true);
        gamestate.setRoundEnded(false);
        for (Player player : gamestate.getPlayers()) {
            player.setEnemiesSpawned(false);
        }
        gamestate.setTimeUntilNextRound(TIME_BETWEEN_ROUNDS);
    }

    /**
     * Initialisiert die Kollisionsmatrix der Karte
     *
     * @param mapPath Der Dateipfad der zu ladenden Karte
     */
    public void initializeMap(String mapPath) {

        TiledMap tiledMap = new TmxMapLoader().load(mapPath);

        int numberOfColumns = (int) tiledMap.getProperties().get("width");
        int numberOfRows = (int) tiledMap.getProperties().get("height");

        gamestate.setTileWidth((int) tiledMap.getProperties().get("tilewidth"));
        gamestate.setTileHeight((int) tiledMap.getProperties().get("tileheight"));
        gamestate.setNumberOfColumns(numberOfColumns);
        gamestate.setNumberOfRows(numberOfRows);

        for (int i = 0; i < gamestate.getPlayers().size(); i++) {
            Player player = gamestate.getPlayerByNumber(i);
            TiledMapTileLayer waypointLayer = (TiledMapTileLayer) tiledMap.getLayers().get("WaypointsPlayer" + i);
            List<Coordinates> wayPoints = new LinkedList<>();
            for (int j = 0; j < numberOfRows; j++) {
                for (int k = 0; k < numberOfColumns; k++) {
                    if (waypointLayer.getCell(k, j) != null) {
                        int waypointIndex = (int) waypointLayer.getCell(k, j).getTile().getProperties().get("waypointNumber");
                        wayPoints.add(new Coordinates(k, j, i, waypointIndex));
                    }
                }
            }
            wayPoints.sort(Comparator.comparingInt(Coordinates::getWaypointIndex));
            player.setWayPoints(wayPoints);
        }

        TiledMapTileLayer buildPermissionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Kollisionsmatrix");

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                int buildableByPlayer = (int) buildPermissionLayer.getCell(j, i).getTile().getProperties().get("buildableByPlayer");
                addGameMapTile(j, i, buildableByPlayer);
            }
        }

        // TODO: Eventuell obsolet, es sei denn, wir wollen Graph-Algorithmen auf der Map einsetzen.
        for (Coordinates mapCell : gamestate.getCollisionMatrix()) {

            int mapCellXCoordinate = mapCell.getxCoordinate();
            int mapCellYCoordinate = mapCell.getyCoordinate();
            int numberOfCols = gamestate.getNumberOfColumns();

            if (mapCellXCoordinate > 0) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * mapCellYCoordinate + mapCellXCoordinate - 1));
            }
            if (mapCellXCoordinate < numberOfColumns - 1) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * mapCellYCoordinate + mapCellXCoordinate + 1));
            }
            if (mapCellYCoordinate > 0) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * (mapCellYCoordinate - 1) + mapCellXCoordinate));
            }
            if (mapCellYCoordinate < numberOfRows - 1) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * (mapCellYCoordinate + 1) + mapCellXCoordinate));
            }
        }
    }

    /**
     * Fügt der im Spielzustand vorhandenen Karte ein neues Feld hinzu
     *
     * @param xCoordinate       Die x-Koordinate des Feldes auf der Karte
     * @param yCoordinate       Die y-Koordinate des Feldes auf der Karte
     * @param buildableByPlayer Die Nummer der Spielerin, die auf dem Feld bauen darf. -1, wenn das Feld nicht bebaubar ist
     */
    private void addGameMapTile(int xCoordinate, int yCoordinate, int buildableByPlayer) {
        Coordinates coordinates = new Coordinates(xCoordinate, yCoordinate, buildableByPlayer);
        gamestate.addCoordinatesToCollisionMatrix(coordinates);
    }

    /**
     * Gibt die y-Koordinate zurück, die der angegebenen x-Position auf der Karte entspricht
     *
     * @param xPosition Die x-Position, für die die x-Koordinate ermittelt werden soll
     * @return Die passende x-Koordinate
     */
    public int getXCoordinateByPosition(float xPosition) {
        return (int) xPosition / gamestate.getTileWidth();
    }

    /**
     * Gibt die y-Koordinate zurück, die der angegebenen y-Position auf der Karte entspricht
     *
     * @param yPosition Die y-Position, für die die y-Koordinate ermittelt werden soll
     * @return Die passende y-Koordinate
     */
    public int getYCoordinateByPosition(float yPosition) {
        return (int) yPosition / gamestate.getTileHeight();
    }

    Coordinates getMapCellByXandYCoordinates(int xCoordinate, int yCoordinate) {

        yCoordinate *= gamestate.getNumberOfColumns();

        return gamestate.getMapCellByListIndex(xCoordinate + yCoordinate);
    }

    private void addEnemy(Enemy enemy, int playerNumber) {
        Player attackedPlayer = gamestate.getPlayerByNumber(playerNumber);
        attackedPlayer.addEnemy(enemy);
        enemy.setAttackedPlayer(attackedPlayer);
        gamestate.addEnemy(enemy);
        enemy.setGameState(gamestate);
        enemy.addDebuff(generateDifficultyDebuff(enemy.getAttackedPlayer().getDifficulty()));
        enemy.addDebuff(generateRoundNumberDebuff());
        setEnemyToStartPosition(enemy);
        gameScreen.addEnemy(enemy);
        enemy.notifyObserver();
    }

    private Debuff generateDifficultyDebuff(Difficulty difficulty) {

        Debuff difficultyDebuff = new Debuff();

        difficultyDebuff.setPermanent(true);

        switch (difficulty) {
            case TESTMODE:
                difficultyDebuff.setArmorBonus(-100);
                difficultyDebuff.setHealthBonus(-1000);
                difficultyDebuff.setSpeedMultiplier(0.5f);
                break;
            case EASY:
                difficultyDebuff.setArmorBonus(-6);
                difficultyDebuff.setSpeedMultiplier(0.7f);
                break;
            case HARD:
                difficultyDebuff.setArmorBonus(6);
                difficultyDebuff.setSpeedMultiplier(1.3f);
                break;
        }

        return difficultyDebuff;
    }

    private Debuff generateRoundNumberDebuff() {

        Debuff roundNumberDebuff = new Debuff();

        roundNumberDebuff.setArmorBonus(gamestate.getRoundNumber() * ARMOR_INCREASE_PER_LEVEL);
        roundNumberDebuff.setSpeedMultiplier(1 + ((float) gamestate.getRoundNumber()) / 100 * SPEED_INCREASE_PER_LEVEL);

        return roundNumberDebuff;
    }

    /**
     * Setzt einen Gegner an seine Startposition (zurück)
     *
     * @param enemy Der Gegner, der an die Startposition (zurück)gesetzt werden soll
     */
    private void setEnemyToStartPosition(Enemy enemy) {
        Coordinates startCoordinates = enemy.getAttackedPlayer().getWayPoints().get(0);
        enemy.setxPosition(startCoordinates.getXCoordinate() * gamestate.getTileWidth());// + tileSize / 2;
        enemy.setyPosition(startCoordinates.getYCoordinate() * gamestate.getTileHeight());// + tileSize / 2;
    }

    void addTower(Tower tower, int xPosition, int yPosition, int playerNumber) {
        Player owningPlayer = gamestate.getPlayerByNumber(playerNumber);
        tower.setOwner(owningPlayer);
        owningPlayer.addTower(tower);

        Coordinates coordinates = getMapCellByXandYCoordinates(xPosition, yPosition);
        tower.setPosition(coordinates);
        coordinates.setTower(tower);

        tower.setGamestate(gamestate);
        gamestate.addTower(tower);
        gameScreen.addTower(tower);
    }

    private void addProjectile(Tower tower) {
        Projectile projectile = new Projectile(tower);

        gamestate.addProjectile(projectile);
        gameScreen.addProjectile(projectile);
    }

    /**
     * Baut einen neuen Turm an den angegebenen Koordinaten auf der Karte
     *
     * @param towerType    Der Typ des zu bauenden Turms
     * @param xCoordinate  Die x-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param yCoordinate  Die y-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param playerNumber Die Nummer der Spielerin, die den Turm bauen will
     */
    @Override
    public void buildTower(int towerType, int xCoordinate, int yCoordinate, int playerNumber) {

        if (checkIfCoordinatesAreBuildable(xCoordinate, yCoordinate, playerNumber)) {
            Tower tower = createNewTower(towerType);
            int towerPrice = tower.getPrice();
            Player player = gamestate.getPlayerByNumber(playerNumber);
            int playerResources = player.getResources();
            if (playerResources >= towerPrice) {
                if (server) {
                    gameServer.buildTower(towerType, xCoordinate, yCoordinate, playerNumber);
                }
                player.setResources(playerResources - towerPrice);
                addTower(tower, xCoordinate, yCoordinate, playerNumber);
                player.notifyObserver();

            } else {
                displayErrorMessage("Nicht genug Ressourcen!", playerNumber);
            }
        } else {
            displayErrorMessage("Hier kann nicht gebaut werden!", playerNumber);
        }
    }

    public boolean checkIfCoordinatesAreBuildable(int xCoordinate, int yCoordinate, int playerNumber) {

        boolean buildable = true;

        if (!coordinatesOnTheMap(xCoordinate, yCoordinate, gamestate)) {
            System.out.println("Koordinaten nicht auf der Map!");
            buildable = false;
        } else if (!playerExists(playerNumber, gamestate)) {
            System.out.println("Spieler gibt es nicht!");
            buildable = false;
        } else {
            Coordinates mapCell = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);
            if (mapCell.getTower() != null) {
                buildable = false;
            } else if (mapCell.getBuildableByPlayer() != playerNumber) {
                buildable = false;
            }
        }

        return buildable;
    }

    private boolean coordinatesOnTheMap(int xCoordinate, int yCoordinate, Gamestate gamestate) {

        boolean onTheMap;

        if (xCoordinate < 0 | yCoordinate < 0) {
            onTheMap = false;
        } else {
            onTheMap = xCoordinate < gamestate.getNumberOfColumns() & yCoordinate < gamestate.getNumberOfRows();
        }

        return onTheMap;
    }

    private boolean playerExists(int playerNumber, Gamestate gamestate) {
        return playerNumber >= 0 | gamestate.getPlayers().size() < playerNumber;
    }

    public boolean hasCellTower(int xCoordinate, int yCoordinate) {
        Coordinates coordinates = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);

        return coordinates.getTower() != null;
    }

    /**
     * Verkauft einen Turm
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    @Override
    public void sellTower(int xCoordinate, int yCoordinate, int playerNumber) {

        Coordinates mapCell = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);

        Tower tower = mapCell.getTower();

        if (tower == null) {
            displayErrorMessage("Hier gibt es keinen Turm zum Verkaufen!", playerNumber);
        } else if (tower.getOwner().getPlayerNumber() != playerNumber) {
            displayErrorMessage("Du darfst diesen Turm nicht verkaufen!", playerNumber);
        } else {
            tower.getOwner().addToResources(tower.getSellPrice());
            tower.getOwner().notifyObserver();
            removeTower(tower);
            if (server) {
                gameServer.sellTower(xCoordinate, yCoordinate, playerNumber);
            }
        }
    }

    /**
     * Entfernt einen Turm aus dem Spiel
     *
     * @param tower Der zu entfernende Turm
     */
    void removeTower(Tower tower) {
        tower.setRemoved(true);
        tower.getOwner().removeTower(tower);
        tower.getPosition().setTower(null);
        gamestate.removeTower(tower);
        tower.setRemoved(true);
        tower.notifyObserver();
    }


    /**
     * Rüstet einen Turm auf
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    @Override
    public void upgradeTower(int xCoordinate, int yCoordinate, int playerNumber) {

        Coordinates mapCell = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);
        Player owningPlayer = gamestate.getPlayerByNumber(playerNumber);
        Tower tower = mapCell.getTower();

        if (owningPlayer != tower.getOwner()) {
            displayErrorMessage("Du darfst nur eigene Türme aufrüsten!", playerNumber);
        } else if (owningPlayer.getResources() < tower.getUpgradePrice()) {
            displayErrorMessage("Du hast nicht genug Geld, um diesen Turm aufzurüsten!", playerNumber);
        } else {
            owningPlayer.setResources(owningPlayer.getResources() - tower.getUpgradePrice());
            TowerUpgrader.upgradeTower(tower);
            owningPlayer.notifyObserver();
            tower.notifyObserver();
            if (server) {
                gameServer.upgradeTower(xCoordinate, yCoordinate, playerNumber);
            }
        }
    }

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType            Der Typ des zu schickenden Gegners
     * @param playerToSendToNumber Die Nummer der Spielerin, an die der Gegner geschickt werden soll
     * @param sendingPlayerNumber  Die Nummer des Spielers, der den Gegner sendet
     */
    @Override
    public void sendEnemy(int enemyType, int playerToSendToNumber, int sendingPlayerNumber) {

        Enemy enemy = createNewEnemy(enemyType);
        Player sendingPlayer = gamestate.getPlayerByNumber(sendingPlayerNumber);
        if (sendingPlayer.getResources() >= enemy.getSendPrice()) {
            Player playerToSendTo = gamestate.getPlayerByNumber(playerToSendToNumber);
            if (playerToSendTo.getWaves().size() > gamestate.getRoundNumber() + 1) {
                playerToSendTo.getWaves().get(gamestate.getRoundNumber() + 1).addEnemy(enemy);
                sendingPlayer.setResources(sendingPlayer.getResources() - enemy.getSendPrice());
                sendingPlayer.notifyObserver();
                System.out.println("Enemy added!");
                if (server) {
                    gameServer.sendEnemy(enemyType, playerToSendToNumber, sendingPlayerNumber);
                }
            }
        } else {
            displayErrorMessage("Nicht genug Geld vorhanden, um diesen Gegner zu senden!", sendingPlayerNumber);
        }
    }

    /**
     * Gibt den Spielzustand zurück
     *
     * @return Der aktuelle Spielzustand
     */
    public Gamestate getGamestate() {
        return gamestate;
    }

    /**
     * Legt den Spielzustand fest
     *
     * @param gamestate Der festzulegende Spielzustand
     */
    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    @Override
    public void displayErrorMessage(String errorMessage, int playerNumber) {
        if (playerNumber == localPlayerNumber) {
            gameScreen.displayErrorMessage(errorMessage);
        } else {
            System.out.println(playerNumber);
            System.out.println(localPlayerNumber);
            gameServer.sendErrorMessage(errorMessage, playerNumber);
        }
    }

    public GameView getGameScreen() {
        return gameScreen;
    }

    /**
     * Beendet das Spiel
     *
     * @param saveBeforeExit Gibt an, ob das Spiel vorher gespeichert werden soll
     */
    public void exitGame(boolean saveBeforeExit) {
        gameScreen.dispose();
        if (saveBeforeExit) {
            SaveState saveState = new SaveState(new Date(), multiplayer, profile, gamestate, localPlayerNumber, mapPath);
            //saveStateDao.create(saveState);
        }
        //mainController.setEndScreen(gamestate);
        mainController.showMenuScreen();
    }

    @Override
    public Player getLocalPlayer() {
        return gamestate.getPlayers().get(localPlayerNumber);
    }

    public int getLocalPlayerNumber() {
        return localPlayerNumber;
    }

    void setLocalPlayerNumber(int localPlayerNumber) {
        this.localPlayerNumber = localPlayerNumber;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }
}
