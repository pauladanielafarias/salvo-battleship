//Vue.js - app
var app = new Vue({
    el: '#app',
//data in Vue.js
    data: {
        usernameP1: "",
        usernameP2: "",
        currentPlayer: "",
        currentPlayerId: "",
        ships: [] ,
        dataSalvoes: [],
        gameState: "",
     
    }       

})

// game view fetch
getGameView()

function getGameView(){
    fetch("/api/game_view/"+ paramObj(location.search).gp, {mode:'no-cors'})
    .then(function(response) {
        return response.json()
        
    }).then(function(json) {
        //if there's a 403 error so the grid won't be display and the error 403 image will be displayed
        if(json.error != undefined){
            console.log(json.error)
            document.getElementById('error').classList.add('show')
            document.getElementById('app').style.display = 'none'
            
        } else{   
            // if there's no error 403 so the grid will display 
            document.getElementById('error').classList.remove('show')
            document.getElementById('app').style.display = 'block'
            
            //saving the json in the variable gameView
            gameView = json;
            //saving the ships from the json in vue.js
            app.ships = json.ships;
            //saving the gameState from the json in vue.js
            app.gameState = json.gameState;

            // calling the funtion that prints the game players username
            printPlayersUsername(); 

            if(gameView.ships.length == 0){
                loadGrid(false); // loads grid. when there are still ships missing the grid is not static
            }else{
                loadGrid(true); // loads grid. when there are no ships missing the grid is static
            }

            // loads salvoes when it loads the page
            salvoes();
            addSalvoes();
            
            //loads game state
            checkGameState();

            //changing the url so that the differents alerts can be shown on the correct part of the game
            if(paramObj(location.search).newgame != undefined && paramObj(location.search).newgame == 'true'){
                swal({text:"Hey " + app.currentPlayer +", you've created a new game! Have fun!", icon:"success", button: {text:"Great", className:"createdGame-button"}})
            }
            if(paramObj(location.search).join != undefined && paramObj(location.search).join == 'true'){
                swal({text:"Hey " + app.currentPlayer + ", you've joined a new game. Have fun!", icon:"success", button: {text:"Thanks", className:"join-button"}})
            }
            if(paramObj(location.search).newships!= undefined && paramObj(location.search).newships == 'true'){
                swal({text:"You've added the ships. Now wait for another player to join you!", icon:"success", button: {text:"Great!", className:"newships-button"}})
            }
            if(paramObj(location.search).newsalvoes!= undefined && paramObj(location.search).newsalvoes == 'true'){
                swal({text:"You've shot a paw! Now wait for the other player to fire back.", icon:"success", button: {text:"Great!", className:"newships-button"}})
            }
        }
    }).catch(function(error){
        console.log(error.error)
        //swal({text:"Invalid Username or Password", icon:"warning", button:{className:"fail-login-button"}});
    });
}


//-------------- SHIPS -------------------
// add ships function that contains the fetch(post)
function addShips(){
    let dataShips = []
    let shipsList = document.querySelectorAll(".grid-stack-item");

    shipsList.forEach(function(ship){
        let x = +(ship.dataset.gsX);
        let y = +(ship.dataset.gsY);
        let w = +(ship.dataset.gsWidth);
        let h = +(ship.dataset.gsHeight);
        let obj={};
        let locs = []
        let type = ship.id;

        if(h>w){
            for(i=0; i< h; i++){
                locs.push(String.fromCharCode(65+y+i)+(x+1))
            }
        }else{
            for(i=0; i< w; i++){
                locs.push(String.fromCharCode(65+y)+(x+1+i))
            }
        }
        obj.locations = locs;
        obj.shipType = type;

        dataShips.push(obj)
    })
//adding the ships fetch (post)
    fetch("/api/games/players/" + paramObj(location.search).gp + "/ships",{
        method:'POST',
        body: JSON.stringify(dataShips),
        headers: {'Content-Type': 'application/json'}
        
    })
    .then(function(response) {
        return response.json()
    })
    .then(function(json) {
        console.log(json)
        window.location.replace("game-view.html?gp="+paramObj(location.search).gp+"&newgame=false&join=false&newships=true")
    })
    .catch(function(error){
        console.log(error)
    })
}

//------------ SALVOES ------------------

