package com.selfman.search.exception;

public class UnableToRetrieveException extends RuntimeException {

	private static final long serialVersionUID = 926545729218124246L;

	public UnableToRetrieveException(final String message) {
        super(message);
    }
}
