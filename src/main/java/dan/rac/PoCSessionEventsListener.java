package dan.rac;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class PoCSessionEventsListener {

	private List<String> activeSessions = new ArrayList<>();
	private final Logger logger = LoggerFactory.getLogger(PoCSessionEventsListener.class);

	@EventListener
	public void onSessionConnectedEvent(SessionConnectedEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		activeSessions.add(sha.getSessionId());
		logger.info("Added user session: {}", sha.getSessionId());
	}

	@EventListener
	public void onSessionDisconnected(SessionDisconnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		activeSessions.remove(sha.getSessionId());
		logger.info("Removed user session: {}", sha.getSessionId());
	}

	public List<String> getActiveSessions() {
		return activeSessions;
	}
}
