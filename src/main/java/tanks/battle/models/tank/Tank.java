package tanks.battle.models.tank;

import tanks.battle.models.battle.BattleObserver;
import tanks.battle.models.map.Map;
import tanks.battle.models.tank.utils.FACING;
import tanks.battle.models.tank.utils.MOVE;
import tanks.battle.models.tank.utils.Position;

public  class Tank  {

    private String name;
    private int health;
    private int damage;
    private Position position;
    private FACING facing;

    protected Tank() {}

    public boolean isDead() {
        return health <= 0;
    }

    public String getName() {
        return name;
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
        this.name = name;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() { return this.position; }
}
