package tanks.battle.models.tank;

import tanks.battle.engine.BattleLog;
import tanks.battle.models.map.MapHandler;
import tanks.battle.models.map.PathFinder;
import tanks.battle.utils.Constants;
import tanks.battle.utils.MOVE;
import tanks.battle.utils.Position;

import java.util.Collections;
import java.util.List;

public class TankConductor {

    private TankConductor enemyTankConductor;
    private Tank tank;
    private MapHandler mapHandler;
    private BattleLog battleLog;

    MOVE lastMove = null;
    public MOVE makeNextMove() {
        //slow down the game
        thinkAboutNextMove();
        //if low on health and not already ducking
        if(lastMove != MOVE.DUCK && tank.getHealth() < (tank.getMaxHealth()/2)) {
            lastMove =  MOVE.DUCK;
        } else {
            //if clear shot, then shoot
            int shotDistance = mapHandler.getShotDistance(tank.getPosition(), enemyTankConductor.getTankPosition());
            boolean clearShot = shotDistance > 0;
            if (clearShot) {
                lastMove = MOVE.SHOOT;
            } else {
                lastMove = MOVE.ADVANCE;
            }
        }
        return lastMove;
    }
    public void shoot() {
        battleLog.logEvent(String.format("%s gives damage of %d", tank, tank.getDamage()));
        enemyTankConductor.receiveDamage(tank.getDamage());
    }

    private List<Position> pathToEnemy = Collections.emptyList();
    private PathFinder pathFinder = new PathFinder();

    public void advanceToEnemy() {
        pathToEnemy = pathFinder.getPath(mapHandler.getMap(), tank.getPosition(), enemyTankConductor.getTankPosition());
        if(pathToEnemy.size() == 0) {
            System.out.println(mapHandler.getMap().toStringWithTanks(tank.getPosition(), enemyTankConductor.getTankPosition()));
            battleLog.logEvent(String.format("%s cannot advance towards %s", tank, enemyTankConductor.getTankPosition()));
            return;
        }
        Position newPosition =pathToEnemy.get(Math.min(5, pathToEnemy.size()-1));
        battleLog.logEvent(String.format("%s advances to %s", tank, newPosition));
        tank.setPosition(newPosition);
    }

    public void duck() {
        //get nearest obstacle position
        Position duckPosition = pathFinder.getNearestObstacle(mapHandler.getMap(), getTankPosition(), tank.getName().equalsIgnoreCase(Constants.PANZER));
        if(duckPosition != null) {
            battleLog.logEvent(String.format("%s ducks at position %s", tank, duckPosition));
            //move in NSWE direction towards it
            tank.setPosition(duckPosition);
        }
    }


    private void thinkAboutNextMove() {
        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void receiveDamage(int damage) {
        tank.setHealth(tank.getHealth()-damage);
        battleLog.logEvent(String.format(" %s health at %d", tank.getName(), tank.getHealth()));
        if(tank.isDead()) {
            die();
        }
    }

    private void die() {
        battleLog.logEvent(String.format("Tank %s has died", tank.getName()));
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }

    public void setEnemyTankConductor(TankConductor enemy) { this.enemyTankConductor = enemy; }

    public void setBattleLog(BattleLog observer) {
        this.battleLog = observer;
    }

    public Position getTankPosition() { return tank.getPosition(); }

    public void setMapHandler(MapHandler handler) { this.mapHandler = handler; }
}
