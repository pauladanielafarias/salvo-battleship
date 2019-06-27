package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;

import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api") //this adds /api to every urls of the classes that will be used in this controller so that they don't overlapse with the /rest. In /rest the JSON will bring all the information from a class, while in /api I can control what the JSON will contain

public class SalvoController {

    /*---------------------------PLAYERS---------------------------------*/
    //AUTOWIRED PLAYER REPOSITORY
    @Autowired  //tells Spring to create an instance of PlayerRepository and store it in the instance variable playerRepo. Also, keeps track of that instance and uses it for any other class that has autowired PlayerRepository. This allows the objects to be shared and managed by Spring.
    private PlayerRepository playerRepo;

    //AUTOWIRED PASSWORD ENCODER
    @Autowired
    PasswordEncoder passwordEncoder;

    /*PLAYERS POST METHOD: REGISTER / SIGN UP*/
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String username, @RequestParam String password) {
                ResponseEntity<Object> responseEntity;

                if (username.isEmpty() || password.isEmpty()) {
                    responseEntity = new ResponseEntity<>("Missing data (username or password empty)", HttpStatus.FORBIDDEN);
                }else if (playerRepo.findByUsername(username) != null) {
                    responseEntity = new ResponseEntity<>("Username already in use", HttpStatus.FORBIDDEN);
                }else {
                    playerRepo.save(new Player(username, passwordEncoder.encode(password)));
                    responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
                }
            return responseEntity;
    }

    /*---------------------------GAMES---------------------------------*/
    //AUTOWIRED GAME REPOSITORY
    @Autowired
    private GameRepository gameRepo;

    /*GAMES DTO CONTROLLER: GET GAMES*/
    @RequestMapping("/games")
    public Map<String,Object> getGames(Authentication authentication) {
        Map<String,Object> map = new HashMap<>();
        if(isGuest(authentication)){
            map.put("Player", "GUEST");
        }
        else {
            map.put("Player", authentication.getName());
        }
        map.put("Games", gameRepo
                .findAll()
                .stream()
                .map(game -> game.gamesDTO())
                .collect(Collectors.toList()));
        return map;
    }


    /*GAMES POST METHOD: CREATE GAME*/
    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(Authentication authentication) {
        ResponseEntity<Object> responseEntity;

        if (isGuest(authentication)) {
            responseEntity = new ResponseEntity<>("Not logged in", HttpStatus.UNAUTHORIZED);
        }else {
            Game game =new Game(LocalDateTime.now());
            gameRepo.save(game);
            Player currentPlayer = playerRepo.findByUsername(authentication.getName());
            GamePlayer gamePlayer = new GamePlayer(LocalDateTime.now(), game ,currentPlayer);
            gamePlayerRepo.save(gamePlayer);
            responseEntity = new ResponseEntity<>(makeMap("gpId", gamePlayer.getId()),HttpStatus.CREATED);
        }
        return responseEntity;
    }

    /*---------------------------GAME PLAYERS / GAME VIEW---------------------------------*/

    //AUTOWIRED GAME PLAYER REPOSITORY
    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    /*GAME VIEW DTO CONTROLLER: GET GAME VIEW*/

    @RequestMapping(path ="game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable long gamePlayerId, Authentication authentication) {
        ResponseEntity<Map<String, Object>> responseEntity;

        if (gamePlayerRepo.findById(gamePlayerId) == null) {
            responseEntity = new ResponseEntity<>(makeMap("Error", "There's no gamePlayer with that id."), HttpStatus.FORBIDDEN);
        }else{
            GamePlayer gamePlayer = gamePlayerRepo.findById(gamePlayerId);
            Player player = playerRepo.findByUsername(authentication.getName());
            if(gamePlayer.getPlayer().getId() == player.getId()){
                responseEntity = new ResponseEntity<>(gamePlayer.gameViewDTO(), HttpStatus.OK);
            } else{
                responseEntity = new ResponseEntity<>(makeMap("Error", "Not your game."), HttpStatus.FORBIDDEN);
            }
        }
        return responseEntity;
    }

    /*GAME VIEW POST METHOD: JOIN GAME*/
    @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame(Authentication authentication, @PathVariable long gameId) {
        ResponseEntity<Object> responseEntity;

        if (isGuest(authentication)) {
            responseEntity = new ResponseEntity<>("Not logged in", HttpStatus.UNAUTHORIZED);
        }
        Game game = gameRepo.findById(gameId);
        if(game == null){
            responseEntity = new ResponseEntity<>("No such game.", HttpStatus.FORBIDDEN);
        }
        if(game.getGamePlayers().size() != 1){
            responseEntity = new ResponseEntity<>("This game is full.", HttpStatus.FORBIDDEN);
        }

        Player currentPlayer = playerRepo.findByUsername(authentication.getName());
        if(game.getGamePlayers().stream().anyMatch(gamePlayer -> gamePlayer.getPlayer().getId() == currentPlayer.getId())){
            responseEntity = new ResponseEntity<>("You are already playing this game.", HttpStatus.FORBIDDEN);
        }else{
            GamePlayer gamePlayer = new GamePlayer(LocalDateTime.now(), game ,currentPlayer);
            gamePlayerRepo.save(gamePlayer);
            responseEntity = new ResponseEntity<>(makeMap("gpId", gamePlayer.getId()),HttpStatus.CREATED);
        }

        return responseEntity;
    }

    /*---------------------------SHIPS---------------------------------*/
    @RequestMapping(path ="/games/players/{gamePlayerId}/ships")
    public ResponseEntity<Object> addShips(@PathVariable long gamePlayerId, Authentication authentication, @RequestBody List<Ship> shipsList) {
        ResponseEntity<Object> responseEntity;

        if(isGuest(authentication)){
            responseEntity = new ResponseEntity<>(makeMap("error", "Not logged in."), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayerRepo.findById(gamePlayerId) == null) {
            responseEntity = new ResponseEntity<>(makeMap("error","There's no gamePlayer with that id."), HttpStatus.UNAUTHORIZED);
        }

        GamePlayer gamePlayer = gamePlayerRepo.findById(gamePlayerId);
        Player currentPlayer = playerRepo.findByUsername(authentication.getName());
        if(gamePlayer.getPlayer().getId() != currentPlayer.getId()) {
            responseEntity = new ResponseEntity<>(makeMap("error","The current user is not the gamePlayer the ID references"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer.getShips().size() > 0){
            responseEntity = new ResponseEntity<>(makeMap("error","There are already ships."), HttpStatus.FORBIDDEN);

        }else if(shipsList.size() != 5){
            responseEntity = new ResponseEntity<>(makeMap("error","You need to have 5 ships"),HttpStatus.FORBIDDEN);

        }else{
            shipsList.forEach(ship -> gamePlayer.addShip(ship));
            gamePlayerRepo.save(gamePlayer);
            responseEntity = new ResponseEntity<>(makeMap("success","Ships created!"),HttpStatus.CREATED);
        }

        return responseEntity;
    }


    /*---------------------------OTHER METHODS---------------------------------*/
    /*MAKE MAP METHOD*/
    private Map<String, Object> makeMap(String key, Object value){
        Map<String, Object> map = new HashMap<>();
        map.put(key,value);
        return map;
    }

    /*IS GUEST METHOD */
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }





}
