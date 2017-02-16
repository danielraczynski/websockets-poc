package dan.rac;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PoCHttpController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private SimpUserRegistry simpUserRegistry;

	private final Logger logger = org.slf4j.LoggerFactory.getLogger(PoCHttpController.class);
	private PoCSessionEventsListener eventsListener;

	@Autowired
	public PoCHttpController(PoCSessionEventsListener eventsListener) {
		this.eventsListener = eventsListener;
	}

	@RequestMapping("/information")
	public String info(HttpSession session) {
		logger.info("Http session: {}", session.getId());
		return "Server running - HTTP service";
	}

	@RequestMapping("activeSessions")
	public String activeSessions() {
		return "Local active sessions: " + StringUtils.join(eventsListener.getActiveSessions(), ",") + "\n" +
				"All active sessions: " + StringUtils.join(simpUserRegistry.getUsers(), ",");
	}

	@RequestMapping("getFromOtherNode")
	public void getFromOtherNode(@RequestBody PocBackendServiceMessage message) {
		logger.info("Received message from other node. User: {}", message.getDestination());
		simpMessagingTemplate.convertAndSendToUser(message.getDestination(), "/queue/personal-message10", new PoCReplyMessage(message.getMessage()));
		logger.info("Sending STOMP message to the broker. User: {}", message.getDestination());
	}
}
