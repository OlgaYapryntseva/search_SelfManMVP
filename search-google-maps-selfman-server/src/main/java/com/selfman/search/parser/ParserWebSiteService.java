package com.selfman.search.parser;

import java.io.IOException;

import com.selfman.search.dto.details.PlacesDetailsByIdDto;

public interface ParserWebSiteService {

	public void parseLogoExtractor(PlacesDetailsByIdDto place);
	
	public String[] parseTitleDescription(String url) throws IOException;

}
