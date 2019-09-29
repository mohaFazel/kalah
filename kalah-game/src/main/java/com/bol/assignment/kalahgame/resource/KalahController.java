package com.bol.assignment.kalahgame.resource;

import com.bol.assignment.kalahgame.model.MoveResponse;
import com.bol.assignment.kalahgame.model.view.KalahView;
import com.bol.assignment.kalahgame.service.KalahService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This controller identifies the resources to play Kalah Game.
 * In this controller, we have two different endpoints:
 * First endpoint is to initialize the board to start the game
 * Second endpoint specifies the pit Id that selected by the player to move its stone in the game which is specified by the game Id
 * @author mohammad Fazel
 */

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Api(value = "REST API for Kalah game", tags = {"kalah"})
@RequestMapping("/games")
public class KalahController {

    @Autowired
    private KalahService kalahSrv;

    @PostMapping
    @ApiOperation(value = "The endpoint to start the game")
    public ResponseEntity start() {
        log.debug("Initialize a new game");
        KalahView view = new KalahView(kalahSrv.initialize());
        return ResponseEntity.status(HttpStatus.CREATED).body(view);
    }

    @PutMapping("/{gameId}/pits/{pitId}")
    @ApiOperation(value = "The endpoint to identify the pit Id that player wants to move its stones in the game which specified by game Id")
    public ResponseEntity move(@PathVariable @ApiParam(required = true, example = "b88c9fa1-5638-459d-a1c8-c6e22fd54a0d") String gameId,
                               @PathVariable @ApiParam(required = true, example = "1") int pitId) {
        log.debug("Move stones from pit number {} in game {}", pitId, gameId);
        MoveResponse response = kalahSrv.move(gameId, pitId);
        if (response.isError()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorDescription());
        }
        KalahView view = new KalahView(response.getKalah());
        return ResponseEntity.ok(view);
    }
}
