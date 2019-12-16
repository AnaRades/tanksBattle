package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/battle")
public class BattleController {

	@RequestMapping(method= RequestMethod.GET)
	@ResponseBody
	public String test() {
		return "test.html";
	}
}
