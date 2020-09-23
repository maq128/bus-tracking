package bus;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;

// @ServerEndpoint and @Autowired
// https://stackoverflow.com/questions/29306854/serverendpoint-and-autowired

@ServerEndpoint("/ws")
@Slf4j
public class WsServerEndpoint {
	static ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();

	@OnOpen
	public void onOpen(Session session) {
		log.trace("WsServerEndpoint.onOpen : {}", session.getId());
		sessions.put(session.getId(), session);
	}

	@OnClose
	public void onClose(Session session) {
		log.trace("WsServerEndpoint.onClose: {}", session.getId());
		sessions.remove(session.getId());
	}

	public static void broadcast(String text) {
		for (String id : sessions.keySet()) {
			Session session = sessions.get(id);
			try {
				session.getBasicRemote().sendText(text);
			} catch (Exception e) {
				try {
					session.close();
					sessions.remove(id);
				} catch (IOException e1) {
				}
			}
		}
	}
}
