package tanks.battle.models.map;

import tanks.battle.utils.Position;

import java.util.ArrayList;

public class MapHandler {

    private Map map;


    public MapHandler(Map map) {
        this.map = map;
    }

    public Map getMap() { return  this.map; }

    public int getShotDistance(Position attackerTankPosition, Position victimTankPosition) {
        ArrayList<Pair> projectileTrajectory = new ArrayList<>();
        int diffX = Math.abs(attackerTankPosition.getX() - victimTankPosition.getX());
        int diffY = Math.abs(attackerTankPosition.getY() - victimTankPosition.getY());

        //projectile can only go NSWE
        if (diffX > 0 && diffY > 0 && diffX != diffY) {
            return -1;
        }

        int stepX = attackerTankPosition.getX() < victimTankPosition.getX() ? 1 : -1;
        int stepY = attackerTankPosition.getY() < victimTankPosition.getY() ? 1 : -1;

        int currentX = attackerTankPosition.getX();
        int currentY = attackerTankPosition.getY();

        int movesCount = Math.max(diffX, diffY);
        for (int i = 0; i < movesCount; i++) {
            currentX += (i < diffX) ? stepX : 0;
            currentY += (i < diffY) ? stepY : 0;
            projectileTrajectory.add(new Pair(currentX, currentY));
        }

        for (Pair pair : projectileTrajectory) {
            if (map.getRows().get(pair.x).get(pair.y)) {
                return -1;
            }
        }
        return movesCount;
    }

    private class Pair {
        int x;
        int y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
