package com.codeoftheweb.salvo.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

//JPA annotations
@Entity  //tells Spring to create a score table for this class.

public class Score {

    /* attributes with JPA annotations*/
    @Id  //says that the id instance variable holds the database key for this class.
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")  //tells JPA to use whatever ID generator is provided by the database system
    @GenericGenerator(name = "native", strategy = "native")  //tells JPA to use whatever ID generator is provided by the database system.
    private long id;


    @ManyToOne(fetch = FetchType.EAGER) //when fetching a Score, JPA should automatically fetch the players
    @JoinColumn(name="player_id") //tells Spring to create a player_id column in the Score class column
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER) //when fetching a Score, JPA should automatically fetch the game
    @JoinColumn(name="game_id") //tells Spring to create a game_id column in the Score class column
    private Game game;


    /* attributes without JPA annotations*/
    private double points;

    private LocalDateTime finishDate;


    /*constructors*/
    public Score() {
    }

    public Score(Player player, Game game, double points, LocalDateTime finishDate) {
        this.player = player;
        this.game = game;
        this.points = points;
        this.finishDate = finishDate;
    }

    public Score(Player player, Game game, LocalDateTime finishDate) {
        this.player = player;
        this.game = game;
        this.finishDate = finishDate;
    }

    public Score(Game game, LocalDateTime finishDate) {
        this.game = game;
        this.finishDate = finishDate;
    }

    /*getters and setters*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player playerId) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game gameId) {
        this.game = game;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }




}
