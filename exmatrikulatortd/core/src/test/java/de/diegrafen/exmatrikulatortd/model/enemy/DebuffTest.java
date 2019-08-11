package de.diegrafen.exmatrikulatortd.model.enemy;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DebuffTest {

    private Debuff debuffToTest;

    @Before
    public void setUp() throws Exception {
        debuffToTest = new Debuff();
    }

    @Test
    public void setSpeedMultiplier() {
        debuffToTest.setSpeedMultiplier(-12123123);
        assertEquals(debuffToTest.getSpeedMultiplier(), 0, 0);
        debuffToTest.setSpeedMultiplier(5);
        assertEquals(debuffToTest.getSpeedMultiplier(), 5, 0);
    }
}