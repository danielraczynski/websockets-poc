package dan.rac;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class PoCWebsocketController {

	private final Logger logger = LoggerFactory.getLogger(PoCWebsocketController.class);
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("hello")
	@SendTo("/topic/greetings")
	public PoCReplyMessage greeting(PoCRequestMessage helloMessage, @Headers MessageHeaders headers) throws Exception {
		Thread.sleep(1000);
		return new PoCReplyMessage("Hello " + helloMessage.getName());
	}

//	@MessageMapping("/personal")
//	@SendToUser("/queue/personal-message")
//	public PoCReplyMessage personalMessage(SimpMessageHeaderAccessor headerAccessor, PoCRequestMessage message, @Headers MessageHeaders headers) {
//		try {
//
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		return new PoCReplyMessage("Personal Message" + message.getName());
//	}

	@MessageMapping("/personal")
	public void personalMessage(SimpMessageHeaderAccessor headerAccessor, PoCRequestMessage message, @Headers MessageHeaders headers) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final String daniel = "/queue/twoajastara";
		simpMessagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), daniel, message);
	}

	private void sendToOtherNode(String dest) {
		PocBackendServiceMessage message = new PocBackendServiceMessage(dest, "Notification message from other node.");
		String uri = "http://localhost:8081/getFromOtherNode";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<PocBackendServiceMessage> entity = new HttpEntity<>(message, headers);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(uri, HttpMethod.POST, entity, Void.class);
		logger.info("Sent notification message for {}", uri);
	}
}
