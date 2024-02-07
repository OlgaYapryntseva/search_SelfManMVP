package com.selfman.search.dto.details;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class PlacesDetailsByIdDto {
    String name;
    String formatted_phone_number;
    String international_phone_number;
    String formatted_address;
    String adr_address;
    List<String> types;  
    Double rating;
    String user_ratings_total;
    String website;  
    String icon;   
}
