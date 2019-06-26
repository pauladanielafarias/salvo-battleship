package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;


//JPA annotations
@Entity  //tells Spring to create a game table for this class.


public class Game {
    /* attributes with JPA annotations*/
    @Id  //says that the id instance variable holds the database key for this class.
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")  //tells JPA to use whatever ID generator is provided by the database system
    @GenericGenerator(name = "native", strategy = "native")  //tells JPA to use whatever ID generator is provided by the database system.
    private long id; // attribute

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER) //when fetching a Game, JPA should automatically fetch the game players
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER) //when fetching a Game, JPA should automatically fetch the scores
    private Set<Score> scores = new HashSet<>();

    /* attributes without JPA annotations*/
    private LocalDateTime creationDate;


    /*constructors*/
    public Game() { }

    public Game(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /*getters and setters*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }


    /*DTO methods*/
    //GAME DTO - brings the id, created from the Game class and gamePlayers from the gamePlayer DTO.
    public Map<String, Object> gamesDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("created",this.getCreationDate());
        dto.put("gamePlayers", this.getGamePlayers().stream().map(gamePlayer -> gamePlayer.gamePlayersDTO()));
        return dto;
    }


    /*methods*/

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    // returns a list of the players that are playing, but it is ignored because of the @JsonIgnore
    @JsonIgnore
    public List<Player> getPlayers() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }


}

