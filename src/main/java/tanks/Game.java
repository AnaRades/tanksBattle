package tanks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import tanks.battle.models.battle.Battle;
import tanks.battle.models.map.Map;
import tanks.battle.models.tank.Tank;
import tanks.battle.utils.Constants;
import tanks.mongo.MapRepository;
import tanks.mongo.TankRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Game {

    private static final HashMap<String, Battle> battleMap = new HashMap<>();
    private static Game instance = null;

    private static Tank sovietTank, panzerTank;
    private static List<Map> mapList;
    private int mapIndex;

    public Game() {
        mapIndex = 0;
    }

    private static Game getInstance() {
        if(instance == null) {
            //throw not initialized exception
        }
        return instance;
    }

    public static void init(Tank panzer, Tank soviet, List<Map> maps) {
        if(instance == null) {
            instance = new Game();
        }
        panzerTank = panzer;
        sovietTank = soviet;
        mapList = new ArrayList<>(maps);
    }

    /*
     * If battle for this client started, just return it's id
     */
    public static String startGame(String sessionId) {
        Battle battle;
        if(!battleMap.containsKey(sessionId)) {
            battle = getInstance().createBattle(sessionId);
            battle.startBattle();
            battleMap.put(sessionId, battle);
        }

        return battleMap.get(sessionId).getBattleId();
    }

    public static void gameOver(Battle battle) {
        battleMap.remove(battle.getSessionId());
    }

    public static Battle getBattleBySessionId(String id) {
        return battleMap.get(id);
    }

    private Battle createBattle(String sessionId) {
        if(mapIndex >=mapList.size()) {
            mapIndex = 0;
        }
        Map stalingradMap = mapList.get(mapIndex++);
        Battle battle = new Battle(sessionId, panzerTank.clone(), sovietTank.clone(), stalingradMap);
        battleMap.put(sessionId, battle);

        return battleMap.get(sessionId);
    }

}
