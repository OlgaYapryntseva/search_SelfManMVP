package com.selfman.search.service.interfaces;

import com.google.maps.errors.ApiException;
import com.selfman.search.dto.SearchResultDto;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface SearchService {
	List<SearchResultDto> searchNearbyPlacesDetails(Double longitude, Double latitude, Double radius) 
    		throws ApiException, InterruptedException, IOException, ExecutionException;

}
