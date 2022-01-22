package com.github.thundersphun.bcf.config.json;

public class JsonMissingKeyException extends Throwable {
	public JsonMissingKeyException() {
	}

	public JsonMissingKeyException(String s) {
		super(s);
	}

	public JsonMissingKeyException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonMissingKeyException(Throwable cause) {
		super(cause);
	}
}
