package com.selfman.search.client;

import com.selfman.search.dto.palm2_api.Palm2ApiRequestDto;
import com.selfman.search.dto.palm2_api.Palm2ApiResponseDto;
import com.selfman.search.dto.palm2_api.Palm2Prompt;
import com.selfman.search.exception.UnableToRetrieveException;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Palm2ApiClient {
    final static int CANDIDATE_COUNT = 1;
    final static double TEMPERATURE = 1.0;
    final static String PALM2_API_URL = "https://generativelanguage.googleapis.com/v1beta2/models/text-bison-001:generateText";
    @Autowired
    RestTemplate client;
    @Value("${palm2.api.key}")
    String apiKey;

    public Palm2ApiResponseDto getPalm2IndustryKeywordsSummary(String[] titleDescription) {
        Palm2ApiRequestDto requestDto = getRequestBody(titleDescription[0], titleDescription[1]);
        String uri = UriComponentsBuilder.fromUriString(PALM2_API_URL)
                .queryParam("key", apiKey).toUriString();
        try {
            return client.exchange(uri, HttpMethod.POST, new HttpEntity<>(requestDto), Palm2ApiResponseDto.class).getBody();
        } catch (Exception e) {
            throw new UnableToRetrieveException("Unable to retrieve response from Palm2 API");
        }
    }

    private Palm2ApiRequestDto getRequestBody(String title, String description) {
        return Palm2ApiRequestDto.builder()
                .prompt(new Palm2Prompt(getPromptText(title, description)))
                .temperature(TEMPERATURE)
                .candidateCount(CANDIDATE_COUNT)
                .build();
    }

    private String getPromptText(String title, String description) {
        return String.format("Using the title (%s) and description (%s) of the HTML page describe (a) with maximum 3 " +
                "words the company's industry and (b) the most applicable keywords (maximum 3). Your answer must contain " +
                "two sentences ending with dot - first sentence shall describe the company's industry and second shall " +
                "contain keywords separated with ordinary commas. First sentence shall contain only one word (industry name). " +
                "Second sentence shall contain only keywords separated by commas. Structure of your answer MUST BE as follows: " +
                "'Marketplace. B2B, produce, wholesale'. You must always follow the example's structure. You must not include any words " +
                "contradicting to the structure described above (i.e. sentences 'Industry: Marketing. Keywords: services, B2B, wholesale' " +
                "MUST NOT be used as they contain 'Industry:' and 'Keywords:' words). Two sentences must always be divided with dot(.)! " +
                "The sentences must always be in English.", title, description);
    }
}
