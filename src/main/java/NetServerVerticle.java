import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetServerVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(NetServerVerticle.class);

    @Override
    public void start() {
        NetServer server = vertx.createNetServer();
        server.connectHandler(socket -> {
            vertx.deployVerticle(new NetClientVerticle(socket.writeHandlerID()), ar -> {
                socket.closeHandler(__ -> {
                    vertx.undeploy(ar.result());
                }).handler(buffer -> {
                    if (buffer.toString().equalsIgnoreCase("q")) {
                        socket.close();
                    }
                });
            });
        }).listen(1234, "localhost", res -> {
            if (res.succeeded()) {
                log.info("Server is now listening!");
            } else {
                log.info("Failed to bind!");
            }
        });
    }
}
