package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tanks.Battle;
import tanks.Tank;

@RestController
@RequestMapping(value = "/gettankprop")
public class GetTankProperties {

  @RequestMapping(method= RequestMethod.GET)
  public Tank getTankDetails(@RequestParam(value="name", defaultValue="tank#0") String name) {
    return Battle.getTankByName(name);
  }
}
