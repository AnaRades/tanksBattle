package tanks.battle.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tanks.battle.engine.Battle;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

@Service
@EnableScheduling
public class GameNotificationService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();
    private Battle battle;

    public void addEmitter(final SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void removeEmitter(final SseEmitter emitter) {
        emitters.remove(emitter);
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }


    @Async
    @Scheduled(fixedRate = 2000)
    public void doNotify() {
        if(battle.isGameOver()) {
            stopService();
            return;
        }
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().data(battle.getLatestLog()));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }

    public void stopService() {
        try {
            scheduledTasks.get(this).cancel(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Bean
    public TaskScheduler poolScheduler() {
        return new CustomTaskScheduler();
    }


    class CustomTaskScheduler extends ThreadPoolTaskScheduler {

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
            ScheduledFuture<?> future = super.scheduleAtFixedRate(task, period);

            ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
            scheduledTasks.put(runnable.getTarget(), future);

            return future;
        }

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
            ScheduledFuture<?> future = super.scheduleAtFixedRate(task, startTime, period);

            ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
            scheduledTasks.put(runnable.getTarget(), future);

            return future;
        }
    }

}
