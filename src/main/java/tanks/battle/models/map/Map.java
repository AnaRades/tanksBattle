package tanks.battle.models.map;

import tanks.battle.models.tank.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Map {
    int rowCount;
    int columnCount;
    List<Row> rows;

    Position gatePosition;

    public Map(List<Row> rows) {
        if(rows == null) {
            //throw error
        }
        this.rows = rows;
        this.rowCount = rows.size();
        this.columnCount = rows.get(0).size();
    }

    public int height() {
        return this.rowCount;
    }

    public int width() {
        return this.columnCount;
    }

    public boolean clearShot(Position attackerTankPosition, Position victimTankPosition) {
        ArrayList<Pair> projectileTrajectory = new ArrayList<>();
        int diffX = Math.abs(attackerTankPosition.x() - victimTankPosition.x());
        int diffY = Math.abs(attackerTankPosition.y() - victimTankPosition.y());

        int stepX = attackerTankPosition.x() < victimTankPosition.x()? 1:-1;
        int stepY = attackerTankPosition.y() < victimTankPosition.y()? 1:-1;

        int currentX = attackerTankPosition.x();
        int currentY = attackerTankPosition.y();

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
            if(rows.get(pair.x).get(pair.y)) {
                System.out.println("Obstacle found in path");
                return false;
            }
        }
//        System.out.println("Final = " + victimTankPosition.x() + ", " + victimTankPosition.y());
//        System.out.println("Clear shot");
        return true;
    }

    @Override
    public String toString() {
        StringBuilder mapStr = new StringBuilder();

        //top border
        String hborder = new String(new char[rows.get(0).size()*2+2]).replace("\0", "_");
        mapStr.append(hborder);
        mapStr.append("\n");

        //add cells
        for(Row row : rows) {
            //left border
            mapStr.append("|");
            for(boolean isObstacle : row) {
                mapStr.append(isObstacle?"O":" ").append("|");
            }
            mapStr.append("\n");
        }

        //bottom border
        mapStr.append(hborder);

        return mapStr.toString();
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
