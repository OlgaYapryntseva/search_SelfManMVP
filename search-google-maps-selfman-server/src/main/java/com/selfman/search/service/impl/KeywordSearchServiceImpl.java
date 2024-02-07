package com.selfman.search.service.impl;


import com.selfman.search.dto.SearchResultDto;
import com.selfman.search.model.Resource;
import com.selfman.search.service.interfaces.ElasticService;
import com.selfman.search.service.interfaces.KeywordSearchService;
import com.selfman.search.util.SearchResultMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeywordSearchServiceImpl implements KeywordSearchService {
    //MapsApiClient mapsApiClient;
    final ElasticService cachingService;

    @Override
    public List<SearchResultDto> searchByKeywords(Double longitude, Double latitude, Double radius,String[] keywords) {
        String[] formattedNonBlankKeywords = prepareKeywords(keywords);
        //Search resources by cached keywords
        Set<Resource> resourcesFound = new HashSet<>();
        //Search resources by non-cached keywords and cached results
        //This will produce references between new keywords and respective resources
        resourcesFound.addAll(cachingService.findResourcesByNonCachedKeywords(formattedNonBlankKeywords));
        //If no results were found - make request to Google Maps API
      /*  if (resourcesFound.isEmpty() || resourcesFound.size() <= 10) {
            List<CustomPlaceDescription> places = mapsApiClient.getProvidersByText(longitude, latitude, radius,formattedNonBlankKeywords);
            places.forEach((place) -> {
                String resourceUrl = place.getWebsiteUri();
                //If resource is not cached - scrap, cache it and add to results
                if (resourceUrl != null && !cachingService.checkIfResourceExistsByUrl(resourceUrl)) {
                    String resourceContent = WebScrapper.scrapResource(resourceUrl);
                    resourcesFound.add(cachingService.saveResourceWithKeywords(formattedNonBlankKeywords, place, resourceContent));
                }
            });
        }*/
        return resourcesFound.stream().map((resource -> {
            List<String> foundKeywords = new ArrayList<>(cachingService.findKeywordsByResourceUrl(resource.getResourceUrl()));
            return SearchResultMapper.resourceToDto(resource, foundKeywords);
        })).collect(Collectors.toList());
    }

    private String[] prepareKeywords(String[] keywords) {
        return Arrays.stream(keywords).filter(k -> !k.isBlank())
                .map(String::toLowerCase).toArray(String[]::new);
    }
}
