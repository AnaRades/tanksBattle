package tanks.battle.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tanks.battle.models.battle.Battle;

import java.util.List;

@RestController
@RequestMapping(value = "/gettanks")
public class GetTanks {

  @RequestMapping(method= RequestMethod.GET)
  public List<String> getTanksList() {
    return Battle.getTankList();
  }
}
