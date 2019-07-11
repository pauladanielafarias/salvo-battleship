package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

//JPA annotations
@Entity  //tells Spring to create a player table for this class.


public class Player {

    /* attributes with JPA annotations*/
    @Id  //says that the id instance variable holds the database key for this class.
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")  //tells JPA to use whatever ID generator is provided by the database system
    @GenericGenerator(name = "native", strategy = "native")  //tells JPA to use whatever ID generator is provided by the database system.
    private long id;

    @OneToMany(mappedBy="player", fetch= FetchType.EAGER) //when fetching a Player, JPA should automatically fetch the game players
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="player", fetch= FetchType.EAGER) //when fetching a Player, JPA should automatically fetch the scores
    private Set<Score> scores = new HashSet<>();

    /* attributes without JPA annotations*/
    private String firstName;
    private String lastName;
    private String username; //will be the email
    private String password;


    /*constructors*/
    public Player() { }

    public Player(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password =password;
    }

    public Player(String username, String password) {
        this.username = username;
        this.password =password;
    }


    /*getters and setters*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<GamePlayer> getGamePlayers() {  //
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




    /*DTO methods*/
    //PLAYERS DTO - brings the id and username(email) from the Player class
    public Map<String, Object> playersDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("email", this.getUsername());
        return dto;
    }

    /*methods*/
    public String toString() {
            return firstName + " " + lastName;
    }

    //Adds a GamePlayer
    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    //Gets the Score from an specific game
    public Score getScore(Game game){
        return scores.stream()
                .filter(score -> score.getGame().getId() == game.getId())
                .findAny().orElse(null);
    }


    // returns a list of the games a player is playing
    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }


    }

