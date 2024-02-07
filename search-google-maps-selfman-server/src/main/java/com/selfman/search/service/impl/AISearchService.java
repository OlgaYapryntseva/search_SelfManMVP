package com.selfman.search.service.impl;


import com.google.maps.errors.ApiException;
import com.selfman.search.client.MapsApiDetailsClient;
import com.selfman.search.client.Palm2ApiClient;
import com.selfman.search.dto.SearchResultDto;
import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.dto.palm2_api.Palm2ApiResponseDto;
import com.selfman.search.exception.UnableToParseException;
import com.selfman.search.service.IAISearchService;
import com.selfman.search.util.SearchResultMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AISearchService implements IAISearchService {
    MapsApiDetailsClient mapsApiDetailsClient;
    Palm2ApiClient palm2ApiClient;

    @Override
    public List<SearchResultDto> searchNearbyPlacesDetails(Double longitude, Double latitude, Double radius) 
    		throws ApiException, InterruptedException, IOException {
        List<PlacesDetailsByIdDto> places = mapsApiDetailsClient.getNearbyDetailsProviders(longitude, latitude, radius);
        if(places == null) {
            return List.of();
        }
        return places.stream()
                .filter(cd -> cd.getWebsite() != null && !cd.getWebsite().isEmpty())
                .map(this::getSearchResults)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private SearchResultDto getSearchResults(PlacesDetailsByIdDto place) {
        String[] titleDescription = this.parseTitleDescription(place.getWebsite());
        Palm2ApiResponseDto palm2ApiResponseDto = palm2ApiClient.getPalm2IndustryKeywordsSummary(titleDescription);
        if (palm2ApiResponseDto.getCandidates() == null) {
            return null;
        }
        return SearchResultMapper.placeDescriptionToDto(place, getKeywords(palm2ApiResponseDto));
    }

//    private String getIndustry(Palm2ApiResponseDto palm2Response) {
//        if (palm2Response.getCandidates() == null || palm2Response.getCandidates().isEmpty()) {
//            return "Undefined";
//        }
//        return palm2Response.getCandidates().get(0).getOutput().split("\\.\\s")[0];
//    }

    private List<String> getKeywords(Palm2ApiResponseDto palm2Response) {
        if (palm2Response.getCandidates() == null || palm2Response.getCandidates().isEmpty()) {
            return List.of("Undefined");
        }
        String keywordsStr = palm2Response.getCandidates().get(0).getOutput().split("\\.\\s")[1];
        return Arrays.stream(keywordsStr.split(","))
                .map(this::formatKeyword)
                .collect(Collectors.toList());
    }

    private String formatKeyword(String keyword) {
        String trimmedKeyword = keyword.trim().replaceAll("\\.", "");
        String firstLetter = trimmedKeyword.substring(0, 1);
        StringBuilder builder = new StringBuilder(trimmedKeyword);
        builder.replace(0, 1, firstLetter.toUpperCase());
        return builder.toString();
    }

    private String[] parseTitleDescription(String uri) {
        try {
            Document doc = Jsoup.connect(uri).get();
            String title = parseTitle(doc);
            String description = parseDescription(doc);
         System.out.println("url = " + uri);
         System.out.println("doc = " + doc);
         System.out.println("title = " + title);
            return new String[]{title, description};
        } catch (IOException e) {
            throw new UnableToParseException("Unable to parse");
        }
    }

    private String parseDescription(Document doc) {
        if (doc.select("meta[name=description]").isEmpty()) {
            return "None";
        }
        return doc.select("meta[name=description]")
                .get(0)
                .attr("content");
    }

    private String parseTitle(Document doc) {
        return doc.title();
    }
}
