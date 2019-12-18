package tanks.battle.models.tank;

import tanks.battle.models.battle.BattleObserver;
import tanks.battle.models.map.Map;
import tanks.battle.models.tank.utils.FACING;
import tanks.battle.models.tank.utils.MOVE;
import tanks.battle.models.tank.utils.Position;

public  class Tank  {

    //TODO: relate tank to SSE
    //instead of sout, add to SSE

    private String tankName;
    private int health;
    private int damage;

    private Position position;

    private TankBehavior behavior;
    private Tank otherTank;
    private FACING facing;

    private BattleObserver battleObserver;


    private enum DIRECTION {
        EAST,
        WEST,
        NORTH,
        SOUTH
    }

    protected Tank() {
        behavior = new TankBehavior();
    }

    public MOVE makeNextMove(Map map) {
        thinkAboutNextMove();
        //if clear shot, then shoot
        if(map.clearShot(this.position, otherTank.position)) {
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
                this.position.moveEast();
            }
            case WEST: {
                this.position.moveWest();
            }
            case NORTH:{
                this.position.moveNorth();
            }
            case SOUTH:{
                this.position.moveSouth();
            }
        }
    }

    public void shoot() {
        battleObserver.logEvent(String.format("Tank %s gives damage of %d", tankName, damage));
        otherTank.receiveDamage(damage);
    }

    public void advance() {
        //get gate direction
        //move in NSWE direction towards it
    }

    public void duck(Map map) {
        //get nearest obstacle position
        //move in NSWE direction towards it
    }

    public void receiveDamage(int damage) {
        this.health -= damage;
        battleObserver.logEvent(String.format("Tank %s receives damage of %d, new health at %d", tankName, damage, health));
        if(isDead()) {
            die();
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public String getTankName() {
        return tankName;
    }

    private void die() {

        System.out.format("Tank %s has died", tankName);

        battleObserver.logEvent(String.format("Tank %s has died", tankName));
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setName(String name) {
        this.tankName = name;
    }

    public void setOtherTank(Tank tank) {
        this.otherTank = tank;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setBattleObserver(BattleObserver observer) {
        this.battleObserver = observer;
    }
}
