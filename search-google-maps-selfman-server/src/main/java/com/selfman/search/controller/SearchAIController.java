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
@Tag(name = "Controller for nearby search with AI keywords / industry analysis")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SearchAIController {
	SearchService iNearbySearchService;

	@CrossOrigin
	@GetMapping("/search/nearby/ai")
	@Operation(summary = "Search all nearby providers with Palm2 AI analysis of keywords / industry. Caching currently is not involved.")
	public List<SearchResultDto> searchNearbyProvidersWithAI(@RequestParam(name = "ltd") Double latitude,
			@RequestParam(name = "lng") Double longitude,
			@RequestParam(name = "radius", required = false, defaultValue = "1000") Double radius)
			throws ApiException, InterruptedException, IOException {
		return iNearbySearchService.searchNearbyPlacesDetails(longitude, latitude, radius);
	}



	

}
