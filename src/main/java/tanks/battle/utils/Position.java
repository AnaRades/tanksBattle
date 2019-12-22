package tanks.battle.utils;

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

    public boolean hasCoordinates(int x, int y) {
        return (this.x == x && this.y == y);
    }

    public Position moveEast() {
        return new Position(x, y + 1);
    }

    public Position moveWest() {
        return new Position(x, y - 1);
    }

    public Position moveNorth() {
        return new Position(x - 1, y);
    }

    public Position moveSouth() {
        return new Position(x + 1, y);
    }

    /*
    * Utility methods to help the tank get unblocked
    * */
    public Position moveForward(int vLimit, int hLimit) {
        if (x < vLimit - 1) {
            return moveSouth();
        }
        if (y < hLimit - 1) {
            return moveEast();
        }
        return moveNorth();
    }

    public Position moveBackward() {
        if (x > 0) {
            return moveNorth();
        }
        if (y > 0) {
            return moveWest();
        }
        return moveSouth();
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public boolean equals(Object otherPosition) {
        if (!(otherPosition instanceof Position))
            return false;
        return (x == ((Position) otherPosition).x && y == ((Position) otherPosition).y);
    }
}
