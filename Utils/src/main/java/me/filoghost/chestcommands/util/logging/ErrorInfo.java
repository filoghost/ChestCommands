package me.filoghost.chestcommands.util.logging;

import java.util.ArrayList;
import java.util.List;

public class ErrorInfo {

	private final List<String> message;
	private Throwable cause;

	protected ErrorInfo(String messagePart) {
		this.message = new ArrayList<>();
		this.message.add(messagePart);
	}

	public ErrorInfo appendMessage(String messagePart) {
		message.add(messagePart);
		return this;
	}

	public List<String> getMessage() {
		return message;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

}
