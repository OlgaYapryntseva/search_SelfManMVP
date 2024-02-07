package com.selfman.search.dto.details;


import java.util.List;
import lombok.Getter;


@Getter
public class PlacesByLocationResponseDto {
    List<PlacesIdDto> results;
    String status;
	
}