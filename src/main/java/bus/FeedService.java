package bus;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
@EnableScheduling
public class FeedService {
	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		return scheduler;
	}

	@Autowired
	ApiService apiService;

	@Scheduled(fixedDelay = 15000)
	public void broadcast() {
		// 如果无人在线，则跳过
		if (WsServerEndpoint.getSessionsNum() == 0) return;

		// 抓取位置信息，并广播推送
		Map<String, Object>[] locations = apiService.monitorBus(true);
		Gson g = new Gson();
		String json = g.toJson(locations);
		WsServerEndpoint.broadcast(json);
	}
}
