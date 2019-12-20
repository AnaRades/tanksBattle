package tanks.battle.models.tank.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tanks.battle.models.battle.Battle;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/gamenotification")
@CrossOrigin(origins = "*")
public class GameNotificationController {

    @Autowired
    GameNotificationService service;

    final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<SseEmitter> doNotify(@RequestParam(value = "id", defaultValue = "battle-0") String id,
            @RequestHeader(value = "Content-Type", defaultValue = "text/event-stream") String contentType) {
        final SseEmitter emitter = new SseEmitter();
        Battle battle = Battle.getBattleById(id);
        service.addEmitter(emitter);
        service.setBattle(battle);
        service.doNotify();
        emitter.onCompletion(() -> service.removeEmitter(emitter));
        emitter.onTimeout(() -> service.removeEmitter(emitter));
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public String handleHttpMediaTypeNotAcceptableException() {
        return "acceptable MIME type:" + MediaType.TEXT_EVENT_STREAM_VALUE;
    }

}