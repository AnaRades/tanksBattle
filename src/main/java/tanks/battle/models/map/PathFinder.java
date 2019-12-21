package tanks.battle.models.map;

import tanks.battle.engine.Battle;
import tanks.battle.utils.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathFinder {

    public static void main(String[] args) {
        Map map = Battle.generateRandomMap(10, 43);
        Position start = new Position(2, 13);
        Position end = new Position(6, 35);

        System.out.println(map.toStringWithTanks(start, end));

        PathFinder pathFinder = new PathFinder();
        List<Position> path = pathFinder.getPath(map, start, end);
        System.out.println("==========================Path size " + path.size() + "=========================");
        for (Position position : path) {
            System.out.println(position);
        }
    }

    public List<Position> getPath(Map map, Position tankLocation, Position enemyLocation) {
        List<Position> currentPath = new ArrayList<>();
        boolean[][] mapMatrix = convertMapToMatrix(map);
        //adding 1 to tank location because map matrix contains border cells
        currentPath = getPath(mapMatrix, tankLocation, currentPath, enemyLocation);
        return currentPath;
    }

    private static List<Position> getPath(boolean[][] mapMatrix, Position location, List<Position> currentPath, Position enemyLocation) {
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
        currentPath.remove(location);
        return Collections.emptyList(); // no currentPath has been found
    }

    public Position getNearestObstacle(Map map, Position location, boolean forward) {
        boolean[][] mapMatrix = convertMapToMatrix(map);
        int vLimit = map.getHeight();
        int hLimit = map.getWidth();
        boolean[][] visited = new boolean[mapMatrix.length][mapMatrix[0].length];
        return getNeareastObstacle(mapMatrix, visited, vLimit, hLimit, location, forward);
    }

    //add visited
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
}