// add salvoes function 
function addSalvoes(){
 
    let salvoesCells = document.querySelectorAll(".grid-salvoes .grid-cell");
    
    //add salvo's img when you click on one cell
    for(i=0; i<salvoesCells.length; i++){
        salvoesCells[i].addEventListener("click", function(){
            addSalvoImg(event);
        })    
    }
    //adding and removing salvo's img
    function addSalvoImg(evt){
        let cell = evt.target;
        if(cell.classList.contains("salvoes-img")){
            cell.classList.remove('salvoes-img');
        } else{
            cell.classList.add('salvoes-img');
        }
    }

 }

 //adding the salvoes fetch(post)
 function postSalvoes(){
    let postSalvoes = []
    let salvoesPaws = document.querySelectorAll(".grid-salvoes .grid-cell.salvoes-img");
    //posting the salvos to the back-end
    salvoesPaws.forEach(function(paw){
        let y = paw.dataset.y; // letter
        let x = +(paw.dataset.x); // number
        let location = y+x;

        postSalvoes.push(location);  
    })

    fetch("/api/games/players/" + paramObj(location.search).gp + "/salvoes",{
        method:'POST',
        body: JSON.stringify(postSalvoes),
        headers: {'Content-Type': 'application/json'}
    })
    .then(function(response) {
        if(response.ok){
            return response.json()
        }else{
            return Promise.reject(response.json())
        }     
    })
    .then(function(json) {
        checkGameState()
        gameViewRefresh()
        console.log(json)
    })
    .catch(function(error){
        return error
    })
    .then(function(jsonError){
        if(jsonError != undefined){
            console.log(jsonError)
            swal({text:jsonError.error, button: {text:"Ok", className:"paws-button"}})
        }
    })
 }

 var interval;
 function gameViewRefresh(){
    interval = setInterval(function(){
        fetch("/api/game_view/"+ paramObj(location.search).gp, {mode:'no-cors'})
        .then(function(response) {
            return response.json()
        })
        .then(function(json){
            gameView = json;
        })
        .then(function(){
            salvoes();
            checkGameState();
        })
        .catch(function(error){
            console.log(error)
        })
    },1000)
 }
 gameViewRefresh();


//----------------- 
// function that makes the symbols compatible on the url
function paramObj(search) {
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;
  
    search.replace(reg, function(match, param, val) {
      obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });
  
    return obj;
}


//--------------------- PLAYERS USERNAMES ---------------
// printing game players usernames depending on the id
function printPlayersUsername(){
    if(gameView.gamePlayers.length > 1){
        if(gameView.gamePlayers[0].id == paramObj(location.search).gp){
            app.usernameP1 = gameView.gamePlayers[0].player.email;
            app.currentPlayer = gameView.gamePlayers[0].player.email
            app.currentPlayerId = gameView.gamePlayers[0].player.id
            app.usernameP2 = gameView.gamePlayers[1].player.email;
        }else{
            app.usernameP1 = gameView.gamePlayers[1].player.email;
            app.usernameP2 = gameView.gamePlayers[0].player.email;
            app.currentPlayer = gameView.gamePlayers[1].player.email
            app.currentPlayerId = gameView.gamePlayers[1].player.id
        }
    } else{
        app.usernameP1 = gameView.gamePlayers[0].player.email;
            app.currentPlayer = gameView.gamePlayers[0].player.email
            app.currentPlayerId = gameView.gamePlayers[0].player.id
            app.usernameP2 = "waiting..."
    }
    
}


//--------------------------- GRIDS ---------------------
/*SHIPS GRID*/

