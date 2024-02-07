package com.selfman.search.dto.places_api.text_search;

import com.selfman.search.dto.places_api.search_nearby.PlacesLocationRestrictions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlacesApiTextRequestDto {
    String textQuery;
    Integer maxResultCount;
    PlacesLocationRestrictions locationBias;
}
