package com.selfman.search.service.impl;


import com.google.maps.errors.ApiException;
import com.selfman.search.client.MapsApiDetailsClient;
import com.selfman.search.dto.SearchResultDto;
import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.model.Resource;
import com.selfman.search.service.interfaces.ElasticService;
import com.selfman.search.service.interfaces.KeywordSearchService;
import com.selfman.search.util.SearchResultMapper;
import com.selfman.search.util.WebScrapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeywordSearchServiceImpl implements KeywordSearchService {
	
    final MapsApiDetailsClient mapsApiDetailsClient;
    final ElasticService elasticService;

    @Override
    public List<SearchResultDto> searchByKeywords(Double longitude, Double latitude, Double radius,String[] keywords) 
    		throws ApiException, InterruptedException, IOException {
        String[] formattedNonBlankKeywords = prepareKeywords(keywords);
        //Search resources by cached keywords
        Set<Resource> resourcesFound = new HashSet<>();
        //Search resources by non-cached keywords and cached results
        //This will produce references between new keywords and respective resources
        resourcesFound.addAll(elasticService.findResourcesByNonCachedKeywords(formattedNonBlankKeywords));
        //If no results were found - make request to Google Maps API
        if (resourcesFound.isEmpty() || resourcesFound.size() <= 10) {
            List<PlacesDetailsByIdDto> places = mapsApiDetailsClient.getNearbyDetailsProviders(longitude, latitude, radius);         
            places.forEach((place) -> {
                String resourceUrl = place.getWebsite();
                //If resource is not cached - scrap, cache it and add to results
                if (resourceUrl != null && !elasticService.checkIfResourceExistsByUrl(resourceUrl)) {
                    String resourceContent = WebScrapper.scrapResource(resourceUrl);
                    resourcesFound.add(elasticService.saveResourceWithKeywords(formattedNonBlankKeywords, place, resourceContent));
                }
            });
        }
        return resourcesFound.stream().map((resource -> {
            List<String> foundKeywords = new ArrayList<>(elasticService.findKeywordsByResourceUrl(resource.getResourceUrl()));
            return SearchResultMapper.resourceToDto(resource, foundKeywords);
        })).collect(Collectors.toList());
    }

    private String[] prepareKeywords(String[] keywords) {
        return Arrays.stream(keywords).filter(k -> !k.isBlank())
                .map(String::toLowerCase).toArray(String[]::new);
    }
}
