package com.bol.assignment.kalahgame.service;

import com.bol.assignment.kalahgame.model.Board;
import com.bol.assignment.kalahgame.model.Kalah;
import com.bol.assignment.kalahgame.model.MoveResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.bol.assignment.kalahgame.enums.State.*;

@Service
public class KalahServiceImpl implements KalahService {

    private Map<String, Kalah> cache = new HashMap<>();

    @Override
    public Kalah initialize() {
        Kalah kalah = new Kalah();
        cache.put(kalah.getId(), kalah);
        kalah.getBoard().printBoard();
        return kalah;
    }

    @Override
    public MoveResponse move(String gameId, int pitId) {
        Kalah kalah = cache.get(gameId);
        if (kalah == null) {
            return new MoveResponse(true, "There is no Kalah game with the id: " + gameId, kalah);
        }

        Board board = kalah.getBoard();

        MoveResponse response = board.isValidMove(pitId);
        if (response.isError()) {
            response.setKalah(kalah);
            return response;
        }

        board.updateKalah(pitId);

        board.printBoard();
        if (board.getState().equals(FINISHED)) {
            cache.remove(kalah.getId());
        }

        response.setKalah(kalah);
        return response;
    }
}
