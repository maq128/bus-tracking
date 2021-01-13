package bus;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

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

	@Scheduled(fixedDelay = 5000)
	public void healthcheck() {
		if (wsClient == null) {
			wsClient = new WsClientEndpoint(wsEndpoint, new WsClientEndpoint.MessageHandler() {
				public void handleMessage(String json) {
					// 如果是重复内容，则跳过
					if (json.equals(lastJson)) {
						log.trace("handleTextMessage: duplicated message, skip.");
						return;
					}
					lastJson = json;

					log.trace("handleTextMessage: {}", json);

					// 广播推送消息
					// TODO: 避免发送时间过长，导致新消息重叠
					WsServerEndpoint.broadcast(json);
				}
			});
		}

		if (WsServerEndpoint.getSessionsNum() > 0) {
			// 有用户在线，需要持续获取数据
			wsClient.connect();
		} else {
			// 没有用户在线，停止获取数据，以节省流量
			wsClient.disconnect();
		}
	}
}
