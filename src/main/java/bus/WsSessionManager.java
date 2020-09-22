package bus;

import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WsSessionManager {
	ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();

	public void put(Session session) {
		log.trace("WsSessionManager.put:{} WsSessionManager:{} / sessions:{}", session.getId(), this.hashCode(), sessions.hashCode());
		sessions.put(session.getId(), session);
	}

	public void remove(Session session) {
		log.trace("WsSessionManager.remove:{} WsSessionManager:{} / sessions:{}", session.getId(), this.hashCode(), sessions.hashCode());
		sessions.remove(session.getId());
	}

	public Session get(String id) {
		log.trace("WsSessionManager.get:{} WsSessionManager:{} / sessions:{}", id, this.hashCode(), sessions.hashCode());
		return sessions.get(id);
	}
}
