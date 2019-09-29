package com.bol.assignment.kalahgame.service;

import com.bol.assignment.kalahgame.model.Board;
import com.bol.assignment.kalahgame.model.Kalah;
import com.bol.assignment.kalahgame.model.MoveResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.bol.assignment.kalahgame.enums.State.PLAYER1_TURN;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KalahServiceImplTest {

    @Autowired
    KalahServiceImpl kalahSrv;

    @Test
    public void initializetest() {
        Kalah kalah = kalahSrv.initialize();
        assertEquals(kalah.getBoard().getPitById(7).getNumberOfStones().intValue(), Board.INITIAL_STONE_HOME);
        assertEquals(kalah.getBoard().getState().name(), PLAYER1_TURN.name());
    }

    @Test
    public void moveTest() {
        Kalah init = kalahSrv.initialize();
        MoveResponse result = kalahSrv.move(init.getId(), 1);
        assertEquals(result.getKalah().getBoard().getPitById(Board.PLAYER1_HOME).getNumberOfStones().intValue(), 1);
        assertEquals(result.getKalah().getBoard().getPitById(1).getNumberOfStones().intValue(), 0);
        assertEquals(result.getKalah().getBoard().getState().name(), PLAYER1_TURN.name());
    }
}
