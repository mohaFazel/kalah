package com.bol.assignment.kalahgame.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.bol.assignment.kalahgame.enums.State.PLAYER1_TURN;
import static com.bol.assignment.kalahgame.model.Board.PLAYER_1;
import static com.bol.assignment.kalahgame.model.Board.PLAYER_2;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardTest {

    @Test
    public void getPitByIdTest() {
        Board board = new Board();
        assertEquals(board.getPitById(3).getPlayer_id().intValue(), PLAYER_1);
        assertEquals(board.getPitById(7).getNumberOfStones().intValue(), Board.INITIAL_STONE_HOME);
    }

    @Test
    public void getOtherPlayersPitTest() {
        Board board = new Board();
        Pit currentPit = board.getPitById(2);
        assertEquals(board.getOtherPlayersPit(currentPit).getPlayer_id().intValue(), PLAYER_2);
    }

    @Test
    public void getPlayersHomeTest1() {
        Board board = new Board();
        Pit currentPit = board.getPitById(2);
        assertEquals(board.getPlayersHome(currentPit).getPitId().intValue(), 7);
    }

    @Test
    public void getPlayersHomeTest2() {
        Board board = new Board();
        Pit currentPit = board.getPitById(13);
        assertEquals(board.getPlayersHome(currentPit).getPitId().intValue(), 14);
    }

    @Test
    public void getPlayerStoneCountTest() {
        Board board = new Board();
        assertEquals(board.getPlayerStoneCount(PLAYER_1).intValue(), 36);
    }

    @Test
    public void checkValidMoveTest() {
        Kalah kalah = new Kalah();
        kalah.getBoard().isValidMove(1);
        assertEquals(kalah.getBoard().getState().name(), PLAYER1_TURN.name());
    }

    @Test
    public void checkValidMoveTestWhenInvalidPit() {
        Kalah kalah = new Kalah();
        MoveResponse response = kalah.getBoard().isValidMove(39);
        assertTrue(response.isError());
        assertEquals(response.getErrorDescription(), "Invalid pit Id");
    }

    @Test
    public void checkValidMoveTestWhenSelectedPitIsPlayerHome() {
        Kalah kalah = new Kalah();
        MoveResponse response = kalah.getBoard().isValidMove(7);
        assertTrue(response.isError());
        assertEquals(response.getErrorDescription(), "Pit Id cannot be a player home");
    }

    @Test
    public void checkValidMoveTestWhenWrongPit() {
        Kalah kalah = new Kalah();
        MoveResponse response = kalah.getBoard().isValidMove(13);
        assertTrue(response.isError());
        assertEquals(response.getErrorDescription(), "Wrong Pit!");
    }

    @Test
    public void checkValidMoveTestWhenEmptyPit() {
        Kalah kalah = new Kalah();
        setEmpty(kalah.getBoard().getPitById(5));
        MoveResponse response = kalah.getBoard().isValidMove(5);
        assertTrue(response.isError());
        assertEquals(response.getErrorDescription(), "Empty Pit!");
    }

    @Test
    public void updateKalahTest() {
        //TODO
    }

    private void setEmpty(Pit pit) {
        pit.setNumberOfStones(0);
    }
}
