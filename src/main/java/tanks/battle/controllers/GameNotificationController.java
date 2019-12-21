package tanks.battle.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tanks.battle.engine.Battle;

@RestController
@RequestMapping("/gamenotification")
@CrossOrigin(origins = "*")
public class GameNotificationController {

    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<SseEmitter> subscribeToBattleEvents(@RequestParam(value = "id", defaultValue = "battle-0") String id,
            @RequestHeader(value = "Content-Type", defaultValue = "text/event-stream") String contentType) {
        final SseEmitter emitter = new SseEmitter();
        Battle battle = Battle.getBattleById(id);
        if(battle == null) {
            return new ResponseEntity<>(emitter, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        battle.setEmitter(emitter);

        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public String handleHttpMediaTypeNotAcceptableException() {
        return "acceptable MIME type:" + MediaType.TEXT_EVENT_STREAM_VALUE;
    }

}