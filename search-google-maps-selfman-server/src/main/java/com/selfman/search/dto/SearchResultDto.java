package com.selfman.search.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchResultDto {
    String companyName;
    String countryLocality;
    List<String> industry;
    List<String> keywords;
    String internationalPhone;
    Double rating;
    String website;
    String logo;
}
