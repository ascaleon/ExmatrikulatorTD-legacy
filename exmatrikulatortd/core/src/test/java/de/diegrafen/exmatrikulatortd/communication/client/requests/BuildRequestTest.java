package de.diegrafen.exmatrikulatortd.communication.client.requests;

import org.junit.Before;
import org.junit.Test;

import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.REGULAR_TOWER;
import static org.junit.Assert.*;

/**
 * @author janro
 * @version 10.07.2019 19:47
 */
public class BuildRequestTest {

    private BuildRequest buildRequest;

    @Before
    public void setUp() throws Exception {
        buildRequest = new BuildRequest(REGULAR_TOWER, 0, 0, 0);
    }

    @Test
    public void getTowerType() {
        assertEquals(REGULAR_TOWER, buildRequest.getTowerType());
    }

    @Test
    public void getxCoordinate() {
        assertEquals(REGULAR_TOWER, buildRequest.getxCoordinate());
    }

    @Test
    public void getyCoordinate() {
        assertEquals(REGULAR_TOWER, buildRequest.getyCoordinate());
    }

    @Test
    public void getPlayerNumber() {
        assertEquals(REGULAR_TOWER, buildRequest.getPlayerNumber());
    }
}