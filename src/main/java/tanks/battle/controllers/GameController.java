package tanks.battle.controllers;

import org.springframework.web.bind.annotation.*;
import tanks.Game;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/startgame")
@CrossOrigin(origins = "*")
public class GameController {

    @ResponseBody
    @RequestMapping(method= RequestMethod.POST)
    public String startGame(HttpSession session) {
        return Game.startGame(session.getId());
    }

}

