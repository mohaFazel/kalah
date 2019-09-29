package com.bol.assignment.kalahgame.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

import static com.bol.assignment.kalahgame.model.State.*;

@Slf4j
@Getter
public class Board {

    static final int PLAYER_1 = 1;
    static final int PLAYER_2 = 2;
    static final int INITIAL_STONE_PIT = 6;
    public static final int INITIAL_STONE_HOME = 0;
    private static final int PIT_START_INDEX = 1;
    private static final int PIT_END_INDEX = 14;
    public static final int PLAYER1_HOME = 7;
    static final int PLAYER2_HOME = 14;

    private State state;
    private Map<Integer, Pit> pits;
    private String winner;

    public Board() {
        this.state = PLAYER1_TURN;
        this.pits = initializePits();
    }

    private Map<Integer, Pit> initializePits() {
        Map <Integer, Pit> initPits = new HashMap<>();
        for (int i = PIT_START_INDEX; i < PLAYER1_HOME; i++) {
            Pit pit = new Pit(i, INITIAL_STONE_PIT, PLAYER_1);
            initPits.put(i, pit);
        }
        Pit player1Home = new Pit(PLAYER1_HOME, INITIAL_STONE_HOME, PLAYER_1);
        initPits.put(PLAYER1_HOME, player1Home);
        for (int i = PLAYER1_HOME + 1; i < PLAYER2_HOME; i++) {
            Pit pit = new Pit(i, INITIAL_STONE_PIT, PLAYER_2);
            initPits.put(i, pit);
        }
        Pit player2Home = new Pit(PLAYER2_HOME, INITIAL_STONE_HOME, PLAYER_2);
        initPits.put(PLAYER2_HOME, player2Home);
        return initPits;
    }

    Pit getOtherPlayersPit(Pit pit) {
        Integer otherPlayersPitId = (PIT_START_INDEX + PIT_END_INDEX - 1) - pit.getPitId();
        return pits.get(otherPlayersPitId);
    }

    Pit getPlayersHome(Pit pit) {
        int playersHomeId;
        if (pit.getPlayer_id().equals(PLAYER_1)) {
            playersHomeId = PLAYER1_HOME;
        } else {
            playersHomeId = PLAYER2_HOME;
        }
        return pits.get(playersHomeId);
    }

    Integer getPlayerStoneCount(int player) {
        Integer result = 0;
        if (player == PLAYER_1) {
            for (int i = PIT_START_INDEX; i < PLAYER1_HOME; i++) {
                result += pits.get(i).getNumberOfStones();
            }
        } else if (player == PLAYER_2) {
            for (int i = PLAYER1_HOME + 1; i < PLAYER2_HOME; i++) {
                result += pits.get(i).getNumberOfStones();
            }
        }
        return result;
    }

    public MoveResponse isValidMove(int pitId) {
        MoveResponse response = new MoveResponse(false);
        if(pitId < Board.PIT_START_INDEX || pitId > Board.PIT_END_INDEX) {
            response = new MoveResponse(true, "Invalid pit Id");
        } else if (pitId == Board.PLAYER1_HOME || pitId == Board.PLAYER2_HOME) {
            response = new MoveResponse(true, "Pit Id cannot be a player home");
        } else if ((this.state.getValue() == PLAYER_1 && pitId >= PLAYER1_HOME) ||
                (this.state.getValue() == PLAYER_2 && pitId <= PLAYER1_HOME)) {
            response = new MoveResponse(true, "Wrong Pit!");
        } else if (pits.get(pitId).getNumberOfStones() == 0) {
            response = new MoveResponse(true, "Empty Pit!");
        }
        return response;
    }

    public void updateKalah(int pitId) {
        Pit selectedPit = this.pits.get(pitId);

        Pit lastPit = moveStones(selectedPit);
        if (checkBonus(lastPit)) {
            addRewards(lastPit);
        }
        updateState(lastPit);
    }

    private Pit moveStones(Pit pit) {
        Integer stonesToPlay = pit.getNumberOfStones();
        pit.setNumberOfStones(0);
        while (stonesToPlay > 0) {
            pit = this.pits.get(pit.getNextPitId());
            if (!pit.isOpponentHome(this.state)) {
                pit.setNumberOfStones(pit.getNumberOfStones() + 1);
                stonesToPlay -= 1;
            }
        }
        return pit;
    }

    private boolean checkBonus(Pit pit) {
        return pit.isNotHome() && pit.isForCurrentPlayer(this.state) && pit.getNumberOfStones() == 1;
    }

