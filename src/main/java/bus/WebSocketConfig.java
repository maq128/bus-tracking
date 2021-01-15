package bus;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @deprecated 改用 Netty 实现 WebSocket server。
 */
//@Configuration
//@EnableWebSocket
public class WebSocketConfig {
	@Bean
	public ServerEndpointExporter serverEndpoint() {
		ServerEndpointExporter exporter = new ServerEndpointExporter();
		exporter.setAnnotatedEndpointClasses(WsServerEndpoint.class);
		return exporter;
	}
}
