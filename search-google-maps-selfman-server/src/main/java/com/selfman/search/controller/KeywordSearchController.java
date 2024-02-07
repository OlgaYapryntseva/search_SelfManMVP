package com.selfman.search.controller;

import com.selfman.search.dto.SearchResultDto;
import com.selfman.search.service.IKeywordSearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("search/keywords")
@Tag(name = "Controller for keywords-based search")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeywordSearchController {
    final IKeywordSearchService keywordSearchService;

    @CrossOrigin
    @GetMapping
    @Operation(summary = "Search all providers by keywords. Involves caching. NOTE: Only one word in string.")
    public List<SearchResultDto> searchNearbyProviders(
    		@RequestParam(name = "ltd") Double latitude,
            @RequestParam(name = "lng") Double longitude,
            @RequestParam(name = "radius", required = false, defaultValue = "1000") Double radius,
            @RequestParam(name = "keywords") String[] keywords) {

        return keywordSearchService.searchByKeywords(longitude, latitude, radius,keywords);
    }
}
