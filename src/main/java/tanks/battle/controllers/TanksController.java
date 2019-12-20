package tanks.battle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tanks.battle.models.battle.Battle;
import tanks.battle.models.tank.Tank;
import tanks.mongo.TankRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TanksController {

  @Autowired
  TankRepository tankRepository;

  @RequestMapping(value = "/gettanks", method= RequestMethod.GET)
  public List<String> getTanksList() {
    return tankRepository.findAll().stream().map(Tank::getName).collect(Collectors.toList());
  }

  @RequestMapping(value = "/gettanks/{name}", method= RequestMethod.GET)
  public Tank getTankDetails(@PathVariable(value="name") String name) {
    return tankRepository.findByName(name);
  }

}
