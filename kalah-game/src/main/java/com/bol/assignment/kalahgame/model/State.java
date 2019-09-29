package com.bol.assignment.kalahgame.model;

import lombok.Getter;

/**
 *  Identifies the state of the Kalah game
 */
@Getter
public enum State {
    PLAYER1_TURN(1),
    PLAYER2_TURN(2),
    FINISHED(3);

    private final int value;

    State(int value) {
        this.value = value;
    }
}
