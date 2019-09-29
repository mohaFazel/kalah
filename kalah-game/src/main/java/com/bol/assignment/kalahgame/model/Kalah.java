package com.bol.assignment.kalahgame.model;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Kalah {

    private String id;
    private Board board;

    public Kalah() {
        this.id = UUID.randomUUID().toString();
        this.board = new Board();
    }
}
