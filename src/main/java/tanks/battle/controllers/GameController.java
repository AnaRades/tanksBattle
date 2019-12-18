package tanks.battle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tanks.battle.models.battle.Battle;

@RestController
@RequestMapping(value = "/startgame")
public class GameController {


    @ResponseBody
    @RequestMapping(method= RequestMethod.POST)
    public String startGame() {
        //TODO: keep map of sessionId / battle
        Battle battle = new Battle();
        battle.init();
        battle.startBattle();

        return battle.getBattleId();
    }

}

