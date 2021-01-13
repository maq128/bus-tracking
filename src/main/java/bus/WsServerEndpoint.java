package bus;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;

@ServerEndpoint("/ws")
@Slf4j
public class WsServerEndpoint {
	static ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();

	private static String lastJson;

	@OnOpen
	public void onOpen(Session session) {
		log.trace("WsServerEndpoint.onOpen : {}", session.getId());
		if (lastJson != null) {
			try {
				session.getBasicRemote().sendText(lastJson);
			} catch (Exception e) {
				try {
					session.close();
				} catch (IOException e1) {
				}
				return;
			}
		}
		sessions.put(session.getId(), session);
	}

	@OnClose
	public void onClose(Session session) {
		log.trace("WsServerEndpoint.onClose: {}", session.getId());
		sessions.remove(session.getId());
	}

	public static int getSessionsNum() {
		return sessions.size();
	}

	public static void broadcast(String json) {
		log.trace("WsServerEndpoint.broadcast: {}", sessions.size());
		lastJson = json;
		for (String id : sessions.keySet()) {
			Session session = sessions.get(id);
			try {
				session.getBasicRemote().sendText(json);
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
