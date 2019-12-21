package tanks.battle.engine;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tanks.battle.models.map.Map;
import tanks.battle.models.map.MapHandler;
import tanks.battle.models.map.Row;
import tanks.battle.models.tank.Tank;
import tanks.battle.models.tank.TankBuilder;
import tanks.battle.models.tank.TankConductor;
import tanks.battle.utils.FACING;
import tanks.battle.utils.MOVE;
import tanks.battle.utils.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Controller
public class Battle extends Thread {

    private Map stalingradMap;

    private TankConductor germanTankConductor;
    private TankConductor sovietTankConductor;

    private Tank soviet;
    private Tank panzer;

    private BattleLog observer;
    private String id;

    private static int BATTLE_COUNT = 0;
    private static HashMap<String, Battle> currentBattles = new HashMap<>();

    SseEmitter emitter;
    //TODO: create static list of battles
    //add id to battle

    private boolean isTest;
    public Battle() {
        isTest = true;
        id = "Battle-"+BATTLE_COUNT;
        BATTLE_COUNT++;
    }

    public Battle(Tank panzer, Tank soviet, Map stalingradMap) {
        isTest = false;
        id = "Battle-"+BATTLE_COUNT;
        BATTLE_COUNT++;

        this.panzer = panzer;
        this.soviet = soviet;
        setStalingradMap(stalingradMap);
    }

    public static void main(String[] args) {
        Battle battle = new Battle();
        battle.mockData();
        battle.init();
        battle.startBattle();
    }

    public void mockData() {
        TankBuilder tankBuilder = new TankBuilder();
        soviet = tankBuilder.withName("Soviet").withDamage(7).withHealth(70)
                .withFacing(FACING.BACKWARDS).withPosition(new Position(8, 37)).build();

        tankBuilder = new TankBuilder();
        panzer = tankBuilder.withName("Panzer").withDamage(10).withHealth(90)
                .withFacing(FACING.FORWARD).withPosition(new Position(2,7)).build();
        //TODO: refactor
        germanTankConductor = new TankConductor();
        germanTankConductor.setTank(panzer);

        sovietTankConductor = new TankConductor();
        sovietTankConductor.setTank(soviet);

        setStalingradMap(generateRandomMap(10,40));
    }

    public void init() {
        currentBattles.put(id, this);
        this.observer = new BattleLog(id);
        //TODO: refactor
        germanTankConductor = new TankConductor();
        germanTankConductor.setTank(panzer);

        sovietTankConductor = new TankConductor();
        sovietTankConductor.setTank(soviet);

        germanTankConductor.setEnemyTankConductor(sovietTankConductor);
        sovietTankConductor.setEnemyTankConductor(germanTankConductor);

        germanTankConductor.setBattleLog(observer);
        sovietTankConductor.setBattleLog(observer);

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
        System.out.println("=====Start battle");
        boolean isSovietTurn = true;
        TankConductor currentTank;
        while(!isGameOver()) {
            currentTank = isSovietTurn? sovietTankConductor: germanTankConductor;
            MOVE move = currentTank.makeNextMove();
            switch (move) {
                case SHOOT: {
                    currentTank.shoot();
                    break;
                }
                case ADVANCE: {
                    currentTank.advanceToEnemy();
                    break;
                }
                case DUCK:
                    currentTank.duck();
                default:{}
            }
            isSovietTurn = !isSovietTurn;
            try {
                if(emitter != null)
                    emitter.send(SseEmitter.event().data(getLatestLog()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(isTest) {
                System.out.println(observer.getLatestLog());
            }
        }
        emitter.complete();
        System.out.println("=====End battle");
    }

    private static  Random random = new Random(System.currentTimeMillis());
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

    private static  Random dynamicRandom = new Random(System.currentTimeMillis());
    private static  Random staticRandom = new Random(100);
    public static Map generateRandomMap(int height, int width) {
        List<Row> rows = new ArrayList<>(height);

        for (int i = 0; i < height; i++) {
            Row row = new Row();
            for (int j = 0; j < width; j++) {
                row.add((staticRandom.nextInt(300)%20)>17);
            }
            rows.add(row);
        }
        return new Map(rows);
    }

    private void setStalingradMap(Map map) {
        this.stalingradMap = map;
        System.out.println(this.stalingradMap.toStringWithTanks(panzer.getPosition(), soviet.getPosition()));
    }

    public static Map getStalingradMap() {
        List<Row> rows = new ArrayList<>();

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
           return  isGameOver()?"Game over": observer.getLatestLog();
    }

    public boolean isGameOver() {
        return (soviet.isDead() || panzer.isDead());
    }

    public String getBattleId() {
        return id;
    }

    public void setEmitter(SseEmitter emitter) {
        this.emitter = emitter;
    }

    public static Battle getBattleById(String id) {
        return currentBattles.get(id);
    }
}
