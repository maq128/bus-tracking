package bus;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;

/**
 * @deprecated 改用 Netty 实现 WebSocket server。
 */
@ServerEndpoint("/ws")
@Slf4j
public class WsServerEndpoint {
	private static ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
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

	public static CompletableFuture<Void> broadcast(final String json) {
		return CompletableFuture.runAsync(() -> {
			log.trace("WsServerEndpoint.broadcast: {}", sessions.size());
			lastJson = json;

			// 并发推送消息，以提高推送效率
			final WaitGroup wg = new WaitGroup();

			for (String id : sessions.keySet()) {
				Session session = sessions.get(id);
				try {
					wg.add(1);
					session.getAsyncRemote().sendText(json, (result) -> {
						log.trace("SendResult: {} / {}", result.isOK(), result.getException());
						wg.done();
					});
				} catch (Exception e) {
					try {
						sessions.remove(id);
						session.close();
					} catch (IOException e1) {
					}
				}
			}
			wg.await();
		});
	}

	static class WaitGroup {
	    private int jobs = 0;

	    public synchronized void add(int i) {
	        jobs += i;
	    }

	    public synchronized void done() {
	        if (--jobs == 0) {
	            notifyAll();
	        }
	    }

	    public synchronized void await() {
	        while (jobs > 0) {
	            try {
					wait();
				} catch (InterruptedException e) {
					return;
				}
	        }
	    }
	}
}
