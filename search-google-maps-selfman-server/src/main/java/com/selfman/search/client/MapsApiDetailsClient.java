package com.selfman.search.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.selfman.search.dto.details.PlacesByLocationResponseDto;
import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.dto.details.PlacesDetailsResponsDto;
import com.selfman.search.dto.details.PlacesIdDto;
import com.selfman.search.exception.details.PlacesInLocationNotFound;
import com.selfman.search.exception.details.UnableToRetrieveException;
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

	@Value("${maps.api.key_1}")
	String apiKey;

	final static String PLACES_NEARBY_API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	final static String PLACES_NEARBY_API_URL_DETAILS = "https://maps.googleapis.com/maps/api/place/details/json";
	final static List<String> NEARBY_INCLUDED_TYPES = List.of("wholesaler", "store");

	public List<PlacesDetailsByIdDto> getNearbyDetailsProviders(Double longitude, Double latitude, Double radius)
			throws ApiException, InterruptedException, IOException {
		try {
			int value = (int) Math.round(radius);
			String url = UriComponentsBuilder.fromUriString(PLACES_NEARBY_API_URL)
					.queryParam("location", new LatLng(latitude, longitude)).queryParam("radius", value)
					.queryParam("type", NEARBY_INCLUDED_TYPES).queryParam("key", apiKey).build().toString();
			List<PlacesIdDto> placesId = client.getForObject(url, PlacesByLocationResponseDto.class).getResults();
			if (placesId.size() < 0) {
				throw new PlacesInLocationNotFound();
			}
			List<PlacesDetailsByIdDto> placesResponse = new ArrayList<>();
			placesId.stream().forEach(p -> {
				String url1 = UriComponentsBuilder.fromUriString(PLACES_NEARBY_API_URL_DETAILS)
						.queryParam("place_id", p.getPlace_id()).queryParam("key", apiKey).build().toString();
//			    PlacesDetailsByIdDto place = client.getForObject(url1, PlacesDetailsResponsDto.class).getResult();
//				logoExtractor(place);
				placesResponse.add(client.getForObject(url1, PlacesDetailsResponsDto.class).getResult());
			});
			
			System.out.println("place size= " + placesResponse.size());
			return placesResponse;
		} catch (PlacesInLocationNotFound e) {
			e.getMessage();
			return null;
		} catch (UnableToRetrieveException e) {
			e.getMessage();
			return null;
		}
	}

	public void logoExtractor(PlacesDetailsByIdDto place) {
		try {
			if (place.getWebsite() != null) {
				Document document = Jsoup.connect(place.getWebsite()).get();
				String src = document.getElementsByTag("img").get(0).attr("src");
				String[] srcTrue = src.split("://");
				if (src != null && srcTrue[0].equals("https")) {
					place.setIcon(src);
					System.out.println("place_url = " + place.getIcon());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
