package com.selfman.search.service.impl;

import com.google.maps.errors.ApiException;
import com.selfman.search.client.MapsApiDetailsClient;
import com.selfman.search.dto.SearchResultDto;
import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.exception.details.PlacesInLocationNotFound;
import com.selfman.search.model.Resource;
import com.selfman.search.service.interfaces.ElasticService;
import com.selfman.search.service.interfaces.SearchService;
import com.selfman.search.util.SearchResultMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchServiceImpl implements SearchService {

	final MapsApiDetailsClient mapsApiDetailsClient;
	final ElasticService elasticService;

	@Override
	public List<SearchResultDto> searchNearbyPlacesDetails(Double longitude, Double latitude,
			Double radius) throws ApiException, InterruptedException, IOException, ExecutionException {
		List<PlacesDetailsByIdDto> places = mapsApiDetailsClient.getNearbyDetailsProviders(longitude, latitude,
					radius);
		if(places.size() < 0) {
			throw new PlacesInLocationNotFound();
		}
		List<Resource> resourcesFound = new ArrayList<>();
			 places.stream().forEach(place -> {
				resourcesFound.add(elasticService.saveResourceWithOutKeywords(place));
				});	 
			 
//				String resourceUrl = place.getWebsite();
//				if (resourceUrl != null && !elasticService.checkIfResourceExistsByUrl(resourceUrl)) {
//					String resourceContent = WebScrapper.scrapResource(resourceUrl);
//					resourcesFound.add(elasticService.saveResourceWithoutKeywords(place, resourceContent));
//				} else if (resourceUrl != null) {
//					resourcesFound.add(elasticService.findResourceByUrl(resourceUrl));
//				}		
//		return resourcesFound.stream().map((resource -> {
//			List<String> foundKeywords = new ArrayList<>(
//					elasticService.findKeywordsByResourceUrl(resource.getPlace_id()));
//			return SearchResultMapper.resourceToDto(resource, foundKeywords);
//		})).collect(Collectors.toList());
			 
		return resourcesFound.stream().map(r -> SearchResultMapper.resourceToDto(r, List.of("")))
					 .collect(Collectors.toList());
	}
}
