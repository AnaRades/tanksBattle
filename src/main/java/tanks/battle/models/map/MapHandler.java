package tanks.battle.models.map;

import tanks.battle.models.tank.utils.Position;

import java.util.ArrayList;

public class MapHandler {

    private Map map;


    public MapHandler(Map map) {
        this.map = map;
    }

    public boolean clearShot(Position attackerTankPosition, Position victimTankPosition) {
        ArrayList<Pair> projectileTrajectory = new ArrayList<>();
        int diffX = Math.abs(attackerTankPosition.getX() - victimTankPosition.getX());
        int diffY = Math.abs(attackerTankPosition.getY() - victimTankPosition.getY());

        int stepX = attackerTankPosition.getX() < victimTankPosition.getX()? 1:-1;
        int stepY = attackerTankPosition.getY() < victimTankPosition.getY()? 1:-1;

        int currentX = attackerTankPosition.getX();
        int currentY = attackerTankPosition.getY();

        int movesCount = Math.max(diffX, diffY);
        for(int i=0; i<movesCount; i++) {
            currentX += (i < diffX) ? stepX:0;
            currentY += (i < diffY) ? stepY:0;
            projectileTrajectory.add(new Pair(currentX, currentY));
        }

//        System.out.println("Start = " + attackerTankPosition.x() + ", " + attackerTankPosition.y());
//        System.out.println("End = " + victimTankPosition.x() + ", " + victimTankPosition.y());
        for(Pair pair : projectileTrajectory) {
//            System.out.println("X = " + pair.x + " Y = " + pair.y);
            if(map.getRows().get(pair.x).get(pair.y)) {
                System.out.println("Obstacle found in path");
                return false;
            }
        }
//        System.out.println("Final = " + victimTankPosition.x() + ", " + victimTankPosition.y());
//        System.out.println("Clear shot");
        return true;
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
