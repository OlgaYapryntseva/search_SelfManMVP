package com.selfman.search.service.impl;

import com.google.maps.errors.ApiException;
import com.selfman.search.client.MapsApiDetailsClient;
import com.selfman.search.client.Palm2ApiClient;
import com.selfman.search.dto.SearchResultDto;
import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.dto.palm2_api.Palm2ApiResponseDto;
import com.selfman.search.service.interfaces.SearchAIService;
import com.selfman.search.util.ParserWebSite;
import com.selfman.search.util.SearchResultMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchAIServiceImpl implements SearchAIService {
	
	final MapsApiDetailsClient mapsApiDetailsClient;
	final Palm2ApiClient palm2ApiClient;
	final ParserWebSite parserWebSite;
	
	@Override
	public List<SearchResultDto> searchNearbyProvidersWithAI(Double longitude, Double latitude, Double radius)
			throws ApiException, InterruptedException, IOException {
		List<PlacesDetailsByIdDto> places = mapsApiDetailsClient.getNearbyDetailsProviders(longitude, latitude, radius);
		if (places == null) {
			return List.of();
		}
		return places.stream().filter(cd -> cd.getWebsite() != null && !cd.getWebsite().isEmpty())
				.map(t -> {
					try {
						return getSearchResults(t);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	private SearchResultDto getSearchResults(PlacesDetailsByIdDto place) throws IOException {
		String[] titleDescription = parserWebSite.parseTitleDescription(place.getWebsite());
		Palm2ApiResponseDto palm2ApiResponseDto = palm2ApiClient.getPalm2IndustryKeywordsSummary(titleDescription);
		if (palm2ApiResponseDto.getCandidates() == null) {
			return null;
		}
		return SearchResultMapper.placeDescriptionToDto(place, getKeywords(palm2ApiResponseDto));
	}

	private List<String> getKeywords(Palm2ApiResponseDto palm2Response) {
		if (palm2Response.getCandidates() == null || palm2Response.getCandidates().isEmpty()) {
			return List.of("Undefined");
		}
		String keywordsStr = palm2Response.getCandidates().get(0).getOutput().split("\\.\\s")[1];
		return Arrays.stream(keywordsStr.split(",")).map(this::formatKeyword).collect(Collectors.toList());
	}

	private String formatKeyword(String keyword) {
		String trimmedKeyword = keyword.trim().replaceAll("\\.", "");
		String firstLetter = trimmedKeyword.substring(0, 1);
		StringBuilder builder = new StringBuilder(trimmedKeyword);
		builder.replace(0, 1, firstLetter.toUpperCase());
		return builder.toString();
	}

}
