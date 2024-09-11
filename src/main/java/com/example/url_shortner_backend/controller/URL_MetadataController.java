package com.example.url_shortner_backend.controller;

import com.example.url_shortner_backend.model.CustomizedURL;
import com.example.url_shortner_backend.model.URL_Metadata;
import com.example.url_shortner_backend.model.URL_Response;
import com.example.url_shortner_backend.service.URL_ShortnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.url_shortner_backend.utils.Constants.DOMAIN;

@RestController
public class URL_MetadataController {

    URL_ShortnerService urlShortnerService;

    @Autowired
    URL_MetadataController(URL_ShortnerService urlShortnerService){
        this.urlShortnerService = urlShortnerService;
    }

    @PostMapping(value = "/generateUrl")
    ResponseEntity<URL_Response> generateUrl(@RequestBody String url){
        return urlShortnerService.generateURL(url);
    }

    @PostMapping(value = "/customizeUrl")
    ResponseEntity<URL_Response> customizeUrl(@RequestBody CustomizedURL url){
        return urlShortnerService.customizeUrl(url);
    }

    @GetMapping(value = "/{tinyURL}")
    ResponseEntity<URL_Response> redirect(@PathVariable String tinyURL){
        return urlShortnerService.getURL(DOMAIN + tinyURL);
    }

}
