package dan.rac;

public class PocBackendServiceMessage {

	public String destination;
	public String message;

	public PocBackendServiceMessage() {
	}

	public PocBackendServiceMessage(String destination, String message) {
		this.destination = destination;
		this.message = message;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
