package com.selfman.search.service;

import java.util.List;

import com.selfman.search.dto.SearchResultDto;

public interface IKeywordSearchService {
    List<SearchResultDto> searchByKeywords(Double longitude, Double latitude, Double radius,String[] keywords);
}
