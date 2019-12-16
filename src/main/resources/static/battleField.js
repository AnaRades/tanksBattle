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
        // Get the list element and clear it
        var list = document.getElementById('tanksList');
        list.options.length = 0;
        for (var i = 0; i < tanksList.length; i++) {
            // Create the list item:
            var option = document.createElement("option");
            option.text = tanksList[i];
            list.add(option);
        }

        // Finally, enable the list:
        list.disabled=false;
    }

    function getTankProperties() {
        let xhr = new XMLHttpRequest();
        var tankUrl = 'http://localhost:8080/gettankprop?name='+document.getElementById('tanksList').value;
        xhr.open('GET', tankUrl);
        xhr.send();

        xhr.onreadystatechange = function() {
            if(this.readyState == 4 && this.status == 200) {
                var tank = JSON.parse(this.responseText);
                var txt = "Tank " + tank.tankName + " has damage " + tank.damage + " and totalHealth " + tank.health;
                document.getElementById("tankdetails").innerHTML = txt;
            }
        }

        xhr.onerror = function() {
            alert("Request failed");
        };
    }