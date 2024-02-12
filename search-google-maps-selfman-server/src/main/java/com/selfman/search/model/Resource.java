package com.selfman.search.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "places")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Resource {
//    @Id
//    String resourceUrl;
	@Id
    String place_id;
	String resourceUrl;
    String resourceContent;
    String companyName;
    String countryLocality;
    List<String> industry;
    String internationalPhone;
    Double rating;
    String logo;
}
