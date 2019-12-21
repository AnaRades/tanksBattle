package tanks.battle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tanks.battle.engine.Battle;
import tanks.battle.models.map.Map;
import tanks.battle.models.tank.Tank;
import tanks.battle.utils.Constants;
import tanks.battle.utils.Position;
import tanks.mongo.MapRepository;
import tanks.mongo.TankRepository;

@RestController
@RequestMapping(value = "/startgame")
public class GameController {

    @Autowired
    MapRepository mapRepository;

    @Autowired
    TankRepository tankRepository;

    @ResponseBody
    @RequestMapping(method= RequestMethod.POST)
    public String startGame() {
        //TODO: keep map of sessionId / battle. If battle already started return it's ID

        Tank panzer = tankRepository.findByName(Constants.PANZER);
        Tank soviet = tankRepository.findByName(Constants.SOVIET);

        panzer.setPosition(new Position(2,3));
        soviet.setPosition(new Position(7,36));

        Map stalingradMap = mapRepository.findById("map-1").get();

        Battle battle = new Battle(panzer, soviet, stalingradMap);
        battle.init();
        battle.startBattle();

        return battle.getBattleId();
    }

    public static void main(String[] args) {
        Battle battle = new Battle();
        battle.mockData();
        battle.init();
        battle.startBattle();

        Battle battle2 = new Battle();
        battle2.mockData();
        battle2.init();
        battle2.startBattle();
    }

}

