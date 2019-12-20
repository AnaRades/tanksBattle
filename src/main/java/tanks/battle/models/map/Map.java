package tanks.battle.models.map;

import org.springframework.data.annotation.Id;
import tanks.battle.models.tank.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Map {
    @Id
    String id;
    List<Row> rows;
    private static int COUNTER = 0;


//TODO: Position gatePosition;

    public Map() { }

    public Map(List<Row> rows) {
        if(rows == null) {
            System.out.println("Empty map!");
        }
        this.rows = rows;
        this.id = "map-"+COUNTER;
        COUNTER++;
    }

    public List<Row> getRows() {
        return this.rows;
    }

    public int getHeight() {
        return rows.size();
    }

    public int getWidth() {
        return rows.get(0).size();
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

}
