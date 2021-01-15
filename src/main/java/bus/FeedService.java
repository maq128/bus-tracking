package bus;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

@Service
@EnableScheduling
@Slf4j
public class FeedService {
	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		return scheduler;
	}

	final static String wsEndpoint = "ws://jfzg.21vianet.com/xingzheng/websocket/banche/position";

	private String lastJson;
	private WsClientEndpoint wsClient;

	private Future<Void> futureBroadcast;

	@Scheduled(fixedDelay = 5000)
	public void healthcheck() {
		if (wsClient == null) {
			wsClient = new WsClientEndpoint(wsEndpoint, new WsClientEndpoint.MessageHandler() {
				public void handleMessage(final String json) {
					// 如果上一个消息还没有推送完，则跳过
					if (futureBroadcast != null) {
						log.warn("message overlapped, ignore.");
						return;
					}

					// 如果是重复内容，则跳过
					if (json.equals(lastJson)) {
						log.trace("duplicated message, skip.");
						return;
					}
					lastJson = json;

					log.trace("broadcast: {}", json);

					// 广播推送消息，异步执行
					futureBroadcast = NettyWebsocketServerHandler.broadcast(json);
					futureBroadcast.addListener((f) -> {
						log.trace("broadcast: finished.");
						futureBroadcast = null;
					});
				}
			});
		}

		if (NettyWebsocketServerHandler.getClientsNum() > 0) {
			// 有用户在线，需要持续获取数据
			wsClient.connect();
		} else {
			// 没有用户在线，停止获取数据，以节省流量
			wsClient.disconnect();
		}
	}
}
