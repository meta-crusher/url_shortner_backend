package com.example.url_shortner_backend.service;

import com.example.url_shortner_backend.model.CustomizedURL;
import com.example.url_shortner_backend.model.URL_Metadata;
import com.example.url_shortner_backend.model.URL_Response;
import com.example.url_shortner_backend.repository.URL_MetadataRepository;

import static com.example.url_shortner_backend.utils.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Random;


@Service
public class URL_ShortnerService {

    URL_MetadataRepository url_metadataRepository;

    @Autowired
    URL_ShortnerService(URL_MetadataRepository url_metadataRepository) {
        this.url_metadataRepository = url_metadataRepository;
    }

    public String createTinyURL() {
        String urlKey = createUrlKey();
        return DOMAIN + urlKey;
    }

    private String createUrlKey() {
        int size = 8;
        StringBuilder urlKey = new StringBuilder();
        while (size-- > 0) {
            Random random = new Random();
            urlKey.append(CHARACTER_POOL.charAt(random.nextInt(36)));
        }

        return urlKey.toString();
    }


    public ResponseEntity<URL_Response> generateURL(String url) {

        try {
            url = URLDecoder.decode(url, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        //If url is tinyURL, return error.
        if (url_metadataRepository.existsByTinyURL(url)) {
            return ResponseEntity.badRequest().body(
                    URL_Response.builder().success(false).message("URL is already Shortened").build()
            );
        }

        //Check if the url is already present or not.
        //If present, return the same meta_Data.
        if (url_metadataRepository.existsByUrl(url)) {
            URL_Metadata urlMetadata = url_metadataRepository.findByUrl(url);
            return ResponseEntity.ok().body(
                    URL_Response.builder()
                            .success(true)
                            .message("TinyURL found Successfully")
                            .urlMetadata(urlMetadata).build()
            );
        }
        //If not present create new url and save in the DB.
        String tinyUrl = createTinyURL();

        URL_Metadata response = url_metadataRepository.save(
                URL_Metadata.builder()
                        .tinyURL(tinyUrl)
                        .url(url)
                        .hits(0)
                        .build()
        );

        return ResponseEntity.ok(URL_Response.builder()
                .success(true)
                .message("TinyURL created Successfully")
                .urlMetadata(response).build()
        );
    }

    public ResponseEntity<URL_Response> getURL(String tinyURL) {
        if (url_metadataRepository.existsByTinyURL(tinyURL)) {
            return ResponseEntity.ok(URL_Response.builder()
                    .success(true)
                    .message("TinyURL found")
                    .urlMetadata(url_metadataRepository.findByTinyURL(tinyURL))
                    .build()
            );
        }

        return ResponseEntity.badRequest().body(
                URL_Response.builder()
                .success(false)
                .message("URL not found")
                .urlMetadata(null)
                .build()
        );
    }

    public ResponseEntity<URL_Response> customizeUrl(CustomizedURL url) {
        if (url_metadataRepository.existsByUrl(url.getCustomizedURL())) {
            return ResponseEntity.badRequest().body(
                    URL_Response.builder().success(false).message("Customized URL already present").build()
            );
        }
        URL_Metadata urlMetadata = url_metadataRepository.findByUrl(url.getUrl());
        urlMetadata.setTinyURL(url.getCustomizedURL());
        URL_Metadata response = url_metadataRepository.save(urlMetadata);

        return ResponseEntity.ok(URL_Response.builder()
                .success(true)
                .message("TinyURL created Successfully")
                .urlMetadata(response)
                .build()
        );
    }
}
