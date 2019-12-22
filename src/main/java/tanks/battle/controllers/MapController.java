package tanks.battle.controllers;

import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tanks.Game;
import tanks.battle.models.map.Map;
import tanks.mongo.MapRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/getmap")
@CrossOrigin(origins = "*")
public class MapController {

    @Autowired
    MapRepository mapRepository;

    @RequestMapping(method= RequestMethod.GET)
    public String displayMap(HttpSession session) {
        if(Game.getBattleBySessionId(session.getId()) != null) {
            String mapId = Game.getBattleBySessionId(session.getId()).getStalingradMap().getId();
            return mapRepository.findById(mapId).get().toString();
        }
        return mapRepository.findAll().get(0).toString();
    }
}
