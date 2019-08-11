package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class GameLogicUnitTest {

    private LogicController logicController;

    private GameLogicUnit gameLogicUnit;

    private Gamestate gamestate;

    @Before
    public void setUp() throws Exception {
        logicController = mock(GameLogicController.class);
        gamestate = mock(Gamestate.class);
        logicController.setGamestate(gamestate);
        gameLogicUnit = new GameLogicUnit(logicController);
    }

    @Test
    public void spawnWave() {

    }

    @Test
    public void applyAuras() {
    }

    @Test
    public void applyMovement() {
    }

    @Test
    public void makeAttacks() {
    }

    @Test
    public void applyBuffsToTowers() {
    }

    @Test
    public void applyDebuffsToEnemies() {
    }

    @Test
    public void applyAttackDelay() {
    }

    @Test
    public void moveProjectiles() {
    }
}