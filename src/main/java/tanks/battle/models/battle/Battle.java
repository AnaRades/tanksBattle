package tanks.battle.models.battle;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tanks.Game;
import tanks.battle.models.map.Map;
import tanks.battle.models.map.MapHandler;
import tanks.battle.models.map.Row;
import tanks.battle.models.tank.Tank;
import tanks.battle.models.tank.TankBuilder;
import tanks.battle.models.tank.TankConductor;
import tanks.battle.utils.FACING;
import tanks.battle.utils.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Turn based game. Each player has their turn to make a move:
 * Either advance towards enemy, shoot enemy or hide from enemy
 */
public class Battle extends Thread {

    private String id;
    private String sessionId;
    private BattleLog battleLog;
    private Map stalingradMap;

    private TankConductor germanTankConductor;
    private TankConductor sovietTankConductor;

    private static int BATTLE_COUNT = 0;

    //used to periodically send game updates to client
    private SseEmitter sseEmitter;
    private boolean isTest;

    public Battle() {
        isTest = true;
        id = "Battle-"+BATTLE_COUNT++;
        this.battleLog = new BattleLog(id);
    }

    public Battle(String sessionId, Tank panzer, Tank soviet, Map stalingradMap) {
        id = "Battle-"+BATTLE_COUNT++;
        this.sessionId = sessionId;
        this.isTest = false;
        this.battleLog = new BattleLog(id);

        init(panzer, soviet, stalingradMap);
    }

    public static void main(String[] args) {
        Battle battle = new Battle();
        battle.mockData();
        battle.startBattle();
    }

    public void mockData() {
        TankBuilder tankBuilder = new TankBuilder();
        Tank soviet = tankBuilder.withName("Soviet").withDamage(7).withHealth(70)
                .withFacing(FACING.BACKWARDS).withPosition(new Position(8, 37)).build();

        tankBuilder = new TankBuilder();
        Tank panzer = tankBuilder.withName("Panzer").withDamage(10).withHealth(90)
                .withFacing(FACING.FORWARD).withPosition(new Position(2,7)).build();

        germanTankConductor = new TankConductor(panzer, battleLog);
        sovietTankConductor = new TankConductor(soviet, battleLog);

        germanTankConductor.setEnemyTankConductor(sovietTankConductor);
        sovietTankConductor.setEnemyTankConductor(germanTankConductor);

        MapHandler mapHandler = new MapHandler(stalingradMap);
        germanTankConductor.setMapHandler(mapHandler);
        sovietTankConductor.setMapHandler(mapHandler);

        setStalingradMap(generateRandomMap(10,40));
    }

    public void init(Tank soviet, Tank panzer, Map map) {
        setStalingradMap(map);
        //TODO: refactor
        germanTankConductor = new TankConductor(panzer, battleLog);
        sovietTankConductor = new TankConductor(soviet, battleLog);

        germanTankConductor.setEnemyTankConductor(sovietTankConductor);
        sovietTankConductor.setEnemyTankConductor(germanTankConductor);

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
        boolean isSovietTurn = true;
        TankConductor currentTank;
        //while both tanks are alive, make moves
        while(!isGameOver()) {
            currentTank = isSovietTurn? sovietTankConductor: germanTankConductor;
            currentTank.makeNextMove();
            try {
                //send latest battle log to clinet
                if(sseEmitter != null)
                    sseEmitter.send(SseEmitter.event().data(getLatestLog()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(isTest) {
                System.out.println(battleLog.getLatestLog());
            }

            isSovietTurn = !isSovietTurn;
        }
        //game over, end transmission
        sseEmitter.complete();
        Game.gameOver(this);
        System.out.println("=====End battle");
    }

    private static  Random random = new Random(System.currentTimeMillis());
    private static boolean rollDice() {
        return random.nextInt()%5 < 4;
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
    }

    public Map getStalingradMap() {
        return stalingradMap;
    }

    public String getLatestLog() {
           return  isGameOver()?"Game over": battleLog.getLatestLog();
    }

    public boolean isGameOver() {
        return (sovietTankConductor.isDead() || germanTankConductor.isDead());
    }

    public String getBattleId() {
        return id;
    }

    public String getSessionId() { return sessionId; }

    public void setSseEmitter(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }
}
