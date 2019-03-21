import io.vertx.core.AbstractVerticle;

import java.time.LocalDateTime;

public class TimeOfDayVerticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.setPeriodic(1000, (__) -> {
            vertx.eventBus().publish("TimeOfDay", LocalDateTime.now().toString());
        });
    }
}
