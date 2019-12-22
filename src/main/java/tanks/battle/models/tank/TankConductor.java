package tanks.battle.models.tank;

import tanks.battle.models.battle.BattleLog;
import tanks.battle.models.map.MapHandler;
import tanks.battle.utils.MOVE;
import tanks.battle.utils.Position;

import java.util.List;

public class TankConductor {

    private TankConductor enemyTankConductor;
    private Tank tank;
    private MapHandler mapHandler;
    private BattleLog battleLog;
    private MOVE lastMove = MOVE.UNDEFINED;
    private List<Position> pathToEnemy;

    private static final int STEPS_COUNT = 5;

    public TankConductor(Tank tank, BattleLog battleLog) {
        this.tank = tank;
        this.battleLog = battleLog;
    }

    public void makeNextMove() {
        MOVE move = getNextMove();
        switch (move) {
            case SHOOT: {
                shoot();
                break;
            }
            case ADVANCE: {
                advanceToEnemy();
                break;
            }
            case DUCK:
                duck();
            default:{}
        }
    }

    /**
     * If tank is low on health and not already hiding, duck behind an obstacle
     * Otherwise, it conductor has clear shot to enemy tank, he shoots
     * If already hiding or no clear shot, advance towards enemy tank
     * @return next move in game
     */
    public MOVE getNextMove() {
        //slow down the game
        thinkAboutNextMove();
        //if low on health and not already ducking
        if (lastMove != MOVE.DUCK && tank.getHealth() < (tank.getMaxHealth() / 2)) {
            lastMove = MOVE.DUCK;
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

    /**
    * Gets path to enemy and takes 5 steps towards it and logs new position
     */
    public void advanceToEnemy() {
        pathToEnemy = mapHandler.getPath(mapHandler.getMap(), tank.getPosition(), enemyTankConductor.getTankPosition());
        if (pathToEnemy.size() == 0) {
            System.out.println(mapHandler.getMap().toStringWithTanks(tank.getPosition(), enemyTankConductor.getTankPosition()));
            battleLog.logEvent(String.format("%s cannot advance towards %s", tank, enemyTankConductor.getTankPosition()));
            return;
        }
        Position newPosition = pathToEnemy.get(Math.min(STEPS_COUNT, pathToEnemy.size() - 1));
        battleLog.logEvent(String.format("%s advances to %s", tank, newPosition));
        tank.setPosition(newPosition);
    }

    /**
       Finds nearest obstacle and advances to next to it
     */
    public void duck() {
        //get nearest obstacle position
        Position duckPosition = mapHandler.getNearestObstacle(mapHandler.getMap(), getTankPosition(), tank.isFacingForward());
        if (duckPosition != null) {
            battleLog.logEvent(String.format("%s ducks at position %s", tank, duckPosition));
            //move in NSWE direction towards it
            tank.setPosition(duckPosition);
        }
    }

    /**
     * Slow down between moves
     */
    private void thinkAboutNextMove() {
        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * If health below 0, tan k is dead
     */
    public void receiveDamage(int damage) {
        tank.setHealth(tank.getHealth() - damage);
        battleLog.logEvent(String.format(" %s health at %d", tank.getName(), tank.getHealth()));
        if (isDead()) {
            die();
        }
    }

    private void die() {
        battleLog.logEvent(String.format("Tank %s has died", tank.getName()));
    }
    public boolean isDead() {
        return tank.getHealth() <= 0;
    }

    public void setEnemyTankConductor(TankConductor enemy) {
        this.enemyTankConductor = enemy;
    }

    public Position getTankPosition() {
        return tank.getPosition();
    }

    public void setMapHandler(MapHandler handler) {
        this.mapHandler = handler;
    }
}
