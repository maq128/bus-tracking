package bus;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

public class WsSessionManager {
	static ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();

	public static void put(Session session) {
		sessions.put(session.getId(), session);
	}

	public static void remove(Session session) {
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
