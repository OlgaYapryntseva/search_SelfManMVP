package com.selfman.search.service.impl;

import com.google.maps.errors.ApiException;
import com.selfman.search.client.MapsApiDetailsClient;
import com.selfman.search.dto.SearchResultDto;
import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.entity.Resource;
import com.selfman.search.service.ICachingService;
import com.selfman.search.service.INearbySearchService;
import com.selfman.search.util.SearchResultMapper;
import com.selfman.search.util.WebScrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NearbySearchService implements INearbySearchService {
    final MapsApiDetailsClient mapsApiDetailsClient;
    final ICachingService cachingService;

    @Override
    public List<SearchResultDto> searchNearbyPlacesDetails(Double longitude, Double latitude, Double radius) 
    		throws ApiException, InterruptedException, IOException {
        //Retrieve nearby providers from Google Maps API
    	List<PlacesDetailsByIdDto> places = mapsApiDetailsClient.getNearbyDetailsProviders(longitude, latitude, radius);
        List<Resource> resourcesFound = new ArrayList<>();
        places.stream().forEach((place) -> {
            String resourceUrl = place.getWebsite();
            //If resource is not cached - scrap and save it
            if (resourceUrl != null && !cachingService.checkIfResourceExistsByUrl(resourceUrl)) {
                String resourceContent = WebScrapper.scrapResource(resourceUrl);  
                resourcesFound.add(cachingService.saveResourceWithoutKeywords(place, resourceContent));
            } else if (resourceUrl != null) {
                resourcesFound.add(cachingService.findResourceByUrl(resourceUrl));
            }
        });
        return resourcesFound.stream().map((resource -> {
            List<String> foundKeywords = new ArrayList<>(cachingService.findKeywordsByResourceUrl(resource.getResourceUrl()));
            return SearchResultMapper.resourceToDto(resource, foundKeywords);
        })).collect(Collectors.toList());
    }
}
