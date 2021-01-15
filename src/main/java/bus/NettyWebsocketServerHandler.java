package bus;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Sharable
@Component
@Slf4j
public class NettyWebsocketServerHandler extends ChannelInboundHandlerAdapter {
	private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private static String lastJson;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		log.trace("channelActive: {} @{}", channel.id().asLongText(), channel.remoteAddress());
		if (lastJson != null) {
			try {
				// FIXME: 客户端没有收到？
				channel.writeAndFlush(new TextWebSocketFrame(lastJson));
			} catch (Exception e) {
				channel.close();
				return;
			}
		}
		channelGroup.add(channel);

		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		log.trace("channelInactive: {} @{}", channel.id().asLongText(), channel.remoteAddress());

		super.channelInactive(ctx);
	}

	public static int getClientsNum() {
		return channelGroup.size();
	}

	public static Future<Void> broadcast(final String json) {
		log.trace("broadcast: {}", channelGroup.size());
		lastJson = json;

		return channelGroup.writeAndFlush(new TextWebSocketFrame(json));
	}
}
