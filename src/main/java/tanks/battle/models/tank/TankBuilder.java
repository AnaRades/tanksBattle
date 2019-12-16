package tanks.battle.models.tank;

import tanks.battle.models.tank.utils.FACING;
import tanks.battle.models.tank.utils.Position;

public class TankBuilder {
    private String name = "Default Tank";
    private int health = 100;
    private int damage = 5;
    private FACING facing;

    private Position position = new Position(0,0);

    public TankBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TankBuilder withHealth(int health) {
        this.health = health;
        return this;
    }

    public TankBuilder withDamage(int damage) {
        this.damage = damage;
        return this;
    }

    public TankBuilder withFacing(FACING facing) {
        this.facing = facing;
        return this;
    }

    public TankBuilder withPosition(Position position) {
        this.position = position;
        return this;
    }

    public Tank build() {
        Tank tank = new Tank();

        tank.setName(name);
        tank.setHealth(health);
        tank.setDamage(damage);
        tank.setPosition(position);

        return tank;
    }
}