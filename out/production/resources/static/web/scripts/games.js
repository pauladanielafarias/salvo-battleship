//fetch for games
function getData(){
    return fetch("/api/games", {mode:'no-cors'})
    .then(function(response) {
        return response.json()
    }).then(function(json) {
        app.games = json.Games;
        app.currentPlayer = json.Player;
        //printGames();
    })
}

getData();

//login
function login(evt, newUser) {
    evt.preventDefault();
    var form = evt.target.form;
    $.post("/api/login", 
           { username: form["username"].value,
             password: form["password"].value })
    .done(function(){
        getData().then(function(){
            if(newUser){
                swal({title:"Welcome! You are now registered as " + app.currentPlayer +"!", button:{text:"Continue", className:"signup-button"}})
            } else{
                swal({title:"Welcome " + app.currentPlayer +"!", button: {text:"X", className:"welcome-button"}}) 
            }
        })})
     .fail(function(){swal({text:"Invalid Username or Password", icon:"warning", button:{className:"fail-login-button"}})});
  }

//logout
  function logout(evt) {
    evt.preventDefault();
    $.post("/api/logout")
     .done(function(){
        swal({text:"Thanks for playing!", button:{text:"Bye!", className:"logout-button"}})
        getData();
        
     })
     .fail();
  }

  //sign up
  function signup(evt) {
    evt.preventDefault();
    var form = evt.target.form;
    if(!validateEmail(form["username"].value)){
        swal({text:"You have entered an invalid email address!", icon:"error", button:{className:"fail-signup-button"}})
    }else{
        $.post("/api/players",
            { username: form["username"].value,
            password: form["password"].value })
        .done(function(){ 
            getData().then(function(){
                login(evt,true) 
            })
        })
        .fail(function(){
            swal({title:"The username already exists.", button:{text:"Back", className:"fail-signup-button"}})
        });
    }
  }

  function validateEmail(email) 
{
    var regex = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return regex.test(email) ? true : false;
}


//Vue.js

var app = new Vue({
    el: '#app',
//data in Vue.js
    data: {
     currentPlayer: "",
     games: [],
     url: []
    },
    
    methods:{
        //sort by max
        sortByMax: function(a,b){
            return b-a
        },
        //leaders (leaderboard)
        leaders: function(){
            let leaders = []
            let aux = []

            this.games.forEach(game => {
                game.gamePlayers.forEach(gp => {
                    if(aux.indexOf(gp.player.id) == -1){
                        aux.push(gp.player.id)

                        //creates an object that will have the username and scores
                        let obj = {} 
                        obj.id = gp.player.id
                        obj.username = gp.player.email
                        obj.score = gp.score
                        obj.won = gp.score == 1.0? 1:0;
                        obj.lost = gp.score == 0.0? 1:0;
                        obj.tied = gp.score == 0.5? 1:0;
                        leaders.push(obj)

                    }else{
                        leaders.forEach(leader => {
                            if(leader.id == gp.player.id){
                                leader.score += gp.score;
                                leader.won += gp.score == 1.0? 1:0;
                                leader.lost += gp.score == 0.0? 1:0;
                                leader.tied += gp.score == 0.5? 1:0;
                            }
                        })
                    }
                })
            });
            //sorts leaders by the highest score
            leaders.sortByMax
            return leaders
        },

        needLoginAlert: function(){
            swal({title:"Please login to join a game", button:{text:"Back", className:"fail-signup-button"}})
        }
        
    }
})


//fetch for urls
/*
fetch("/api/game_view/"+ paramObj(location.search).gp, {mode:'no-cors'})
.then(function(response) {
    return response.json()
}).then(function(json) {
    app.url = json;
});

// function that makes the symbols compatible on the url
function paramObj(search) {
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;
  
    search.replace(reg, function(match, param, val) {
      obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });
  
    return obj;
}
*/



//function that print games
/*function printGames(){
    //function that get the email from players
    gameData.forEach(function(game) {
        var emailPlayer = []
        game.gamePlayers.forEach(function(gamePlayer){
            emailPlayer.push(gamePlayer.player.email);
        });

        document.getElementById("game-list").innerHTML+="<li>"+ "Game id: "+game.id + ", Created: " + game.created + ", Email player 1: " + emailPlayer[0] + ", Email player 2: " + emailPlayer[1] + "</li> </br>" ;
    });
}*/






