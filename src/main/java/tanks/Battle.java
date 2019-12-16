package tanks;

import map.Map;
import map.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Battle {

    private static Map stalingradMap;
    private static Tank soviet;
    private static Tank panzer;

    //to be replaced by a rest controller mapping
    //will be one of the 3 commands it receives from the client
    //get tank properties, get map, start battle
    public static void main(String[] args) {


        init();
        start();
    }

    public static void init() {
        ArrayList<Row> rows = new ArrayList<>(11);

        boolean[] row1 =   new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
        boolean[] row2 =   new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
        boolean[]  row3 =  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
        boolean[]  row4 =  new boolean[]{true, true, true, true, true, false, false, false, false, true, true, true, false, false};
        boolean[]  row5 =  new boolean[]{true, true, true, true, true, false, false, false, false, false, true, true, false, false};
        boolean[]  row6 =  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
        boolean[]  row7 =  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
        boolean[]  row8 =  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
        boolean[]  row9 =  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
        boolean[]  row10 = new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
        boolean[]  row11=  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
        boolean[][] map = new boolean[][]{row1, row2, row3, row4, row5, row6, row7, row8, row9, row10, row11};

        for(int i=0; i<11; i++) {
            rows.add(new Row(map[i]));
        }

        stalingradMap = new Map(rows);
        TankBuilder tankBuilder = new TankBuilder();
        soviet = tankBuilder.withName("Soviet").withDamage(6).withHealth(70)
                .withFacing(FACING.BACKWARDS).withPosition(new Position(10, 7))
                .build();

        tankBuilder = new TankBuilder();
        panzer = tankBuilder.withName("Panzer").withDamage(4).withHealth(90).withFacing(FACING.FORWARD)
                .withPosition(new Position(2,7)).build();

        panzer.setOtherTank(soviet);
        soviet.setOtherTank(panzer);
    }

    private static void start() {
        System.out.println("Start battle");
        boolean isSovietTurn = true;
        Tank[] tankOrder = new Tank[2];
        while(!soviet.isDead() && !panzer.isDead()) {
            tankOrder[0] = isSovietTurn? soviet: panzer;
            tankOrder[1] = isSovietTurn? panzer: soviet;

//          tank1 makes move1 -> roll dice -> if success make move, else do nothing
            MOVE move = tankOrder[0].makeNextMove(stalingradMap);;
            if(rollDice()) {
//          if move = shoot -> tank1.shoot(tank2)
                if (move.equals(MOVE.SHOOT)) {
                    tankOrder[0].shoot();
                } else if (move.equals(MOVE.ADVANCE)) {

                }
            }
        isSovietTurn = !isSovietTurn;
        }
    }
    private static  Random random = new Random();
    private static boolean rollDice() {
        return random.nextBoolean();
    }

    public static List<String> getTankList() {
        ArrayList<String> list = new ArrayList<>();
        list.add(soviet.getTankName());
        list.add(panzer.getTankName());
        return list;
    }

    public static Tank getTankByName(String name) {
        if(name.equalsIgnoreCase("Panzer")) {
            return panzer;
        }
        return soviet;
    }

}
