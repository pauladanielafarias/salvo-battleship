package com.codeoftheweb.salvo.models;

public enum GameState {
    UNDEFINED,
    PLACE_SHIPS,
    WAITING_FOR_OPPONENT,
    WAITING_FOR_OPPPONENT_SHIPS,
    SHOOT_SALVOES,
    SHOOT_AGAIN,
    WAITING_FOR_OPPONENT_SHOOT_SALVOES,
    WON,
    LOST,
    TIE
}
