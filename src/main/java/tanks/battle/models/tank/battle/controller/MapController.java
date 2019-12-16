package tanks.battle.models.tank.battle.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tanks.battle.models.battle.Battle;

@RestController
@RequestMapping("/map")
public class MapController {

    @RequestMapping(method= RequestMethod.GET)
    public String displayMap() {
        return Battle.getStalingradMap().toString();
    }
}
