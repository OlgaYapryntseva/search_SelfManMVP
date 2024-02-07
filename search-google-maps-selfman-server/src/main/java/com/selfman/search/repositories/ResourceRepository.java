package com.selfman.search.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.selfman.search.model.Resource;

import java.util.List;

@Repository
public interface ResourceRepository extends ElasticsearchRepository<Resource, String> {
    List<Resource> findByResourceContentContaining(String searchRequest);
}
