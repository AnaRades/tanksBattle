package tanks.battle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tanks.mongo.MapRepository;

@RestController
@RequestMapping("/getmap")
public class MapController {

    @Autowired
    MapRepository mapRepository;

    @RequestMapping(method= RequestMethod.GET)
    public String displayMap() {
        return mapRepository.findById("map-0").toString();
    }
}
