package bus;

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
	@OnOpen
	public void onOpen(Session session) {
		log.trace("WsServerEndpoint.onOpen : {}", session.getId());
		WsSessionManager.put(session);
	}

	@OnClose
	public void onClose(Session session) {
		log.trace("WsServerEndpoint.onClose: {}", session.getId());
		WsSessionManager.remove(session);
	}
}
