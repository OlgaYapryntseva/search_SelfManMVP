package com.selfman.search.dto.palm2_api;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Palm2ApiRequestDto {
    Palm2Prompt prompt;
    Double temperature;
    Integer candidateCount;
}
