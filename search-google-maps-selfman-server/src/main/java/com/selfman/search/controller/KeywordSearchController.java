package com.selfman.search.controller;

import com.google.maps.errors.ApiException;
import com.selfman.search.dto.SearchResultDto;
import com.selfman.search.service.interfaces.KeywordSearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("search/keywords")
@Tag(name = "Controller for keywords-based search")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeywordSearchController {
    final KeywordSearchService keywordSearchService;

    @CrossOrigin
    @GetMapping
    @Operation(summary = "Search all providers by keywords. Involves caching. NOTE: Only one word in string.")
    public List<SearchResultDto> searchByKeywords(
    		@RequestParam(name = "ltd") Double latitude,
            @RequestParam(name = "lng") Double longitude,
            @RequestParam(name = "radius", required = false, defaultValue = "1000") Double radius,
            @RequestParam(name = "keywords") String[] keywords) 
            		throws ApiException, InterruptedException, IOException, ExecutionException {

        return keywordSearchService.searchByKeywords(longitude, latitude, radius,keywords);
    }
}
