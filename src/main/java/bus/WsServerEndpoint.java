package bus;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@ServerEndpoint("/ws")
@Service
@Slf4j
public class WsServerEndpoint {
	@Autowired
	WsSessionManager wsSessionManager;

	@OnOpen
	public void onOpen(Session session) {
		log.trace("WsServerEndpoint.onOpen : {} WsServerEndpoint:{} / wsSessionManager:{}", session.getId(), this.hashCode(), wsSessionManager.hashCode());
		wsSessionManager.put(session);
	}

	@OnClose
	public void onClose(Session session) {
		log.trace("WsServerEndpoint.onClose: {} WsServerEndpoint:{} / wsSessionManager:{}", session.getId(), this.hashCode(), wsSessionManager.hashCode());
		wsSessionManager.remove(session);
	}

	@OnMessage
	public String onMessage(String text) throws IOException {
		log.trace("WsServerEndpoint.onMessage: {}", text);
		return "servet 发送：" + text;
	}

	public void broadcast(String text) {
		log.trace("WsServerEndpoint.broadcast: {} WsServerEndpoint:{} / wsSessionManager:{}", text, this.hashCode(), wsSessionManager.hashCode());
//		for (String id : sessions.keySet()) {
//			log.trace("    : {}", id);
//			try {
//				Session session = sessions.get(id);
//				session.getBasicRemote().sendText(text);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
