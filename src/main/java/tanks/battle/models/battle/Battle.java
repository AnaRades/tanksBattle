package tanks.battle.models.battle;

import java.util.HashMap;

import tanks.battle.models.map.MapHandler;
import tanks.battle.models.tank.*;
import tanks.battle.models.map.Map;
import tanks.battle.models.map.Row;
import tanks.battle.models.tank.utils.FACING;
import tanks.battle.models.tank.utils.MOVE;
import tanks.battle.models.tank.utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Battle extends Thread {

    private Map stalingradMap;

    private TankConductor germanTankConductor;
    private TankConductor sovietTankConductor;


    private Tank soviet;
    private Tank panzer;

    private BattleObserver observer;
    private String id;

    private static int BATTLE_COUNT = 0;
    private static HashMap<String, Battle> currentBattles = new HashMap<>();
    //TODO: create static list of battles
    //add id to battle

    public Battle() {
        id = "Battle-"+BATTLE_COUNT;
        BATTLE_COUNT++;
        observer = new BattleObserver();
    }


    //to be replaced by a rest controller mapping
    //will be one of the 3 commands it receives from the client
    //get tank properties, get map, start battle
    public static void main(String[] args) {
        Battle battle = new Battle();
        battle.init();
        battle.startBattle();
    }

    public void init() {

        currentBattles.put(id, this);
        stalingradMap = getStalingradMap();

        TankBuilder tankBuilder = new TankBuilder();
        soviet = tankBuilder.withName("Soviet").withDamage(30).withHealth(70)
                .withFacing(FACING.BACKWARDS).withPosition(new Position(10, 7)).build();

        tankBuilder = new TankBuilder();
        panzer = tankBuilder.withName("Panzer").withDamage(40).withHealth(90)
                .withFacing(FACING.FORWARD).withPosition(new Position(2,7)).build();

        germanTankConductor = new TankConductor();
        germanTankConductor.setTank(panzer);

        sovietTankConductor = new TankConductor();
        sovietTankConductor.setTank(soviet);

        germanTankConductor.setEnemyTankConductor(sovietTankConductor);
        sovietTankConductor.setEnemyTankConductor(germanTankConductor);

        germanTankConductor.setBattleObserver(observer);
        sovietTankConductor.setBattleObserver(observer);

        MapHandler mapHandler = new MapHandler(stalingradMap);
        germanTankConductor.setMapHandler(mapHandler);
        sovietTankConductor.setMapHandler(mapHandler);
    }
    public void startBattle() {
        try {
            this.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Start battle");
        boolean isSovietTurn = true;
        TankConductor currentTank;
        while(!soviet.isDead() && !panzer.isDead()) {
            currentTank = isSovietTurn? sovietTankConductor: germanTankConductor;

//          tank1 makes move1 -> roll dice -> if success make move, else do nothing
            MOVE move = currentTank.makeNextMove();
            if(rollDice()) {
//          if move = shoot -> tank1.shoot(tank2)
                if (move.equals(MOVE.SHOOT)) {
                    currentTank.shoot();
                } else if (move.equals(MOVE.ADVANCE)) {
                    currentTank.advance();
                }
                observer.logEvent("Move was unsuccessfull");
            }
//            System.out.println(observer.getLatestLog());
            isSovietTurn = !isSovietTurn;
        }
    }

    private static  Random random = new Random(100);
    private static boolean rollDice() {
        return random.nextInt()%5 < 4;
    }

    /*static getters to be replaced by retrieve from DB*/
    public static List<String> getTankList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Soviet");
        list.add("Panzer");
        return list;
    }

    public static Tank getTankByName(String name) {
        TankBuilder tankBuilder = new TankBuilder();
        if(name.equalsIgnoreCase("Panzer")) {
            return tankBuilder.withName("Panzer").withDamage(4).withHealth(90).withFacing(FACING.FORWARD)
                .withPosition(new Position(2,7)).build();
        }
        return  tankBuilder.withName("Soviet").withDamage(6).withHealth(70)
            .withFacing(FACING.BACKWARDS).withPosition(new Position(10, 7))
            .build();
    }

    public static Map getStalingradMap() {
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
        Map stalingradMap = new Map(rows);
        System.out.println("\n"+stalingradMap+"\n");
        return stalingradMap;
    }

    public String getLatestLog() {
        return  observer.getLatestLog();
    }

    public String getBattleId() {
        return id;
    }

    public static Battle getBattleById(String id) {
        return currentBattles.get(id);
    }
}
