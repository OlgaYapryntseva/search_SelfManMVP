package com.selfman.search.exception.details;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlacesInLocationNotFound extends RuntimeException {

	private static final long serialVersionUID = 9196996693106865288L;
	
	public PlacesInLocationNotFound() {
		super("Noting found in this geolocation");
	}

}
