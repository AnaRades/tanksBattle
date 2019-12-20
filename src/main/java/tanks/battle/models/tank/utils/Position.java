package tanks.battle.models.tank.utils;

public class Position {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveEast() {
        x++;
    }

    public void moveWest() {
        x--;
    }

    public void moveNorth() {
        y++;
    }

    public void moveSouth() {
        y--;
    }
}
