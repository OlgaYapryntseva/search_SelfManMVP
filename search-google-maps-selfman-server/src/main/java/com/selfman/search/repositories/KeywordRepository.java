package com.selfman.search.repositories;

import java.util.List;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.selfman.search.model.Keyword;


@Repository
public interface KeywordRepository extends ElasticsearchRepository<Keyword, String> {
    @Query("{\"nested\": {\"path\": \"resources\", \"query\": {\"match\": {\"resources.resourceUrl.keyword\": \"?0\"}}}}")
    List<Keyword> findByResourceUrl(String resourceUrl);
}
