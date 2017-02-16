package dan.rac;

public class PoCRequestMessage {

	public PoCRequestMessage(String name) {
		this.name = name;
	}

	public PoCRequestMessage() {
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
