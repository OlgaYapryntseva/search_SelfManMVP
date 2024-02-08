package com.selfman.search.controller;

import com.google.maps.errors.ApiException;
import com.selfman.search.dto.SearchResultDto;
import com.selfman.search.service.interfaces.SearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("search/nearby/main")
@Tag(name = "Controller for general nearby search")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {
    SearchService searchService;

    @CrossOrigin
    @GetMapping
    @Operation(summary = "Search nearby providers by current geo-position. Involves caching.")
    public List<SearchResultDto> searchNearbyPlacesDetails(@RequestParam(name = "ltd") Double latitude,
                                                                 @RequestParam(name = "lng") Double longitude,
                                                                 @RequestParam(name = "radius", required = false, defaultValue = "1000") Double radius) throws ApiException, InterruptedException, IOException {
        return searchService.searchNearbyPlacesDetails(longitude, latitude, radius);
    }
}
