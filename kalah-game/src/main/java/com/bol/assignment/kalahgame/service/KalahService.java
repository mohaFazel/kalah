package com.bol.assignment.kalahgame.service;

import com.bol.assignment.kalahgame.model.Kalah;
import com.bol.assignment.kalahgame.model.MoveResponse;

public interface KalahService {

    Kalah initialize();

    MoveResponse move(String gameId, int pitId);
}
