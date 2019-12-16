package map;

import java.util.ArrayList;

public class Row extends ArrayList<Boolean> {

    public Row(boolean[] row) {
        for(boolean cell : row) {
            this.add(cell);
        }
    }

}
