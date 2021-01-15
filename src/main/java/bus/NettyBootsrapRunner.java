package bus;

import java.net.InetSocketAddress;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NettyBootsrapRunner implements ApplicationRunner, ApplicationListener<ContextClosedEvent>, ApplicationContextAware {
	@Value("${netty.websocket.ip}")
	private String ip;

	@Value("${netty.websocket.port}")
	private int port;

	@Value("${netty.websocket.path}")
	private String path;

	@Value("${netty.websocket.max-frame-size}")
	private int maxFrameSize;

	private ApplicationContext applicationContext;

	private Channel serverChannel;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void onApplicationEvent(ContextClosedEvent event) {
		if (this.serverChannel != null) {
			this.serverChannel.close();
		}
	}

	public void run(ApplicationArguments args) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup);
			serverBootstrap.channel(NioServerSocketChannel.class);
			serverBootstrap.localAddress(new InetSocketAddress(this.ip, this.port));
			serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					ChannelPipeline pipeline = socketChannel.pipeline();
					pipeline.addLast(new HttpServerCodec());
					pipeline.addLast(new ChunkedWriteHandler());
					pipeline.addLast(new HttpObjectAggregator(65536));
					pipeline.addLast(new ChannelInboundHandlerAdapter() {
						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
							if(msg instanceof FullHttpRequest) {
								FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
								String uri = fullHttpRequest.uri();
								if (!uri.equals(path)) {
									// 访问的路径不是 websocket 的端点地址，响应 404
									ctx.channel()
										.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND))
										.addListener(ChannelFutureListener.CLOSE);
									return;
								}
							}
							super.channelRead(ctx, msg);
						}
					});
					pipeline.addLast(new WebSocketServerCompressionHandler());
					pipeline.addLast(new WebSocketServerProtocolHandler(path, null, true, maxFrameSize));

					// 在业务层处理 WebSocket 事件
					pipeline.addLast(applicationContext.getBean(NettyWebsocketServerHandler.class));
				}
			});
			Channel channel = serverBootstrap.bind().sync().channel();
			this.serverChannel = channel;
			log.info("netty.websocket server started at {}:{}{}", this.ip, this.port, this.path);
			channel.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			log.info("netty.websocket server stopped.");
		}
	}
}
