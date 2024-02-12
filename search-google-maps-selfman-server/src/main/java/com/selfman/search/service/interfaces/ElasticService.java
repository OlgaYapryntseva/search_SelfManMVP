package com.selfman.search.service.interfaces;

import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.model.Resource;

import java.util.List;
import java.util.Set;

public interface ElasticService {
	
    Resource saveResourceWithOutKeywords(PlacesDetailsByIdDto placeDescription);

    Resource saveResourceWithKeywords(String[] keywords, PlacesDetailsByIdDto placeDescription, String resourceContent);

    List<Resource> findResourcesByCachedKeywords(String[] keywords);

    List<Resource> findResourcesByNonCachedKeywords(String[] keywords);

    boolean checkIfResourceExistsById(String placeId);

    Resource findResourceByUrl(String url);

    Set<String> findKeywordsByResourceUrl(String url);
}
