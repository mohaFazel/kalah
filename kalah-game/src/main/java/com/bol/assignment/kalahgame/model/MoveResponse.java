package com.bol.assignment.kalahgame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MoveResponse {

    private boolean isError;
    private String errorDescription;
    private Kalah kalah;

    MoveResponse(boolean isError) {
        this.isError = isError;
    }

    public MoveResponse(boolean isError, String errorDescription) {
        this.isError = isError;
        this.errorDescription = errorDescription;
    }
}
