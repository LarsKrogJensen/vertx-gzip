import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.VertxInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetServerGzipVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(NetServerGzipVerticle.class);

    @Override
    public void start() {
        ContextInternal contextInt = (ContextInternal) context;
        EventLoop eventLoop = contextInt.nettyEventLoop();

        VertxInternal vertxInt = contextInt.owner();
        EventLoopGroup acceptorGroup = vertxInt.getAcceptorEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.group(acceptorGroup, eventLoop);
        bootstrap.childHandler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) {
                log.info("Channel init...");
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast();
//                TimeServerHandler handler = new TimeServerHandler(context, requestHandler);
//                pipeline.addLast(handler);
            }
        });

       bootstrap.bind(1245).addListener(future -> contextInt.executeFromIO(future, fut -> {
            if (fut.isSuccess()) {
                log.info("Bound sucessfully");
//                future.
            } else {
                log.info("Oh noes, bound faild");
            }
        }));

    }
}
