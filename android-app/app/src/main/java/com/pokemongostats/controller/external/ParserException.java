package com.pokemongostats.controller.external;

public class ParserException extends Exception {

	public ParserException(String message) {
		super(message);
	}
		public ParserException(String message, Throwable cause) {
			super(message, cause);
		}

		public ParserException(Throwable cause) {
			super(cause);
		}

}