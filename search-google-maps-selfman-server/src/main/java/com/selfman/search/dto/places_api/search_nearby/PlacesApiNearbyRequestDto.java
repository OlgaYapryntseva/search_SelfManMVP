package com.selfman.search.dto.places_api.search_nearby;

import lombok.AccessLevel;
import lombok.Builder;
//import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlacesApiNearbyRequestDto {
    List<String> includedTypes;
    int maxResultCount;
    PlacesLocationRestrictions locationRestriction;
    
}
