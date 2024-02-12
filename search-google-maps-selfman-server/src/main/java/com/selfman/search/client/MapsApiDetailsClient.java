package com.selfman.search.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.selfman.search.dto.details.PlacesByLocationResponseDto;
import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.dto.details.PlacesDetailsResponsDto;
import com.selfman.search.dto.details.PlacesIdDto;
import com.selfman.search.dto.places_api.search_nearby.PlacesCircle;
import com.selfman.search.dto.places_api.search_nearby.PlacesCircleCenter;
import com.selfman.search.dto.places_api.search_nearby.PlacesLocationRestrictions;
import com.selfman.search.dto.places_api.text_search.PlacesApiTextRequestDto;
import com.selfman.search.exception.details.PlacesInLocationNotFound;
import com.selfman.search.exception.details.UnableToRetrieveException;
import com.selfman.search.parser.ParserWebSiteServiceImpl;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Data
public class MapsApiDetailsClient {

	@Autowired
	final RestTemplate client;
	final ParserWebSiteServiceImpl parserWebSite;

	@Value("${maps.api.key_1}")
	String apiKey;

	final static int MAX_RESULT_COUNT = 10;
	final static String PLACES_NEARBY_API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	final static String PLACES_NEARBY_API_URL_DETAILS = "https://maps.googleapis.com/maps/api/place/details/json";
	final static String PLACES_API_PHOTO = "https://maps.googleapis.com/maps/api/place/photo";

//	final static List<String> TYPES = List.of("wholesaler", "store");
	final static List<String> TYPES = List.of("manufacturing","establishment.manufacturing");
//	final static List<String> TYPES = List.of("establishment.manufacturing", "factory", "establishment.industrial", 
//			"establishment.plant", "production_site", "shop.craft");
//	final static List<String> TYPES = List.of("manufacturer", "factory", "industrial", 
//			"plant", "workshop", "combine", "production_site");
	final static String PLACES_TEXT_API_URL = "https://places.googleapis.com/v1/places:searchText";
	final static String API_FIELD_MASK = "places.displayName,places.websiteUri,places.adrFormatAddress,places.internationalPhoneNumber";

	
	public List<PlacesDetailsByIdDto> getNearbyDetailsProviders(Double longitude, Double latitude, Double radius)
			throws ApiException, InterruptedException, IOException {
		try {
			List<PlacesDetailsByIdDto> placesResponse = new ArrayList<>();
				int value = (int) Math.round(radius);
				String url = UriComponentsBuilder.fromUriString(PLACES_NEARBY_API_URL)
						.queryParam("location", new LatLng(latitude, longitude)).queryParam("radius", value)
						.queryParam("types", String.join("|", TYPES)).queryParam("key", apiKey).build().toString();
System.out.println("URL = " + url);
				List<PlacesIdDto> placesId = client.getForObject(url, PlacesByLocationResponseDto.class).getResults();
				if (placesId.size() < 0) {
					throw new PlacesInLocationNotFound();
				}
				placesId.stream().forEach(p -> {
					System.out.println("place = " + p.getPlace_id());
					String url1 = UriComponentsBuilder.fromUriString(PLACES_NEARBY_API_URL_DETAILS)
							.queryParam("place_id", p.getPlace_id()).queryParam("key", apiKey).build().toString();
					PlacesDetailsByIdDto place = client.getForObject(url1, PlacesDetailsResponsDto.class).getResult();
					if(place.getWebsite() == null) {
						place.setIcon("https://shetko.online/static/media/selfman.0dfe4b35a490aa138b91.png");
					}
					
//				parserWebSite.parseLogoExtractor(place);
//			    if(place.getPhotos() != null && place.getPhotos().size() > 0) {
//			    	String photoReference = place.getPhotos().get(0).getPhoto_reference();
//			    	String urlPhoto = UriComponentsBuilder.fromUriString(PLACES_API_PHOTO)
//			    			.queryParam("maxwidth", 400)
//		                 	.queryParam("photoreference", photoReference)
//			                .queryParam("key", apiKey).build().toString();
//			    	place.setIcon(urlPhoto);
//			    }
					
					placesResponse.add(place);
				});
				System.out.println("place size = " + placesResponse.size());
			return placesResponse;
		} catch (PlacesInLocationNotFound e) {
			e.getMessage();
			return null;
		} catch (UnableToRetrieveException e) {
			e.getMessage();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PlacesDetailsByIdDto> getProvidersByText(Double longitude, Double latitude, Double radius,
			String[] keywords) {
		PlacesApiTextRequestDto requestDto = getTextRequestBody(keywords, longitude, latitude, radius);
		HttpHeaders headers = getHttpHeaders();
		HttpEntity<PlacesApiTextRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);
		try {
			PlacesDetailsResponsDto placesResponse = client
					.exchange(PLACES_TEXT_API_URL, HttpMethod.POST, requestEntity, PlacesDetailsResponsDto.class)
					.getBody();
			return placesResponse == null ? List.of() : (List<PlacesDetailsByIdDto>) placesResponse;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new UnableToRetrieveException();
		}
	}

	private PlacesApiTextRequestDto getTextRequestBody(String[] keywords, Double latitude, Double longitude,
			Double radius) {
		PlacesLocationRestrictions restrictions = new PlacesLocationRestrictions(
				new PlacesCircle(new PlacesCircleCenter(latitude, longitude), radius));
		return PlacesApiTextRequestDto.builder().textQuery(String.join(" ", keywords)).maxResultCount(MAX_RESULT_COUNT)
				.locationBias(restrictions).build();
	}

	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("X-Goog-FieldMask", API_FIELD_MASK);
		headers.set("X-Goog-Api-Key", apiKey);
		return headers;
	}

}
