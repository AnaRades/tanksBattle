package tanks.battle.models.tank;

import tanks.battle.models.battle.BattleObserver;
import tanks.battle.models.map.Map;
import tanks.battle.models.map.MapHandler;
import tanks.battle.models.tank.utils.MOVE;
import tanks.battle.models.tank.utils.Position;

public class TankConductor {

    private TankConductor enemyTankConductor;
    private TankBehavior behavior;
    private Tank tank;
    private MapHandler mapHandler;
    private BattleObserver battleObserver;

    private enum DIRECTION {
        EAST,
        WEST,
        NORTH,
        SOUTH
    }


    /**
        Offensive strategy:
     - if enemy in sight:
         if health > 25% shoot
        else duck

    *


    Defensive strategy:
     - if enemy in sight
        if health > 50% shoot
        else duck


     shoot:
        - projectilecan move NSWE

     duck:
        - get nearest obstacle


    **/







    public MOVE makeNextMove() {
        thinkAboutNextMove();
        //if clear shot, then shoot
        if(mapHandler.clearShot(tank.getPosition(), enemyTankConductor.getTankPosition())) {
            return MOVE.SHOOT;
        }
        return MOVE.ADVANCE;
    }

    //attack strategy
    //if enemy in sight -> shoot
    //else advance
    //if last enemy move was shoot and health <= 50% duck (move towards closest obstacle)


    //defend strategy
    //if enemy in sight -> shoot
    //advance towards center of gate
    //if last enemy move was shoot and health <= 25% duck (move towards closest obstacle)

    //determine if enemy in sight
    //determine direction to go
    //determine destination
    //set map stalingrad gate center
    public void shoot() {
        battleObserver.logEvent(String.format("Tank %s gives damage of %d", tank.getName(), tank.getDamage()));
        enemyTankConductor.receiveDamage(tank.getDamage());
    }

    public void advance() {
        battleObserver.logEvent(String.format("Tank %s advances", tank.getName()));
        //get gate direction
        //move in NSWE direction towards it
    }

    public void duck(Map map) {
        battleObserver.logEvent(String.format("Tank %s ducks", tank.getName()));
        //get nearest obstacle position
        //move in NSWE direction towards it
    }


    private void thinkAboutNextMove() {
        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void move(DIRECTION direction, int steps ) {
        switch (direction) {
            case EAST: {
                tank.getPosition().moveEast();
            }
            case WEST: {
                tank.getPosition().moveWest();
            }
            case NORTH:{
                tank.getPosition().moveNorth();
            }
            case SOUTH:{
                tank.getPosition().moveSouth();
            }
        }
    }


    public void receiveDamage(int damage) {
        tank.setHealth(tank.getHealth()-damage);
        battleObserver.logEvent(String.format("Tank %s receives damage of %d, new health at %d", tank.getName(), damage, tank.getHealth()));
        if(tank.isDead()) {
            die();
        }
    }

    private void die() {
        battleObserver.logEvent(String.format("Tank %s has died", tank.getName()));
    }

    public void setTank(Tank tank) {
        this.tank = tank;
        behavior = new TankBehavior();
    }

    public void setEnemyTankConductor(TankConductor enemy) { this.enemyTankConductor = enemy; }

    public void setBattleObserver(BattleObserver observer) {
        this.battleObserver = observer;
    }

    public Position getTankPosition() { return tank.getPosition(); }

    public void setMapHandler(MapHandler handler) { this.mapHandler = handler; }
}
