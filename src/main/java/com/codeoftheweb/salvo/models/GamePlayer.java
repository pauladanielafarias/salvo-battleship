package com.codeoftheweb.salvo.models;


import java.time.LocalDateTime;
import java.util.*;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.Max;

//JPA annotations
@Entity  //tells Spring to create a GamePlayer table for this class.

public class GamePlayer {

    /* attributes with JPA annotations*/
    @Id  //says that the id instance variable holds the database key for this class.
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    //tells JPA to use whatever ID generator is provided by the database system
    @GenericGenerator(name = "native", strategy = "native")
    //tells JPA to use whatever ID generator is provided by the database system.
    private long id;

    @ManyToOne(fetch = FetchType.EAGER) //when fetching a GamePlayer, JPA should automatically fetch the players
    @JoinColumn(name = "player_id") //tells Spring to create a player_id column in the GamePlayer class column
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER) //when fetching a GamePlayer, JPA should automatically fetch the game
    @JoinColumn(name = "game_id") //tells Spring to create a game_id column in the GamePlayer class column
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salvo> salvoes = new HashSet<>();


    /* attributes without JPA annotations*/
    private LocalDateTime creationDate;


    /*constructors*/

    public GamePlayer() {
    }

    public GamePlayer(LocalDateTime creationDate, Game game, Player player) {
        this.creationDate = creationDate;
        this.game = game;
        this.player = player;
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    /*DTO methods*/

    //GAME PLAYER DTO - brings the id from the GamePlayer class and player from the player DTO.
    public Map<String, Object> gamePlayersDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().playersDTO());
        if (this.getPlayerScore() != null)
            dto.put("score", this.getPlayerScore().getPoints());
        else
            dto.put("score", null);
        return dto;
    }

    //GAME VIEW DTO -
    public Map<String, Object> gameViewDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getGame().getId());
        dto.put("created", this.getGame().getCreationDate());
        dto.put("gamePlayers", this.getGame().getGamePlayers().stream().map(gp -> gp.gamePlayersDTO()));
        dto.put("ships", this.getShips().stream().map(ship -> ship.shipsDTO()));
        dto.put("salvoes", this.getGame().getGamePlayers().stream().flatMap(gp -> gp.getSalvoes().stream().map(salvo -> salvo.salvoesDTO())));
        dto.put("gameState", this.getGameState());
        return dto;
    }

    /*methods*/

    //Adding a ship
    public void addShip(Ship ship) {
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    //Adding a salvo
    public void addSalvo(Salvo salvo) {
        salvo.setGamePlayer(this);
        salvoes.add(salvo);
    }

    //getting the score of a game from a player
    public Score getPlayerScore() {
        return player.getScore(game);
    }


    //-------- GAME LOGIC -------------

    // GAME STATE
    public GameState getGameState() {
        GameState gameState = GameState.UNDEFINED;
        GamePlayer opponent = this.getGame().getGamePlayers()
                .stream()
                .filter(gamePlayer -> gamePlayer.getId() != this.getId())
                .findFirst().orElse(null);

        if (this.getShips().size() == 0) {

            gameState = GameState.PLACE_SHIPS; //"Please place your ships";

        } else {
            if (opponent == null) {
                gameState = GameState.WAITING_FOR_OPPONENT; //"Wait for your opponent";

            } else {
                if (opponent.getShips().size() == 0) {
                    gameState = GameState.WAITING_FOR_OPPPONENT_SHIPS ;//"Wait for your opponent to place their ships";

                } else {
                    int opponentTurn = opponent.getSalvoes()
                            .stream().mapToInt(Salvo::getTurn).max().orElse(0) + 1;
                    int myTurn = this.getSalvoes()
                            .stream().mapToInt(Salvo::getTurn).max().orElse(0) + 1;

                    if (this.getId() < opponent.getId() && myTurn == opponentTurn) {
                        gameState = GameState.SHOOT_SALVOES; //"It's your turn to shoot.";

                    }else if (this.getId() < opponent.getId() && myTurn > opponentTurn) {
                            gameState = GameState.WAITING_FOR_OPPONENT_SHOOT_SALVOES; //"Wait for your opponent to shoot.";

                    }else if (this.getId() > opponent.getId() && myTurn == opponentTurn) {
                                gameState = GameState.WAITING_FOR_OPPONENT_SHOOT_SALVOES; //"Wait for your opponent to shoot.";

                    }else if(this.getId() > opponent.getId() && myTurn < opponentTurn){
                                    gameState = GameState.SHOOT_SALVOES; //"It's your turn to shoot.";

                    }

                    Salvo lastSalvo = this.getSalvoes().stream().filter(salvo -> salvo.getTurn() == myTurn).findFirst().orElse(null);
                    Salvo opponentLastSalvo = opponent.getSalvoes().stream().filter(salvo -> salvo.getTurn() == myTurn).findFirst().orElse(null);


                    if(lastSalvo != null && opponentLastSalvo != null){

                        if(lastSalvo.getHits().size() >0){
                            gameState = GameState.SHOOT_AGAIN; //"Hit! You can shoot again";
                        }

                        int mySinks = lastSalvo.getSinks().size();
                        int opponentSinks = opponentLastSalvo.getSinks().size();

                        if(myTurn == opponentTurn && mySinks == 5 && opponentSinks < 5){
                            gameState = GameState.WON;

                        }else if(myTurn == opponentTurn && mySinks < 5 && opponentSinks == 5){
                            gameState = GameState.LOST;

                        }else if(myTurn == opponentTurn && mySinks == 5 && opponentSinks == 5){
                            gameState = GameState.TIE;
                        }
                    }
                }
            }
        }

        return gameState;
    }


} //end of class
