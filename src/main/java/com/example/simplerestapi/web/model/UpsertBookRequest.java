package com.example.simplerestapi.web.model;

import lombok.Data;

@Data
public class UpsertBookRequest {

    private String name;

    private String author;

    private String release;

    private String categoryName;

}
