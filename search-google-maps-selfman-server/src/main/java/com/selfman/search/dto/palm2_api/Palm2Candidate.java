package com.selfman.search.dto.palm2_api;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Palm2Candidate {
    String output;
    List<Palm2SafetyRating> safetyRatings;
}
