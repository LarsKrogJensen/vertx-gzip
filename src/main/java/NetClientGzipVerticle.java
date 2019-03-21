import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class NetClientGzipVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(NetClientGzipVerticle.class);
    private final Channel channel;

    public NetClientGzipVerticle(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void start() {
        log.info("Starting client ");
        vertx.eventBus().consumer("TimeOfDay", this::handleTimeOfDay);
    }

    @Override
    public void stop() {
        channel.close();
        log.info("Closing client socket ");
    }

    private void handleTimeOfDay(Message<String> msg) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeCharSequence(msg.body() + "\n", Charset.forName("UTF-8"));
        channel.writeAndFlush(buffer);
    }
}
