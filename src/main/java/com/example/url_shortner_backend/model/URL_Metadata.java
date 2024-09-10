package com.example.url_shortner_backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Repository;

@Document(collection = "URL_Metadata")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class URL_Metadata {

    private String tinyURL;
    private String url;
    private Integer hits;
}