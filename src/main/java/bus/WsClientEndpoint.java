package bus;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import lombok.extern.slf4j.Slf4j;

@ClientEndpoint
@Slf4j
public class WsClientEndpoint {
	private String url;
	private MessageHandler handler;
	private Session session;

	public WsClientEndpoint(String url, MessageHandler handler) {
		this.url = url;
		this.handler = handler;
	}

	public Session connect() {
		if (session != null) return session;
		log.trace("connecting...");
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			session = container.connectToServer(this, new URI(url));
		} catch (Exception e) {
			log.error("connect failed: {}", e.getMessage());
			if (e.getCause() != null) {
				log.error("              : {}", e.getCause().getMessage());
			}
			session = null;
		}
		return session;
	}

	public void disconnect() {
		if (session == null) return;
		log.trace("disconnecting...");
		try {
			session.close();
		} catch (Exception e) {
			log.error("disconnect failed: {}", e.getMessage());
			if (e.getCause() != null) {
				log.error("                 : {}", e.getCause().getMessage());
			}
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		log.trace("connected: {}", session.getId());
		this.session = session;
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		log.trace("disconnected: {}", session.getId());
		this.session = null;
	}

	@OnMessage
	public void onMessage(String message) {
		if (this.handler != null) {
			this.handler.handleMessage(message);
		}
	}

	public void sendMessage(String message) {
		if (session == null) return;
		this.session.getAsyncRemote().sendText(message);
	}

	public static interface MessageHandler {
		public void handleMessage(String message);
	}
}
