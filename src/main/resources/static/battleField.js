var mapDetailsElement, tankDetailsElement, tankSelect;


    function init() {
        mapDetailsElement = document.getElementById("ta-mapdetails");
        tankDetailsElement = document.getElementById("ta-tankdetails");
        tankSelect = document.getElementById('sel-tanks');
    }

    function getTanksList() {
        let xhr = new XMLHttpRequest();
        xhr.open('GET', 'http://localhost:8080/gettanks');
        xhr.send();

        xhr.onreadystatechange = function() {
            if(this.readyState == 4 && this.status == 200) {
                populateTankList(JSON.parse(this.responseText));
            }
        }
        xhr.onerror = function() {
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
        tankSelect.disabled=false;
    }

    function getTankProperties() {
        let xhr = new XMLHttpRequest();
        var tankUrl = 'http://localhost:8080/gettankprop?name='+tankSelect.value;
        xhr.open('GET', tankUrl);
        xhr.send();

        xhr.onreadystatechange = function() {
            if(this.readyState == 4 && this.status == 200) {
                var tank = JSON.parse(this.responseText);
                var txt = "Tank " + tank.tankName + " has damage " + tank.damage + " and totalHealth " + tank.health;
                document.getElementById("ta-tankdetails").innerHTML = txt;
            }
        }

        xhr.onerror = function() {
            alert("Request failed");
        };
    }

    function getMapDetails() {
        let xhr = new XMLHttpRequest();
        xhr.open('GET', 'http://localhost:8080/map');
        xhr.send();

        xhr.onreadystatechange = function() {
            if(this.readyState == 4 && this.status == 200) {
                mapDetailsElement.innerHTML = this.responseText;
            }
        }

        xhr.onerror = function() {
            alert("Request failed");
        };
    }

    function startGame() {
        const eventSource = new EventSource('http://localhost:8080/notification');
        eventSource.onmessage = e => {
            const msg = e.data;
             console.log('event: ' + msg);
            document.getElementById("greet").innerHTML += msg;
        };
        eventSource.onopen = e => console.log('open');
        eventSource.onerror = e => {
            if (e.readyState == EventSource.CLOSED) {
                console.log('close');
            }
            else {
                console.log(e);
            }
        };
        eventSource.addEventListener('second', function(e) {
            console.log('second', e.data);
        }, false);
    }
