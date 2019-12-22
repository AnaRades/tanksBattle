<h3>Tanks Battle project</h3>

<h4>How to run
1. run mvn clean package
2. run docker-compose up -bui;d
3. go to http://localhost:9090/BattleField.html 

<h4>Game interpretation

This is a turn based game.
When battle is started the map is retrieved from the database in cycle manner.
Each tank starts off at opposite ends of the map and during their turn have the chance to make the following moves:

1. If tank has clear shot  to enemy he takes the shot

    1.1 Shot trajectory is horizontal, vertical or diagonal    
    1.2 Clear shot means such a trajectory exists and no obstacles are on the way
    
2. If tank doesn't have a clear shot, he determines path to enemy and advances 5 steps per turn
3. If tank is low on health, he will prioritize hidding along with trying to kill the enemy
4. If for some reason tanks get blocked (they both circle around the same obstacle when on low health), 
they will try to go around the obstacle with respect to their orientation.
5. Game is over when one tank's health goes to 0 or below. 


<h4>Implementaion Details

The UI is a html page, with actions written in javascript.
Database is a Mongo database. On application startup, we start with a clean database and add test data for maps and tanks.
Backend is done with Spring boot. 

During battle, after each tank makes his move, a log of this event is sent to the client through <b>Server Sent Events</b>.
We have an EventSource on client that connects to the notification controller. Then the controller creates a <b>SSE emitter</b>,
connects this emitter to the battle and schedules sending of updates each 2 seconds.

Database connection is done with <b>Mongo drivers</b> and dependecy from Spring.

Tanks have a basic AI that allow them to navigate the map and shoot enemies.
Shoot algorithm determines if there is a path(straight line) from source to destination and no obstacles are in the way.
It firsts determines trajectory and then check for obstacles.

<b>Path finding algorithm</b> for navigate and duck uses a recursive approach. It converts the map into a 2 dimensional array and marks where obstacles are found.
It pads the edges with obstacles so we don't go outside the map limits. 
Starting at source, create  path by recursively going each NSWE direction. If the path blocks, it is discarded.

  
<h4>Troubleshooting

If http://localhost:9090/BattleField.html  is unreachable, try:
 - replacing localhost with the IP of docker container. For instance http://192.168.99.100:9090/BattleField.html .
 - ports may not be forwarded from virtual machine to host machine. In docker client run :
 
    <b>VBoxManage controlvm "boot2docker-vm" natpf1 "tcp-port5000,tcp,,8080,,8080";
 
 