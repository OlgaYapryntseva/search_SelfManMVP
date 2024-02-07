package com.selfman.search.dto.places_api;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomPlaceDescription {
    String websiteUri;
    String adrFormatAddress;
    PlacesDisplayName displayName;
    String internationalPhoneNumber;
    int rating;
}
