package com.selfman.search.service.interfaces;

import java.util.List;

import com.selfman.search.dto.SearchResultDto;

public interface KeywordSearchService {
    List<SearchResultDto> searchByKeywords(Double longitude, Double latitude, Double radius,String[] keywords);
}
