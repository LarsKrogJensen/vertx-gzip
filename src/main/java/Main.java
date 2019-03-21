import io.vertx.core.Vertx;

public class Main {
    public static void main(String[] args) {
        System.setProperty("io.vertx.core.logging.Log4jLogDelegateFactory", "io.vertx.core.logging.SLF4JLogDelegateFactory");
        var vertx = Vertx.vertx();
        vertx.deployVerticle(new TimeOfDayVerticle());
        vertx.deployVerticle(new NetServerVerticle());
        vertx.deployVerticle(new NetServerGzipVerticle());
    }
}
