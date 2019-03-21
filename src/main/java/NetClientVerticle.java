import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetClientVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(NetClientVerticle.class);
    private final String socketId;

    public NetClientVerticle(String socketId) {
        this.socketId = socketId;
    }

    @Override
    public void start() {
        log.info("Starting client ");
        vertx.eventBus().consumer("TimeOfDay", this::handleTimeOfDay);
    }

    @Override
    public void stop() {
        log.info("Closing client socket " + socketId);
    }

    private void handleTimeOfDay(Message<String> msg) {
        Buffer buffer = Buffer.buffer(msg.body() + "\n");
        vertx.eventBus().publish(socketId, buffer);
    }
}
