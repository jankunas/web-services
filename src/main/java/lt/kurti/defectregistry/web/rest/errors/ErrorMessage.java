package lt.kurti.defectregistry.web.rest.errors;

import java.util.Date;

public class ErrorMessage {

	private Date timestamp;
	private String message;
	private String systemMessage;

	public ErrorMessage(Date timestamp, String message, String systemMessage) {
		this.timestamp = timestamp;
		this.message = message;
		this.systemMessage = systemMessage;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSystemMessage() {
		return systemMessage;
	}

	public void setSystemMessage(final String systemMessage) {
		this.systemMessage = systemMessage;
	}
}
