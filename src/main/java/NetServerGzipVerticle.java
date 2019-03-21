import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.VertxInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetServerGzipVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(NetServerGzipVerticle.class);

    @Override
    public void start(Future<Void> startFuture) {
        ServerBootstrap bootstrap = createBootstrap();
        ContextInternal contextInt = (ContextInternal) context;
        bootstrap.bind(1245).addListener(future -> contextInt.executeFromIO(future, fut -> {
            if (fut.isSuccess()) {
                log.info("Bound sucessfully");
                startFuture.complete();
            } else {
                log.info("Oh noes, bound faild");
                startFuture.fail(fut.cause());
            }
        }));
    }

    private ServerBootstrap createBootstrap() {
        ContextInternal contextInt = (ContextInternal) context;
        EventLoop eventLoop = contextInt.nettyEventLoop();

        VertxInternal vertxInt = contextInt.owner();
        EventLoopGroup acceptorGroup = vertxInt.getAcceptorEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.channel(vertx.isNativeTransportEnabled() ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
        bootstrap.group(acceptorGroup, eventLoop);
        bootstrap.childHandler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) {
                log.info("Channel init...");
                ChannelPipeline pipeline = ch.pipeline();
                // add gzip to pipeline
                pipeline.addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
                pipeline.addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
                // need to add inbound channel handler to read and forward...
                vertx.deployVerticle(new NetClientGzipVerticle(ch));
            }
        });
        
        return bootstrap;
    }




}
