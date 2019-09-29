package com.bol.assignment.kalahgame.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.bol.assignment.kalahgame.enums.State.PLAYER1_TURN;
import static com.bol.assignment.kalahgame.enums.State.PLAYER2_TURN;
import static com.bol.assignment.kalahgame.model.Board.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PitTest {

    @Test
    public void getNextPitIdTest() {
        Pit pit = new Pit(3, INITIAL_STONE_PIT, PLAYER_1);
        assertEquals(pit.getNextPitId().intValue(), 4);
    }

    @Test
    public void isOpponentHomeTest1() {
        Pit pit = new Pit(7, INITIAL_STONE_HOME, PLAYER_1);
        assertTrue(pit.isOpponentHome(PLAYER2_TURN));
    }

    @Test
    public void isOpponentHomeTest2() {
        Pit pit = new Pit(11, INITIAL_STONE_PIT, PLAYER_2);
        assertFalse(pit.isOpponentHome(PLAYER2_TURN));
    }

    @Test
    public void isNotHomeTest1() {
        Pit pit = new Pit(3, INITIAL_STONE_PIT, PLAYER_1);
        assertTrue(pit.isNotHome());
    }

    @Test
    public void isNotHomeTest2() {
        Pit pit = new Pit(14, INITIAL_STONE_HOME, PLAYER_2);
        assertFalse(pit.isNotHome());
    }

    @Test
    public void isForCurrentPlayerTest1() {
        Pit pit = new Pit(2, INITIAL_STONE_PIT, PLAYER_1);
        assertTrue(pit.isForCurrentPlayer(PLAYER1_TURN));
    }

    @Test
    public void isForCurrentPlayerTest2() {
        Pit pit = new Pit(5, INITIAL_STONE_PIT, PLAYER_1);
        assertFalse(pit.isForCurrentPlayer(PLAYER2_TURN));
    }

    @Test
    public void isPlayerHomeTest1() {
        Pit pit = new Pit(7, INITIAL_STONE_HOME, PLAYER_2);
        assertTrue(pit.isPlayerHome(PLAYER_1));
    }

    @Test
    public void isPlayerHomeTest2() {
        Pit pit = new Pit(7, INITIAL_STONE_HOME, PLAYER_2);
        assertFalse(pit.isPlayerHome(PLAYER_2));
    }

}
