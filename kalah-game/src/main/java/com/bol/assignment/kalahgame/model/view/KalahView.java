package com.bol.assignment.kalahgame.model.view;

import com.bol.assignment.kalahgame.model.Kalah;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class KalahView {

    private String id;
    private String url;
    private String state;
    private String winner;
    private Map<Integer, Integer> status;

    public KalahView(Kalah kalah) {
        this.id = kalah.getId();
        this.url = "https://localhost:8080/games/" + this.id;
        this.state = kalah.getBoard().getState().name();
        this.winner = kalah.getBoard().getWinner();
        this.status = kalah.getBoard().getPits().entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(),
                e -> e.getValue().getNumberOfStones()
        ));
    }
}
