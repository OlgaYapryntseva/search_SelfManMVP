package com.selfman.search.util;

import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.entity.Resource;

public class ResourceMapper {
    public static Resource customPlaceDescriptionToResource(String resourceContent, PlacesDetailsByIdDto placesDetailsById) {
        Resource resource = new Resource();
        resource.setResourceUrl(placesDetailsById.getWebsite());
        resource.setResourceContent(resourceContent);
        resource.setCompanyName(placesDetailsById.getName());
        resource.setCountryLocality(AddressParser.parseCountryLocality(placesDetailsById.getAdr_address()));
        //This will be fixed in further versions
        resource.setIndustry(placesDetailsById.getTypes());
        resource.setInternationalPhone(placesDetailsById.getInternational_phone_number());
        resource.setRating(placesDetailsById.getRating());
        resource.setLogo(placesDetailsById.getIcon());
        return resource;
    }
}
