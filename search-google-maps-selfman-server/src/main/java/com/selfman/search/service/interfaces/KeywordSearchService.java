package com.selfman.search.service.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.maps.errors.ApiException;
import com.selfman.search.dto.SearchResultDto;

public interface KeywordSearchService {
    List<SearchResultDto> searchByKeywords(Double longitude, Double latitude, Double radius,String[] keywords) 
    		throws ApiException, InterruptedException, IOException, ExecutionException;
}
