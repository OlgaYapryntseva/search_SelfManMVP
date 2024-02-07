package com.selfman.search.service;

import com.google.maps.errors.ApiException;
import com.selfman.search.dto.SearchResultDto;

import java.io.IOException;
import java.util.List;

public interface INearbySearchService {
    List<SearchResultDto> searchNearbyPlacesDetails(Double longitude, Double latitude, Double radius) 
    		throws ApiException, InterruptedException, IOException;

}
