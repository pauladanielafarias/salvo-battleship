package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//JPA annotations
@Entity  //tells Spring to create a salvo table for this class.

public class Salvo {

    /* attributes with JPA annotations*/
    @Id  //says that the id instance variable holds the database key for this class.
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")  //tells JPA to use whatever ID generator is provided by the database system
    @GenericGenerator(name = "native", strategy = "native")  //tells JPA to use whatever ID generator is provided by the database system.
    private long id; // attribute

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id") //tells Spring to create a gamePlayer_id column in the Salvo class column
    private GamePlayer gamePlayer;

    @ElementCollection
    private List<String> locations;

    /* attributes without JPA annotations*/
    private int turn;


    /*constructors*/

    public Salvo() {
    }

    public Salvo(List<String> locations, int turn) {
        this.locations = locations;
        this.turn = turn;
    }

    /*getters and setters*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }



    /*DTO methods*/
    //SALVOES DTO - brings the turn, locations and playerId from the Salvo class.
    public Map<String, Object> salvoesDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn",this.getTurn());
        dto.put("locations", this.getLocations());
        dto.put("player", this.getGamePlayer().getPlayer().getId());
        return dto;
    }



}
