import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Main {
    public static void main(String[] args) {
        System.setProperty("io.vertx.core.logging.Log4jLogDelegateFactory", "io.vertx.core.logging.SLF4JLogDelegateFactory");
        VertxOptions options = new VertxOptions().setPreferNativeTransport(true);
        var vertx = Vertx.vertx(options);

        vertx.deployVerticle(new TimeOfDayVerticle());
        vertx.deployVerticle(new NetServerVerticle());
        vertx.deployVerticle(new NetServerGzipVerticle());
    }
}