//main function that shoots the gridstack.js framework and load the grid with the ships
const loadGrid = function (hasShips) {

    document.getElementById("grid").innerHTML = ""
    var options = { 
        //10 x 10 grid
        width: 10,
        height: 10,
        //space between elements (widgets)
        verticalMargin: 0,
        //height of cells
        cellHeight: 45,
        //disables resizing of widgets
        disableResize: true,
        //floating widgets
		float: true,
        //removeTimeout: 100,
        //allows the widget to occupy more than one column
        disableOneColumnMode: true,
        //false allows widget dragging, true denies it
        staticGrid: hasShips,
        //activates animations
        animate: true
    }


    //grid initialization
    $('.grid-stack').gridstack(options);

    grid = $('#grid').data('gridstack');

    //adding the ships
    if(hasShips){
        for(i=0;i<gameView.ships.length;i++){
            let shipType = gameView.ships[i].type; //type
            let x = +(gameView.ships[i].location[0].slice(1)) - 1; //number
            let y = gameView.ships[i].location[0].slice(0,1).toUpperCase().charCodeAt(0)-65; //letter
            let w; //width
            let h; //height

            if(gameView.ships[i].location[0].slice(0,1) == gameView.ships[i].location[1].slice(0,1)){
                w = gameView.ships[i].location.length;
                h = 1;
                grid.addWidget($('<div id="'+shipType+'"><div class="grid-stack-item-content '+shipType+'Horizontal"></div><div/>'), x, y, w, h);
                //check that the shipType is written in camel case in the back-end Salvo Application
            } else{
                h = gameView.ships[i].location.length;
                w = 1;
                grid.addWidget($('<div id="'+shipType+'"><div class="grid-stack-item-content '+shipType+'Vertical"></div><div/>'), x, y, w, h);
                //check that the shipType is written in camel case in the back-end Salvo Application
            }
        }
    } else{
        grid.addWidget($('<div id="carrier"><div class="grid-stack-item-content carrierHorizontal"></div><div/>'), 0, 0, 5, 1);
        grid.addWidget($('<div id="battleship"><div class="grid-stack-item-content battleshipHorizontal"></div><div/>'), 0, 1, 4, 1);
        grid.addWidget($('<div id="submarine"><div class="grid-stack-item-content submarineHorizontal"></div><div/>'), 0, 2, 3, 1);
        grid.addWidget($('<div id="destroyer"><div class="grid-stack-item-content destroyerHorizontal"></div><div/>'), 0, 3, 3, 1);
        grid.addWidget($('<div id="patrolBoat"><div class="grid-stack-item-content patrolBoatHorizontal"></div><div/>'), 0, 4, 2, 1);
    }


    createGrid(11, $(".grid-ships"), 'ships')

    if(!hasShips){
        rotateShips("carrier", 5)
        rotateShips("battleship", 4)
        rotateShips("submarine",3)
        rotateShips("destroyer", 3)
        rotateShips("patrolBoat",2)
    }
    

    listenBusyCells('ships')
    $('.grid-stack').on('change', function(){listenBusyCells('ships')})


    //all the functionalities are explained in the gridstack github
    //https://github.com/gridstack/gridstack.js/tree/develop/doc
    
}


//creates the grid structure
const createGrid = function(size, element, id){

    if(document.getElementById(id+"wrapper")){
        document.getElementById(id+"wrapper").innerHTML = ""
    }

    let wrapper = document.createElement('DIV')
    wrapper.id = id+"wrapper"

    wrapper.classList.add('grid-wrapper')

    for(let i = 0; i < size; i++){
        let row = document.createElement('DIV')
        row.classList.add('grid-row')
        row.id =id+`grid-row${i}`
        wrapper.appendChild(row)

        for(let j = 0; j < size; j++){
            let cell = document.createElement('DIV')
            cell.classList.add('grid-cell')
            if(i > 0 && j > 0){
                cell.id = id+`${i - 1}${ j - 1}`
                cell.dataset.y = String.fromCharCode(i - 1 + 65)
                cell.dataset.x = j
            }

            if(j===0 && i > 0){
                let textNode = document.createElement('SPAN')
                textNode.innerText = String.fromCharCode(i+64)
                cell.appendChild(textNode)
            }
            if(i === 0 && j > 0){
                let textNode = document.createElement('SPAN')
                textNode.innerText = j
                cell.appendChild(textNode)
            }
            row.appendChild(cell)
        }
    }
    element.append(wrapper)
}


//--------------------------- ROTATE SHIPS -----------------------

//adds a listener to the ships, wich shoots its rotation when clicked
const rotateShips = function(shipType, cells){

        $(`#${shipType}`).click(function(){
            let x = +($(this).attr('data-gs-x'))
            let y = +($(this).attr('data-gs-y'))
        if($(this).children().hasClass(`${shipType}Horizontal`)){
            if(y + cells - 1 < 10){
                grid.resize($(this),1,cells);
                $(this).children().removeClass(`${shipType}Horizontal`);
                $(this).children().addClass(`${shipType}Vertical`);
            } else{
                grid.update($(this), null, 10 - cells)
                grid.resize($(this),1,cells);
                $(this).children().removeClass(`${shipType}Horizontal`);
                $(this).children().addClass(`${shipType}Vertical`);
                
            }
            
        }else{
            if(x + cells - 1  < 10){
                grid.resize($(this),cells,1);
                $(this).children().addClass(`${shipType}Horizontal`);
                $(this).children().removeClass(`${shipType}Vertical`);
            } else{
                grid.update($(this), 10 - cells)
                grid.resize($(this),cells,1);
                $(this).children().addClass(`${shipType}Horizontal`);
                $(this).children().removeClass(`${shipType}Vertical`);
            }
            
        }
    });

}

