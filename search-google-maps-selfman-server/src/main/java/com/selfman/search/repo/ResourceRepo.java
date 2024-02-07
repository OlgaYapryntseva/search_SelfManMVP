package com.selfman.search.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.selfman.search.entity.Resource;

import java.util.List;

@Repository
public interface ResourceRepo extends ElasticsearchRepository<Resource, String> {
    List<Resource> findByResourceContentContaining(String searchRequest);
}
