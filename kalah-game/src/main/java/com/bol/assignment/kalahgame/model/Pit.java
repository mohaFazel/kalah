package com.bol.assignment.kalahgame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.bol.assignment.kalahgame.model.State.PLAYER1_TURN;
import static com.bol.assignment.kalahgame.model.State.PLAYER2_TURN;
import static com.bol.assignment.kalahgame.model.Board.*;

@Getter
@Setter
@AllArgsConstructor
public class Pit {

    private Integer pitId;

    private Integer numberOfStones;

    private Integer playerId;

    /**
     * identifies the id of next pit in the board
     */
    Integer getNextPitId() {
        int nextPitId = this.pitId + 1;
        return nextPitId <= PLAYER2_HOME ? nextPitId : 1;
    }

    /**
     * identifies if the current pit is the other player's home or not
     */
    boolean isOpponentHome(State state) {
        return (state.name().equals(PLAYER1_TURN.name()) && this.pitId == PLAYER2_HOME) ||
                (state.name().equals(PLAYER2_TURN.name()) && this.pitId == PLAYER1_HOME);
    }

    /**
     * identifies if current pit is any player's home or not
     */
    boolean isNotHome() {
        return !this.pitId.equals(PLAYER1_HOME) && !this.pitId.equals(PLAYER2_HOME);
    }

    /**
     * identifies if current pit is one of the current player's pit or not
     */
    boolean isForCurrentPlayer(State state) {
        if (state.name().equals(PLAYER1_TURN.name()) && this.getPlayerId().equals(state.getValue())) {
            return true;
        }
        if (state.name().equals(PLAYER2_TURN.name()) && this.getPlayerId().equals(state.getValue())) {
            return true;
        }
        return false;
    }

    boolean isPlayerHome(int player) {
        return (player == PLAYER_1 && this.pitId == PLAYER1_HOME) ||
                (player == PLAYER_2 && this.pitId == PLAYER2_HOME);
    }
}
