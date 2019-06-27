package de.diegrafen.exmatrikulatortd.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProfileTest {

    private Profile profile;

    @Before
    public void setUp() throws Exception {
        profile = new Profile();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getHighscores() {
        assertNull(profile.getHighscores());
    }
}