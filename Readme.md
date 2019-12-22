Tanks Battle project

How to run
1. run mvn clean package
2. run docker-compose up -bui;d
3. go to http://localhost:9090/BattleField.html 

Game interpretation

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

Troubleshooting

If http://localhost:9090/BattleField.html  is unreachable, try:
 - replacing localhost with the IP of docker container. For instance http://192.168.99.100:9090/BattleField.html .
 - ports may not be forwarded from virtual machine to host machine. In docker client run :
 
    <b>VBoxManage controlvm "boot2docker-vm" natpf1 "tcp-port5000,tcp,,8080,,8080";
 
 