package com.example.url_shortner_backend.repository;

import com.example.url_shortner_backend.model.URL_Metadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface URL_MetadataRepository extends MongoRepository<URL_Metadata, String> {

    URL_Metadata findByTinyURL(String tinyUrl);
    URL_Metadata findByUrl(String url);
    boolean existsByTinyURL(String url);
    boolean existsByUrl(String url);
}
