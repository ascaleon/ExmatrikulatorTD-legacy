package de.diegrafen.exmatrikulatortd.communication.client.requests;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author janro
 * @version 10.07.2019 19:47
 */
public class BuildRequestTest {

    private BuildRequest buildRequest;

    @Before
    public void setUp() throws Exception {
        buildRequest = new BuildRequest(0, 0, 0, 0);
    }

    @Test
    public void getTowerType() {
        assertEquals(0, buildRequest.getTowerType());
    }

    @Test
    public void getxCoordinate() {
        assertEquals(0, buildRequest.getxCoordinate());
    }

    @Test
    public void getyCoordinate() {
        assertEquals(0, buildRequest.getyCoordinate());
    }

    @Test
    public void getPlayerNumber() {
        assertEquals(0, buildRequest.getPlayerNumber());
    }
}