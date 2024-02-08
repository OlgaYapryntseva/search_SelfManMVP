package com.selfman.search.util;

import com.selfman.search.dto.SearchResultDto;
import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.model.Resource;

import java.util.List;

public class SearchResultMapper {
    public static SearchResultDto placeDescriptionToDto(PlacesDetailsByIdDto place, List<String> keywords) {
        return SearchResultDto.builder()
                .internationalPhone(place.getInternational_phone_number())
                .companyName(place.getName())
                .countryLocality(AddressParser.parseCountryLocality(place.getAdr_address()))
                .industry(place.getTypes())
                .keywords(keywords)
                .rating(place.getRating())
                .website(place.getWebsite())
                .logo(place.getIcon())
                .build();
    }

    public static SearchResultDto resourceToDto(Resource resource, List<String> keywords) {
        return SearchResultDto.builder()
                .internationalPhone(resource.getInternationalPhone())
                .companyName(resource.getCompanyName())
                .countryLocality(resource.getCountryLocality())
                .industry(resource.getIndustry())
                .keywords(keywords)
                .rating(resource.getRating())
                .website(resource.getResourceUrl())
                .logo(resource.getLogo())
                .build();
    }
}
