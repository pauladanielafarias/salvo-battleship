package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//JPA annotations
@Entity  //tells Spring to create a ship table for this class.

public class Ship {

    /* attributes with JPA annotations*/
    @Id  //says that the id instance variable holds the database key for this class.
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")  //tells JPA to use whatever ID generator is provided by the database system
    @GenericGenerator(name = "native", strategy = "native")  //tells JPA to use whatever ID generator is provided by the database system.
    private long id; // attribute

    @ManyToOne(fetch = FetchType.EAGER) //when fetching a Ship, JPA should automatically fetch the game players
    @JoinColumn(name="gamePlayer_id") //tells Spring to create a gamePlayer_id column in the Ship class column
    private GamePlayer gamePlayer;

    @ElementCollection
    private List<String> locations;

    /* attributes without JPA annotations*/
    private String shipType;


    /*constructors*/

    public Ship() {
    }

    public Ship(List<String> locations, String shipType) {
        this.gamePlayer = gamePlayer;
        this.locations = locations;
        this.shipType = shipType;
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

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    /*DTO methods*/
    //SHIPS DTO - brings the location and type from the Ship class.
    public Map<String, Object> shipsDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("location", this.getLocations());
        dto.put("type",this.getShipType());
        return dto;
    }


}
