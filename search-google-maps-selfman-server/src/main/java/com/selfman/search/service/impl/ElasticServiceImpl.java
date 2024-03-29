package com.selfman.search.service.impl;

import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.model.Keyword;
import com.selfman.search.model.Resource;
import com.selfman.search.repositories.KeywordRepository;
import com.selfman.search.repositories.ResourceRepository;
import com.selfman.search.service.interfaces.ElasticService;
import com.selfman.search.util.ResourceMapper;
import com.selfman.search.util.WebScrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElasticServiceImpl implements ElasticService {
   final KeywordRepository keywordRepo;
   final ResourceRepository resourceRepo;

    @Override
    public Resource saveResourceWithOutKeywords(PlacesDetailsByIdDto place) {
		if (!checkIfResourceExistsById(place.getPlace_id())) {
			String webSite = place.getWebsite();
			String resourceContent = WebScrapper.scrapResource(webSite);
			Resource newResource = ResourceMapper.customPlaceDescriptionToResource(resourceContent, place);
			resourceRepo.save(newResource); 
			return newResource;
		}
        return resourceRepo.findById(place.getPlace_id()).get();
    }

    @Override
    public Resource saveResourceWithKeywords(String[] keywords, PlacesDetailsByIdDto place, String resourceContent) {
        Resource newResource = ResourceMapper.customPlaceDescriptionToResource(resourceContent, place);
        for (String keyword : keywords) {
            var word = keywordRepo.findById(keyword);
            //If the keyword is already cached - add reference to the resource
            if (word.isPresent()) {
                Keyword keywordToBeUpdated = word.get();
                updateKeyword(keywordToBeUpdated, newResource);
            } else {
                //If keyword is not cached - save new keyword and add reference to the resources
                saveKeywordWithNewResources(keyword, List.of(newResource));
            }
        }
        return resourceRepo.save(newResource);
    }

    @Override
    public List<Resource> findResourcesByCachedKeywords(String[] keywords) {
        List<Resource> foundResources = new ArrayList<>();
        for (String keyword : keywords) {
            Optional<Keyword> foundKeyword = keywordRepo.findById(keyword);
            foundKeyword.ifPresent(value -> foundResources.addAll(value.getResources()));
        }
        return foundResources;
    }

    @Override
    public List<Resource> findResourcesByNonCachedKeywords(String[] keywords) {
        List<Resource> foundResources = new ArrayList<>();
        for (String keyword : keywords) {
            List<Resource> res = resourceRepo.findByResourceContentContaining(keyword);
            //If resources are found - save new keyword and add reference to the resources
            if (!res.isEmpty()) {
                saveKeywordWithNewResources(keyword, res);
            }
            foundResources.addAll(res);
        }
        return foundResources;
    }

    @Override
    public boolean checkIfResourceExistsById(String id) {
        return resourceRepo.existsById(id);
    }

    @Override
    public Resource findResourceByUrl(String url) {
        return resourceRepo.findById(url).orElse(null);
    }

    @Override
    public Set<String> findKeywordsByResourceUrl(String url) {
        return keywordRepo.findByResourceUrl(url).stream().map(Keyword::getWord).collect(Collectors.toSet());
    }

    private void updateKeyword(Keyword keyword, Resource resource) {
        List<Resource> resourcesToBeUpdated = keyword.getResources();
        resourcesToBeUpdated.add(resource);
        keyword.setResources(resourcesToBeUpdated);
        keywordRepo.save(keyword);
    }

    private void saveKeywordWithNewResources(String keyword, List<Resource> resources) {
        Keyword newKeyword = new Keyword();
        newKeyword.setWord(keyword);
        newKeyword.setResources(resources);
        keywordRepo.save(newKeyword);
    }
}
