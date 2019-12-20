var mapDetails, tankDetails, tankSelect, battleEvents;
var selBattleEvents;

function init() {
    mapDetails = document.getElementById("ta-mapdetails");
    tankDetails = document.getElementById("ta-tankdetails");
    tankSelect = document.getElementById('sel-tanks');
    battleEvents = document.getElementById("ta-battleevents");
    selBattleEvents = document.getElementById("sel-battleevents");
}

function getTanksList() {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/gettanks');
    xhr.send();

    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            populateTankList(JSON.parse(this.responseText));
        }
    }
    xhr.onerror = function () {
        alert("Request failed");
    };
}

function populateTankList(tanksList) {
    tankSelect.options.length = 0;
    for (var i = 0; i < tanksList.length; i++) {
        // Create the list item:
        var option = document.createElement("option");
        option.text = tanksList[i];
        tankSelect.add(option);
    }

    // Finally, enable the list:
    tankSelect.disabled = false;
}

function getTankProperties() {
    let xhr = new XMLHttpRequest();
    var tankUrl = 'http://localhost:8080/gettanks/' + tankSelect.value;
    xhr.open('GET', tankUrl);
    xhr.send();

    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var tank = JSON.parse(this.responseText);
            var txt = "Tank " + tank.name + " has damage " + tank.damage + " and totalHealth "
                      + tank.health;
            tankDetails.innerHTML = txt;
        }
    }

    xhr.onerror = function () {
        alert("Request failed");
    };
}

function getMapDetails() {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/getmap');
    xhr.send();

    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            mapDetails.innerHTML = this.responseText;
        }
    }

    xhr.onerror = function () {
        alert("Request failed");
    };
}

/**
    Send a POST command to server to start the game. If server responds with OK,
    subscribe to battle event through Server Sent Events.
    On each event, the unrequested game logs are returned. When battle is over unsubscribe and close connection
**/
function startGame() {
   let xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost:8080/startgame');
    xhr.send();

    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            selBattleEvents.options.length = 0;
            subscribeToEvents(this.responseText);
        }
    }

    xhr.onerror = function () {
        alert("Request failed");
    };

    subscribeToEvents('dsa');
}

function subscribeToEvents(battleId) {
    const eventSource = new EventSource('http://localhost:8080/gamenotification?id='+battleId);
    eventSource.onmessage = e => {
        const msgArr = e.data.split(';');
        for (var i = 0; i < msgArr.length; i++) {
            var option = document.createElement("option");
            option.text = msgArr[i];
            selBattleEvents.add(option);
            console.log('event: ' + msgArr[i]);
        }
        if(msgArr.length == 0) {
            eventSource.close();
        }
    };
    eventSource.onopen = e => console.log('open');
    eventSource.onerror = e => {
        if (e.readyState == EventSource.CLOSED) {
            console.log('close');
        } else {
            console.log(e);
        }
    };
}
