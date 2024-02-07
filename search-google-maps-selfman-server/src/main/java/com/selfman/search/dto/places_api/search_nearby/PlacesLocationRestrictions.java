package com.selfman.search.dto.places_api.search_nearby;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlacesLocationRestrictions {
    PlacesCircle circle;
}
