package tanks.battle.models.tank.battle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tanks.battle.models.battle.Battle;

@Controller
@RequestMapping(value = "/battle")
public class BattleController {

	@RequestMapping(method= RequestMethod.POST)
	@ResponseBody
	public String startBattle() {
		Battle.start();
		return "test.html";
	}
}
