package de.diegrafen.exmatrikulatortd.communication.client.requests;

import org.junit.Before;
import org.junit.Test;

import static de.diegrafen.exmatrikulatortd.util.Constants.MEDIUM;
import static org.junit.Assert.*;

/**
 * @author janro
 * @version 10.07.2019 19:51
 */
public class GetGameInfoRequestTest {

    private GetGameInfoRequest getGameInfoRequest;

    @Before
    public void setUp() throws Exception {
        getGameInfoRequest = new GetGameInfoRequest("Player 1", MEDIUM, "profilePictures/player1.jpg");
    }

    @Test
    public void getPlayerName() {
        assertEquals("Player 1", getGameInfoRequest.getPlayerName());
    }

    @Test
    public void getDifficulty() {
        assertEquals(MEDIUM, getGameInfoRequest.getDifficulty());
    }

    @Test
    public void getProfilePicturePath() {
        assertEquals("profilePictures/player1.jpg", getGameInfoRequest.getProfilePicturePath());
    }
}