package com.selfman.search.exception.details;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UnableToRetrieveException extends RuntimeException {

	private static final long serialVersionUID = 926545729218124246L;

	public UnableToRetrieveException() {
        super("Unable to retrieve response from Places API");
    }
}