    private void addRewards(Pit pit) {
        Pit otherPlayerPit = getOtherPlayersPit(pit);
        if (otherPlayerPit.getNumberOfStones() > 0) {
            Pit playersHome = getPlayersHome(pit);
            playersHome.setNumberOfStones(playersHome.getNumberOfStones() +
                    otherPlayerPit.getNumberOfStones() +
                    pit.getNumberOfStones()
            );
            pit.setNumberOfStones(0);
            otherPlayerPit.setNumberOfStones(0);
        }
    }

    private void updateState(Pit lastPit) {
        Integer player1StoneCount = getPlayerStoneCount(PLAYER_1);
        Integer player2StoneCount = getPlayerStoneCount(PLAYER_2);
        if (player1StoneCount == 0 || player2StoneCount == 0) {
            int player1Score = pits.get(PLAYER1_HOME).getNumberOfStones() + player1StoneCount;
            int player2Score = pits.get(PLAYER2_HOME).getNumberOfStones() + player1StoneCount;
            this.state = FINISHED;
            if(player1Score > player2Score) {
                this.winner = "PLAYER 1";
            } else if (player1Score < player2Score) {
                this.winner = "PLAYER 2";
            } else {
                this.winner = "DRAW";
            }
            for (int i = PIT_START_INDEX; i < PLAYER1_HOME; i++) {
                pits.get(i).setNumberOfStones(0);
            }
            for (int i = PLAYER1_HOME + 1; i < PLAYER2_HOME; i++) {
                pits.get(i).setNumberOfStones(0);
            }
            pits.get(PLAYER1_HOME).setNumberOfStones(player1Score);
            pits.get(PLAYER2_HOME).setNumberOfStones(player2Score);
        } else if (lastPit.isPlayerHome(PLAYER_1) && this.state.name().equals(PLAYER1_TURN.name())) {
            this.state = PLAYER1_TURN;
        } else if (lastPit.isPlayerHome(PLAYER_2) && this.state.name().equals(PLAYER2_TURN.name())) {
            this.state = PLAYER2_TURN;
        } else {
            this.state = this.state.name().equals(PLAYER1_TURN.name()) ? PLAYER2_TURN : PLAYER1_TURN;
        }
    }

    public Pit getPitById(int pitId) {
        return this.pits.get(pitId);
    }

    /**
     * Method is used to print the board. Just to debug the game.
     */
    public void printBoard() {
        int activePlayer;
        int inactivePlayer;
        if (this.state.equals(PLAYER1_TURN)) {
            activePlayer = 1;
            inactivePlayer = 2;
        } else {
            activePlayer = 2;
            inactivePlayer =1;
        }
        log.info("######################################################################");
        log.info("\t  P" + inactivePlayer + "|");
        log.info("\t\t---------------------------------------------------------");
        StringBuilder player1Pits = new StringBuilder("\t\t\t");
        StringBuilder homes;
        StringBuilder player2Pits = new StringBuilder("\t\t\t");
        if (activePlayer == 1) {
            for (int i = PLAYER2_HOME - 1; i > PLAYER1_HOME; i--) {
                player2Pits.append(pits.get(i).getNumberOfStones().toString()).append("\t");
            }
            log.info(player2Pits.toString());
            homes = new StringBuilder("\t\t" + pits.get(14).getNumberOfStones() + "\t" + " " + "\t" + " " + "\t" + " " + "\t" + " " + "\t\t\t" + pits.get(7).getNumberOfStones());
            log.info(homes.toString());
            for (int i = PIT_START_INDEX; i < PLAYER1_HOME; i++) {
                player1Pits.append(pits.get(i).getNumberOfStones().toString()).append("\t");
            }
            log.info(player1Pits.toString());
            log.info("\t\t---------------------------------------------------------");
            log.info("\t\tPits:  \t1\t2\t3\t4\t5\t6\t|P" + activePlayer);
        } else {
            for (int i = PLAYER1_HOME - 1; i >= PIT_START_INDEX; i--) {
                player1Pits.append(pits.get(i).getNumberOfStones().toString()).append("\t");
            }
            log.info(player1Pits.toString());
            homes = new StringBuilder("\t\t" + pits.get(7).getNumberOfStones() + "\t" + " " + "\t" + " " + "\t" + " " + "\t" + " " + "\t\t\t" + pits.get(14).getNumberOfStones());
            log.info(homes.toString());
            for (int i = PLAYER1_HOME + 1; i < PLAYER2_HOME; i++) {
                player2Pits.append(pits.get(i).getNumberOfStones().toString()).append("\t");
            }
            log.info(player2Pits.toString());
            log.info("\t\t---------------------------------------------------------");
            log.info("\t\tPits:  \t8\t9\t10\t11\t12\t13\t|P" + activePlayer);
        }
    }
}
