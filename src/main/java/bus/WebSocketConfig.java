package bus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfig {
	@Bean
	public ServerEndpointExporter serverEndpoint() {
		ServerEndpointExporter exporter = new ServerEndpointExporter();
		exporter.setAnnotatedEndpointClasses(WsServerEndpoint.class);
		return exporter;
	}
}
