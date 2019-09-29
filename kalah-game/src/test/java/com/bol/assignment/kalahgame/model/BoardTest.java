package com.bol.assignment.kalahgame.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static com.bol.assignment.kalahgame.model.Board.*;
import static com.bol.assignment.kalahgame.model.State.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardTest {

    @Test
    public void initializePitsTest() {
        Board board = new Board();
        Map<Integer, Pit> pits = board.initializePits();
        assertEquals(pits.get(PLAYER1_HOME).getNumberOfStones().intValue(), INITIAL_STONE_HOME);
        assertEquals(pits.get(PLAYER2_HOME).getNumberOfStones().intValue(), INITIAL_STONE_HOME);
        assertEquals(board.getState(), PLAYER1_TURN);
    }

    @Test
    public void getPitByIdTest() {
        Board board = new Board();
        assertEquals(board.getPitById(3).getPlayerId().intValue(), PLAYER_1);
        assertEquals(board.getPitById(7).getNumberOfStones().intValue(), INITIAL_STONE_HOME);
    }

    @Test
    public void getOtherPlayersPitTest() {
        Board board = new Board();
        Pit currentPit = board.getPitById(2);
        assertEquals(board.getOtherPlayersPit(currentPit).getPlayerId().intValue(), PLAYER_2);
    }

    @Test
    public void getPlayersHomeTestWhenPlayer1() {
        Board board = new Board();
        Pit currentPit = board.getPitById(2);
        assertEquals(board.getPlayersHome(currentPit).getPitId().intValue(), 7);
    }

    @Test
    public void getPlayersHomeTestWhenPlayer2() {
        Board board = new Board();
        Pit currentPit = board.getPitById(13);
        assertEquals(board.getPlayersHome(currentPit).getPitId().intValue(), 14);
    }

    @Test
    public void getPlayerStoneCountTestWhenPLayer1() {
        Board board = new Board();
        assertEquals(board.getPlayerStoneCount(PLAYER_1).intValue(), 36);
    }

    @Test
    public void getPlayerStoneCountTestWhenPLayer2() {
        Board board = new Board();
        assertEquals(board.getPlayerStoneCount(PLAYER_2).intValue(), 36);
    }

    @Test
    public void checkValidMoveTest() {
        Board board = new Board();
        MoveResponse response = board.isValidMove(1);
        assertFalse(response.isError());
        assertEquals(board.getState().name(), PLAYER1_TURN.name());
    }

    @Test
    public void checkValidMoveTestWhenInvalidPit() {
        Board board = new Board();
        MoveResponse response = board.isValidMove(39);
        assertTrue(response.isError());
        assertEquals(response.getErrorDescription(), "Invalid pit Id");
    }

    @Test
    public void checkValidMoveTestWhenSelectedPitIsPlayerHome() {
        Board board = new Board();
        MoveResponse response = board.isValidMove(7);
        assertTrue(response.isError());
        assertEquals(response.getErrorDescription(), "Pit Id cannot be a player home");
    }

    @Test
    public void checkValidMoveTestWhenWrongPit() {
        Board board = new Board();
        MoveResponse response = board.isValidMove(13);
        assertTrue(response.isError());
        assertEquals(response.getErrorDescription(), "Wrong Pit!");
    }

    @Test
    public void checkValidMoveTestWhenEmptyPit() {
        Board board = new Board();
        setEmpty(board.getPitById(5));
        MoveResponse response = board.isValidMove(5);
        assertTrue(response.isError());
        assertEquals(response.getErrorDescription(), "Empty Pit!");
    }

    @Test
    public void updateKalahTest() {
        Board board = new Board();
        board.updateKalah(1);
        assertEquals(board.getPitById(1).getNumberOfStones().intValue(), 0);
        assertEquals(board.getState().name(), PLAYER1_TURN.name());
    }

    @Test
    public void updateKalahTestWhenBonus() {
        Board board = new Board();
        board.getPitById(1).setNumberOfStones(1);
        setEmpty(board.getPitById(2));
        int otherPlayerStoneCount = board.getOtherPlayersPit(board.getPitById(2)).getNumberOfStones();
        board.updateKalah(1);
        assertEquals(board.getPitById(2).getNumberOfStones().intValue(), 0);
        assertEquals(board
                .getOtherPlayersPit(board.getPitById(2)).getNumberOfStones().intValue(), 0);
        assertEquals(board.getPlayersHome(board.getPitById(PLAYER_1))
                .getNumberOfStones().intValue(), otherPlayerStoneCount + 1);
    }
    
    @Test
    public void moveStonesTest() {
        Board board = new Board();
        board.moveStones(board.getPitById(1));
        assertEquals(board.getPitById(1).getNumberOfStones().intValue(), 0);
        assertEquals(board.getPlayersHome(board.getPitById(PLAYER_1)).getNumberOfStones().intValue(), 1);
        assertEquals(board.getState(), PLAYER1_TURN);
    }

    @Test
    public void checkBonusTestWhenBonus() {
        Board board = new Board();
        board.getPitById(1).setNumberOfStones(1);
        board.getPitById(2).setNumberOfStones(0);
        Pit lastPit = board.moveStones(board.getPitById(1));
        assertTrue(board.checkBonus(lastPit));
    }

    @Test
    public void checkBonusTestWhenNotBonus() {
        Board board = new Board();
        Pit lastPit = board.moveStones(board.getPitById(1));
        assertFalse(board.checkBonus(lastPit));
    }

    @Test
    public void addRewardsTest() {
        Board board = new Board();
        board.getPitById(1).setNumberOfStones(1);
        setEmpty(board.getPitById(2));
        Pit lastPit = board.moveStones(board.getPitById(1));
        int pitStoneCount = lastPit.getNumberOfStones();
        int otherPlayerPitStoneCount = board.getOtherPlayersPit(lastPit).getNumberOfStones();
        board.addRewards(lastPit);
        assertEquals(lastPit.getNumberOfStones().intValue(), 0);
        assertEquals(board.getOtherPlayersPit(lastPit).getNumberOfStones().intValue(), 0);
        assertEquals(board.getPlayersHome(lastPit).getNumberOfStones().intValue(),
                pitStoneCount + otherPlayerPitStoneCount);
    }

    @Test
    public void addRewardsTestWhenOtherPlayerPitIsEmpty() {
        Board board = new Board();
        board.getPitById(1).setNumberOfStones(1);
        setEmpty(board.getPitById(2));
        setEmpty(board.getOtherPlayersPit(board.getPitById(2)));
        Pit lastPit = board.moveStones(board.getPitById(1));
        board.addRewards(lastPit);
        assertEquals(lastPit.getNumberOfStones().intValue(), 1);
    }

    @Test
    public void updateStateTest() {
        Board board = new Board();
        board.setState(PLAYER1_TURN);
        Pit lastPit = board.getPlayersHome(board.getPitById(8));
        board.updateState(lastPit);
        assertEquals(board.getState(), PLAYER2_TURN);
    }

    @Test
    public void updateStateTestWhenFinished() {
        Board board = new Board();
        for (int i = Board.PIT_START_INDEX; i < PLAYER1_HOME; i++) {
            setEmpty(board.getPitById(i));
        }
        board.getPlayersHome(board.getPitById(1)).setNumberOfStones(17);
        Pit lastPit = board.getPitById(9);
        board.updateState(lastPit);
        assertEquals(board.getState(), FINISHED);
        assertEquals(board.getWinner(), "PLAYER 2");
    }

    @Test
    public void updateStateTestWhenDraw() {
        Board board = new Board();
        board.getPlayersHome(board.getPitById(1)).setNumberOfStones(17);
        board.getPlayersHome(board.getPitById(8)).setNumberOfStones(16);
        for (int i = Board.PIT_START_INDEX; i < PLAYER1_HOME; i++) {
            setEmpty(board.getPitById(i));
        }
        for (int i = PLAYER1_HOME + 1; i < PLAYER2_HOME; i++) {
            setEmpty(board.getPitById(i));
        }
        Pit lastPit = board.getPitById(9);
        lastPit.setNumberOfStones(1);
        board.updateState(lastPit);
        assertEquals(board.getState(), FINISHED);
        assertEquals(board.getWinner(), "DRAW");
    }

    @Test
    public void updateStateTestWhenPlayer1GetBonusMove() {
        Board board = new Board();
        Pit lastPit = board.getPlayersHome(board.getPitById(1));
        board.updateState(lastPit);
        assertEquals(board.getState(), PLAYER1_TURN);
    }

    @Test
    public void updateStateTestWhenPlayer2GetBonusMove() {
        Board board = new Board();
        board.setState(PLAYER2_TURN);
        Pit lastPit = board.getPlayersHome(board.getPitById(8));
        board.updateState(lastPit);
        assertEquals(board.getState(), PLAYER2_TURN);
    }

    private void setEmpty(Pit pit) {
        pit.setNumberOfStones(0);
    }
}
