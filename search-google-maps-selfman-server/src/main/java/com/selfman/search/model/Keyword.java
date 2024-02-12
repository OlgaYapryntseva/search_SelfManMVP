package com.selfman.search.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "places_keywords")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Keyword {
    @Id
    String word;
    String place_id;
    @Field(type = FieldType.Nested, includeInParent = true)
    List<Resource> resources;
}
