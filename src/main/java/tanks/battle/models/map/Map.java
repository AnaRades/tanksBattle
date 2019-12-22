package tanks.battle.models.map;

import org.springframework.data.annotation.Id;
import org.springframework.http.InvalidMediaTypeException;
import tanks.battle.utils.Position;

import java.util.List;

public class Map {
    @Id
    String id;
    List<Row> rows;

    private static int COUNTER = 0;

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

    /**
     * @return String representation of map with tank positions marked
     */
    public String toStringWithTanks(Position tank1, Position tank2) {
        StringBuilder mapStr = new StringBuilder();

        for(int i=0; i<rows.get(0).size(); i++) {
            mapStr.append("|").append(i);
        }
        mapStr.append("\n");

        //add cells
        for(int i=0; i<rows.size(); i++) {
            //left border
            mapStr.append("|");
            Row row = rows.get(i);
            for(int j=0; j<row.size(); j++) {
                if (tank1.hasCoordinates(i,j) || tank2.hasCoordinates(i,j)) {
                    mapStr.append("X").append(j>9?" ":"");
                } else {
                    mapStr.append(row.get(j)?"O":" ").append(j>9?" ":"");
                }
                //right border
                mapStr.append("|");
            }
            mapStr.append("\n");
        }

        for(int i=0; i<rows.get(0).size(); i++) {
            mapStr.append("|").append(i);
        }

        return mapStr.toString();
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

    public String getId() { return id; }
}
