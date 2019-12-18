package tanks.battle.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tanks.battle.models.battle.Battle;

@RestController
@RequestMapping(value = "/battle")
@CrossOrigin(origins = "*")
public class BattleController {

	@RequestMapping(method= RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public String getBattleUpdates(
			@RequestHeader(value = "Content-Type", defaultValue = "text/event-stream") String contentType) {


		/*Battle battle = Battle.getBattleById(id);
		if(battle == null) {
			return "500";
		}*/

		return "banana1;banana2";
		/*String response = String.format("data: %s\n", battle.getLatestLog());

		System.out.println("Sending to server: " + response.substring(6));


		return response;*/
	}
}
