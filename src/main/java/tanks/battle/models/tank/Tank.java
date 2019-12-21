package tanks.battle.models.tank;

import tanks.battle.utils.FACING;
import tanks.battle.utils.Position;

public  class Tank  {

    private String name;
    private int currentHealth;
    private int maxHealth;
    private int damage;
    private Position position;
    private FACING orientation;

    protected Tank() {}

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public String getName() {
        return name;
    }

    public void setHealth(int health) {
        this.currentHealth = health;
    }

    public void setMaxHealth(int health) {
        this.maxHealth = health;
    }

    public int getHealth() {
        return currentHealth;
    }

    public int getMaxHealth() { return maxHealth; }

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

    public FACING getOrientation() { return this.orientation; }

    public boolean isFacingForward() {
        return  getOrientation().equals(FACING.FORWARD);
    }

    public void setOrientation(FACING facing) {
        this.orientation = facing;
    }

    @Override
    public String toString() {
        return String.format("Tank %s [%d, %d]", name, position.getX(), position.getY());
    }
}
