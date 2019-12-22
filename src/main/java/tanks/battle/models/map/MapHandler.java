package tanks.battle.models.map;

import tanks.battle.utils.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapHandler {

    private Map map;

    public MapHandler(Map map) {
        this.map = map;
    }

    /**
     * Computes projectile distance between locations
     * If shorter distance we can use big damage gun
     * If longer distance we can use long range gun
     * If no path in horizantal, vertical or diagonal direction due to obstacles, return -1
     */
    public int getShotDistance(Position attackerTankPosition, Position victimTankPosition) {
        ArrayList<Position> projectileTrajectory = new ArrayList<>();
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
        for (int i = 0; i < movesCount-1; i++) {
            currentX += (i < diffX) ? stepX : 0;
            currentY += (i < diffY) ? stepY : 0;
            projectileTrajectory.add(new Position(currentX, currentY));
        }

        for (Position pair : projectileTrajectory) {
            if (map.getRows().get(pair.getX()).get(pair.getY())) {
                return -1;
            }
        }
        return movesCount;
    }

    /**
     * Returns path from source location to destination
     */
    public List<Position> getPath(Map map, Position tankLocation, Position enemyLocation) {
        List<Position> currentPath = new ArrayList<>();
        boolean[][] mapMatrix = convertMapToMatrix(map);
        //adding 1 to tank location because map matrix contains border cells
        currentPath = getPath(mapMatrix, tankLocation, currentPath, enemyLocation);
        return currentPath;
    }

    /**
     * Recursively construct path
     */
    private List<Position> getPath(boolean[][] mapMatrix, Position location, List<Position> currentPath, Position enemyLocation) {
        if (location.equals(enemyLocation)) {
            return currentPath;
        }
        // check if cell is a boundary, obstacle or has been visited
        if (!mapMatrix[location.getX() + 1][location.getY() + 1]) {
            // make the cell visited and adding 1 to tank location because map matrix contains border cells
            mapMatrix[location.getX() + 1][location.getY() + 1] = true;
            currentPath.add(location);
            // traverse NSWE
            if (getPath(mapMatrix, location.moveNorth(), currentPath, enemyLocation).size() > 0) {
                return currentPath;
            }
            if (getPath(mapMatrix, location.moveSouth(), currentPath, enemyLocation).size() > 0) {
                return currentPath;
            }
            if (getPath(mapMatrix, location.moveWest(), currentPath, enemyLocation).size() > 0) {
                return currentPath;
            }
            if (getPath(mapMatrix, location.moveEast(), currentPath, enemyLocation).size() > 0) {
                return currentPath;
            }
        }
        //we don't want all visited cells, just the ones that make up the path
        currentPath.remove(location);
        return Collections.emptyList(); // no currentPath has been found
    }

    /**
     * Finds nearest obstacle on map and returns the hide position for it,
     * depending on the tank orientation
     */
    public Position getNearestObstacle(Map map, Position location, boolean forward) {
        boolean[][] mapMatrix = convertMapToMatrix(map);
        int vLimit = map.getHeight();
        int hLimit = map.getWidth();
        boolean[][] visited = new boolean[mapMatrix.length][mapMatrix[0].length];
        return getNeareastObstacle(mapMatrix, visited, vLimit, hLimit, location, forward);
    }

    /**
     * Recursive method for obstacle find
     */
    private Position getNeareastObstacle(boolean[][] mapMatrix, boolean[][]visited, int vLimit, int hLimit, Position location, boolean forward) {
        if (location.getX()<0 || location.getX() >= vLimit || location.getY()<0 || location.getY() >= hLimit || visited[location.getX() + 1][location.getY() + 1]) {
            return null;
        }
        if (mapMatrix[location.getX() + 1][location.getY() + 1]) {
            return (forward? location.moveForward(vLimit, hLimit) : location.moveBackward());
        } else {
            visited[location.getX() + 1][location.getY() + 1] = true;
            Position newLocation;
            if ((newLocation = getNeareastObstacle(mapMatrix, visited, vLimit, hLimit, location.moveNorth(), forward)) != null) {
                return newLocation;
            }
            if ((newLocation = getNeareastObstacle(mapMatrix, visited, vLimit, hLimit, location.moveSouth(), forward)) != null) {
                return newLocation;
            }
            if ((newLocation = getNeareastObstacle(mapMatrix, visited, vLimit, hLimit, location.moveWest(), forward)) != null) {
                return newLocation;
            }
            if ((newLocation = getNeareastObstacle(mapMatrix,visited,  vLimit, hLimit, location.moveEast(), forward)) != null) {
                return newLocation;
            }
            return null;
        }
    }

    /**
     * Converts a Map object to a 2-dim array. Marks edges and obstacles
     */
    private boolean[][] convertMapToMatrix(Map map) {
        //create visited matrix from map
        int height = map.getHeight() + 2;
        int width = map.getWidth() + 2;
        boolean[][] mapMatrix = new boolean[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //mark borders as visited
                if (i == 0 || i == height - 1 || j == 0 || j == width - 1) {
                    mapMatrix[i][j] = true;
                } else {
                    //mark visited if there is an obstacle
                    mapMatrix[i][j] = map.getRows().get(i - 1).get(j - 1);
                }
            }
        }
        return mapMatrix;
    }

    public Map getMap() { return  this.map; }
}