//loops over all the grid cells, verifying if they are empty or busy
const listenBusyCells = function(id){
    for(let i = 0; i < 10; i++){
        for(let j = 0; j < 10; j++){
            if(!grid.isAreaEmpty(i,j)){
                $(`#${id}${j}${i}`).addClass('busy-cell').removeClass('empty-cell')
            } else{
                $(`#${id}${j}${i}`).removeClass('busy-cell').addClass('empty-cell')
            }
        }
    }
}

/*SALVOES GRID*/
createGrid(11, $(".grid-salvoes"), 'salvoes')


//adding the salvoes already created in the back-end
function salvoes(){
    let sinks = []

    gameView.salvoes.forEach(salvo => {
        salvo.sinks.forEach(sink => {
            sink.location.forEach(loc => {
                if(salvo.player == app.currentPlayerId){
                    sinks.push(loc)
                } 
            })
        })
    })
           
    //iterate over the salvoes array
    for(i=0;i<gameView.salvoes.length;i++){

        for(j=0; j < gameView.salvoes[i].locations.length; j++){
            let turn = gameView.salvoes[i].turn;
            let x = +(gameView.salvoes[i].locations[j].slice(1)) -1;
            let y = gameView.salvoes[i].locations[j].slice(0,1).charCodeAt(0) - 65;
            let isHit = gameView.salvoes[i].hits.indexOf(gameView.salvoes[i].locations[j]) != - 1 ? true : false;
            let isSink = sinks.indexOf(gameView.salvoes[i].locations[j]) != -1 ? true : false

            if(gameView.salvoes[i].player == app.currentPlayerId){
                document.getElementById("salvoes"+y+x).classList.add("salvo");
                document.getElementById("salvoes"+y+x).classList.remove("salvoes-img");
                document.getElementById("salvoes"+y+x).innerHTML="Turn " + turn;
                if(isHit){
                    document.getElementById("salvoes"+y+x).classList.add("hit");  
                }
                if(isSink){
                    document.getElementById("salvoes"+y+x).classList.add("sink"); 
                }

            } else{
                document.getElementById("ships"+y+x).classList.add("salvo");
                document.getElementById("ships"+y+x).classList.remove("salvoes-img");
                document.getElementById("ships"+y+x).innerHTML="Turn " + turn;
            }
        }   
    }
}


//------------------------- GAME STATE -------------------
//var interval;
function checkGameState(){
    //interval= setInterval(function(){

        if(app.gameState == "WAITING_FOR_OPPONENT"){
            $(".grid-salvoes").attr("id","wait-opponent")
            document.getElementById("wait-opponent").innerHTML="<p>Waiting for an opponent...</p>"
            console.log("gameState: Waiting for an opponent...")

        } else if(app.gameState == "WAITING_FOR_OPPPONENT_SHIPS"){
            $(".grid-salvoes").attr("id","wait-opponent-ships")
            document.getElementById("wait-opponent-ships").innerHTML="<p>Wait for your opponent to place their ships</p>"
            console.log("gameState: Wait for your opponent to place their ships")

        }else if(app.gameState == "SHOOT_SALVOES"){
            document.getElementById("game-state").classList.add("shoot-salvoes");
            document.querySelector(".shoot-salvoes").innerHTML="You can throw your paws now!"
            console.log("gameState: You can throw your paws now!")

        }else if(app.gameState == "WAITING_FOR_OPPONENT_SHOOT_SALVOES"){
            document.getElementById("game-state").classList.add("wait-opponent-shoot");
            document.querySelector(".wait-opponent-shoot").innerHTML="Waiting for your enemy to throw paws at you..."
            console.log("gameState: Waiting for your enemy to throw paws at you...")

        }

        if(app.gameState == "WON"){
                /* when I have the cats vs dogs
                if(currentPlayerData.side == "AUTOBOTS"){
                   $("#win").addClass("autobots-victory")
                } else {
                    $("#win").addClass("decepticons-victory")
                }
                */

                $("#won").addClass("won")
                $("#play-field").hide(1000);
                $("#won").show(1000);

            } else if (app.gameState == "LOST"){
                /*when I have the cats vs dogs
                if(currentPlayerData.side == "AUTOBOTS"){
                   $("#lose").addClass("autobots-defeated")
                } else {
                    $("#lose").addClass("decepticons-defeated")
                }
                */
                $("#play-field").hide(1000);
                $("#lost").show(1000);

            } else if (app.gameState == "TIE"){
                $("#tie").show(1000)
            }
    //},1000)
}

/*
var timerId;
function startInterval() {
  timerId = setInterval(function() { console.log("hi"); }, 1000);
}
startInterval()
*/